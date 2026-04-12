package com.athletiq.app.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.ExerciseEntity
import com.athletiq.app.data.local.entity.ExerciseLogEntity
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.data.repository.WorkoutLogRepository
import com.athletiq.app.domain.model.BlockWithExercises
import com.athletiq.app.domain.model.SessionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the guided workout execution screen — the core experience.
 *
 * **Screen:** [WorkoutScreen]
 *
 * **State managed:**
 * - [uiState]: The current phase of the workout (loading, exercising, resting, complete).
 * - Tracks which block, exercise, and set the user is currently on.
 * - Accumulates per-set log data (weight, reps) for saving.
 *
 * **User actions handled:**
 * - Complete a set (logs weight + reps, advances to next set or rest timer).
 * - Skip rest timer.
 * - Navigate back (prompts to save partial workout).
 * - Finish workout (saves all data, updates enrollment progress).
 *
 * **Workout flow:**
 * ```
 * Load Session → Exercise (Set 1) → [Complete Set] → Rest Timer → Exercise (Set 2)
 *   → ... → Next Exercise → ... → Next Block → ... → Workout Complete
 * ```
 */
@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val workoutLogRepository: WorkoutLogRepository,
    private val enrollmentRepository: EnrollmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    // Workout tracking state.
    private var sessionDetail: SessionDetail? = null
    private var workoutLogId: Long = -1L
    private var enrollmentId: Long = -1L
    private var startTimeMillis: Long = 0L

    // Flat list of all exercises across all blocks for sequential navigation.
    private var allExercises: List<ExerciseWithBlockInfo> = emptyList()
    private var currentExerciseIndex: Int = 0
    private var currentSetNumber: Int = 1

    // Accumulated set logs to save.
    private val exerciseLogs = mutableListOf<ExerciseLogEntity>()

    // Current input state.
    private var currentWeightInput: String = ""
    private var currentRepsInput: String = ""

    /**
     * Initializes the workout by loading the session detail and creating a workout log.
     *
     * @param sessionId The session template to execute.
     * @param enrollmentId The active enrollment for logging.
     * @param dayId The parent day ID for session lookup.
     */
    fun initWorkout(sessionId: Long, enrollmentId: Long, dayId: Long) {
        this.enrollmentId = enrollmentId

        viewModelScope.launch {
            try {
                // Load the day entity first.
                val days = programRepository.getDaysByWeek(0) // We need the day by ID.
                // Actually, we should get the day from the dayId directly.
                // The ProgramDao doesn't have a getDay(dayId) method, so we build the session
                // detail differently — using getSessionsByDay and matching the session ID.

                // Build session detail: load the session, its blocks, and exercises.
                val sessions = loadSessionsForDay(dayId)
                val session = sessions.firstOrNull { it.id == sessionId }

                if (session == null) {
                    _uiState.value = WorkoutUiState.Error("Session not found")
                    return@launch
                }

                val blocks = programRepository.getSessionDetail(sessionId, findDayById(dayId))
                if (blocks == null) {
                    _uiState.value = WorkoutUiState.Error("Failed to load session details")
                    return@launch
                }

                sessionDetail = blocks

                // Create the workout log in the database.
                workoutLogId = workoutLogRepository.startWorkoutLog(
                    enrollmentId = enrollmentId,
                    sessionId = sessionId,
                    date = LocalDate.now()
                )
                startTimeMillis = System.currentTimeMillis()

                // Flatten all exercises from all blocks into a sequential list.
                allExercises = blocks.blocks.flatMap { blockWithExercises ->
                    blockWithExercises.exercises.map { exercise ->
                        ExerciseWithBlockInfo(
                            exercise = exercise,
                            blockName = blockWithExercises.block.name,
                            blockType = blockWithExercises.block.blockType
                        )
                    }
                }

                if (allExercises.isEmpty()) {
                    _uiState.value = WorkoutUiState.Error("No exercises in this session")
                    return@launch
                }

                // Pre-fill weight for the first exercise.
                currentExerciseIndex = 0
                currentSetNumber = 1
                prefillWeight(allExercises[0].exercise.name)
                emitExercisingState()

            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(
                    e.message ?: "Failed to initialize workout"
                )
            }
        }
    }

    /**
     * Updates the weight input for the current set.
     */
    fun updateWeightInput(value: String) {
        currentWeightInput = value
        emitExercisingState()
    }

    /**
     * Updates the reps input for the current set.
     */
    fun updateRepsInput(value: String) {
        currentRepsInput = value
        emitExercisingState()
    }

    /**
     * Completes the current set, logs it, and advances to rest timer or next exercise.
     */
    fun completeSet() {
        val currentExercise = allExercises.getOrNull(currentExerciseIndex) ?: return

        // Log the completed set.
        val log = ExerciseLogEntity(
            workoutLogId = workoutLogId,
            exerciseId = currentExercise.exercise.id,
            exerciseName = currentExercise.exercise.name,
            setNumber = currentSetNumber,
            weightKg = currentWeightInput.toFloatOrNull(),
            repsCompleted = currentRepsInput.toIntOrNull(),
            notes = null
        )
        exerciseLogs.add(log)

        // Save the set immediately (don't wait for workout completion).
        viewModelScope.launch {
            workoutLogRepository.logExerciseSet(log)
        }

        // Decide what's next: another set, rest timer, next exercise, or workout complete.
        val trackable = isExerciseTrackable(currentExercise.exercise)
        if (currentSetNumber < currentExercise.exercise.sets) {
            if (trackable) {
                // More sets remaining — show rest timer, then advance to next set.
                _uiState.value = WorkoutUiState.Resting(
                    exerciseName = currentExercise.exercise.name,
                    completedSet = currentSetNumber,
                    totalSets = currentExercise.exercise.sets,
                    restSeconds = currentExercise.exercise.restSeconds,
                    totalExercises = allExercises.size,
                    currentExerciseIndex = currentExerciseIndex
                )
                currentSetNumber++
            } else {
                // Non-trackable: skip rest, just advance the set.
                currentSetNumber++
                emitExercisingState()
            }
        } else {
            // All sets complete for this exercise — advance to next exercise.
            advanceToNextExercise()
        }
    }

    /**
     * Called when the rest timer finishes (naturally or via skip).
     * Returns to the exercising state for the next set.
     */
    fun onRestComplete() {
        emitExercisingState()
    }

    /**
     * Finishes the workout — saves duration, increments enrollment progress.
     */
    fun finishWorkout() {
        viewModelScope.launch {
            val durationMinutes = ((System.currentTimeMillis() - startTimeMillis) / 60_000).toInt()
            workoutLogRepository.finishWorkout(workoutLogId, durationMinutes)
            enrollmentRepository.incrementCompletedDays(enrollmentId)
            _uiState.value = WorkoutUiState.Complete(durationMinutes = durationMinutes)
        }
    }

    // ── Private Helpers ────────────────────────────────────────────────────────

    private fun advanceToNextExercise() {
        currentExerciseIndex++
        currentSetNumber = 1

        if (currentExerciseIndex >= allExercises.size) {
            // All exercises done — workout is complete.
            finishWorkout()
            return
        }

        // Pre-fill weight for trackable exercises and show exercising state.
        viewModelScope.launch {
            val next = allExercises[currentExerciseIndex]
            if (isExerciseTrackable(next.exercise)) {
                prefillWeight(next.exercise.name)
            } else {
                currentWeightInput = ""
                currentRepsInput = ""
            }
            emitExercisingState()
        }
    }

    private suspend fun prefillWeight(exerciseName: String) {
        val lastWeight = workoutLogRepository.getLastWeightForExercise(exerciseName)
        currentWeightInput = lastWeight?.let {
            // Format nicely: remove trailing .0 if it's a whole number.
            if (it % 1f == 0f) it.toInt().toString() else it.toString()
        } ?: ""
        currentRepsInput = ""
    }

    private fun emitExercisingState() {
        val currentExercise = allExercises.getOrNull(currentExerciseIndex) ?: return
        _uiState.value = WorkoutUiState.Exercising(
            exerciseName = currentExercise.exercise.name,
            targetMuscles = currentExercise.exercise.targetMuscles,
            blockName = currentExercise.blockName,
            currentSet = currentSetNumber,
            totalSets = currentExercise.exercise.sets,
            targetReps = currentExercise.exercise.reps,
            tempo = currentExercise.exercise.tempo,
            rpe = currentExercise.exercise.rpe,
            notes = currentExercise.exercise.notes,
            weightKg = currentWeightInput,
            repsCompleted = currentRepsInput,
            totalExercises = allExercises.size,
            currentExerciseIndex = currentExerciseIndex,
            isTrackable = isExerciseTrackable(currentExercise.exercise)
        )
    }

    private fun isExerciseTrackable(exercise: ExerciseEntity): Boolean {
        val reps = exercise.reps.lowercase()
        return !reps.contains("min") && !reps.contains("sec")
    }

    private suspend fun findDayById(dayId: Long): com.athletiq.app.data.local.entity.DayEntity {
        return programRepository.getDayById(dayId)
            ?: throw IllegalStateException("Day $dayId not found")
    }

    private suspend fun loadSessionsForDay(dayId: Long): List<com.athletiq.app.data.local.entity.SessionEntity> {
        return programRepository.getSessionsByDay(dayId)
    }
}

/**
 * Exercise paired with its parent block metadata, used for flat sequential navigation.
 */
data class ExerciseWithBlockInfo(
    val exercise: ExerciseEntity,
    val blockName: String,
    val blockType: String
)

/**
 * Sealed UI state for the workout execution screen.
 */
sealed interface WorkoutUiState {
    /** Loading session and exercise data. */
    data object Loading : WorkoutUiState

    /**
     * User is performing an exercise set.
     */
    data class Exercising(
        val exerciseName: String,
        val targetMuscles: String?,
        val blockName: String,
        val currentSet: Int,
        val totalSets: Int,
        val targetReps: String,
        val tempo: String?,
        val rpe: Int?,
        val notes: String?,
        val weightKg: String,
        val repsCompleted: String,
        val totalExercises: Int,
        val currentExerciseIndex: Int,
        val isTrackable: Boolean = true
    ) : WorkoutUiState

    /**
     * Rest timer between sets.
     */
    data class Resting(
        val exerciseName: String,
        val completedSet: Int,
        val totalSets: Int,
        val restSeconds: Int,
        val totalExercises: Int,
        val currentExerciseIndex: Int
    ) : WorkoutUiState

    /**
     * Workout finished — all exercises and sets completed.
     */
    data class Complete(val durationMinutes: Int) : WorkoutUiState

    /** An error occurred. */
    data class Error(val message: String) : WorkoutUiState
}

// End of WorkoutViewModel.kt — Core workout execution engine with set tracking and logging.
