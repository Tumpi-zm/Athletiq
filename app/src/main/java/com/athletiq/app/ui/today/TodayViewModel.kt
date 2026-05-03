package com.athletiq.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.dao.WorkoutKey
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.data.repository.WorkoutLogRepository
import com.athletiq.app.domain.model.SessionDetail
import com.athletiq.app.domain.usecase.AbandonProgramResult
import com.athletiq.app.domain.usecase.AbandonProgramUseCase
import com.athletiq.app.domain.usecase.GetTodaySessionUseCase
import com.athletiq.app.domain.usecase.GetWeekScheduleUseCase
import com.athletiq.app.domain.usecase.WeekDaySummary
import com.athletiq.app.domain.usecase.WeekScheduleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val enrollmentRepository: EnrollmentRepository,
    private val programRepository: ProgramRepository,
    private val workoutLogRepository: WorkoutLogRepository,
    private val getTodaySessionUseCase: GetTodaySessionUseCase,
    private val getWeekScheduleUseCase: GetWeekScheduleUseCase,
    private val abandonProgramUseCase: AbandonProgramUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodayUiState>(TodayUiState.Loading)
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val _abandonEvent = MutableStateFlow<AbandonProgramResult?>(null)
    val abandonEvent: StateFlow<AbandonProgramResult?> = _abandonEvent.asStateFlow()

    /**
     * Latest snapshot of finished workout keys for the active enrollment.
     * Updated reactively via [startObservingFinishedWorkouts]. Used by [selectDay] and
     * [navigateWeek] so they can immediately recompute completion indicators on state changes.
     */
    private val _finishedKeys = MutableStateFlow<List<WorkoutKey>>(emptyList())

    /** Coroutine job that collects [WorkoutLogRepository.getFinishedWorkoutKeys]. */
    private var finishedWorkoutJob: Job? = null

    /** Coroutine job that collects in-progress workout logs for the active enrollment. */
    private var inProgressWorkoutJob: Job? = null

    init {
        observeActiveEnrollment()
    }

    private fun observeActiveEnrollment() {
        viewModelScope.launch {
            enrollmentRepository.getActiveEnrollment()
                .collect { enrollment ->
                    if (enrollment == null) {
                        finishedWorkoutJob?.cancel()
                        inProgressWorkoutJob?.cancel()
                        _finishedKeys.value = emptyList()
                        _uiState.value = TodayUiState.NoActiveProgram
                        return@collect
                    }
                    resolveWeekSchedule(enrollment)
                    startObservingFinishedWorkouts(enrollment.id)
                    startObservingInProgressWorkouts(enrollment.id)
                }
        }
    }

    /**
     * Starts (or restarts) a coroutine that watches finished workout keys for [enrollmentId].
     * Every emission recomputes the completion indicators for the currently displayed week/day.
     */
    private fun startObservingFinishedWorkouts(enrollmentId: Long) {
        finishedWorkoutJob?.cancel()
        finishedWorkoutJob = viewModelScope.launch {
            workoutLogRepository.getFinishedWorkoutKeys(enrollmentId)
                .collect { keys ->
                    _finishedKeys.value = keys
                    val current = _uiState.value as? TodayUiState.WeekView ?: return@collect
                    _uiState.value = updateCompletedIndicators(keys, current)
                }
        }
    }

    /**
     * Starts (or restarts) a coroutine that watches in-progress workout logs for [enrollmentId].
     * Every emission updates [TodayUiState.WeekView.inProgressSessionIds] for the Today screen
     * to show "Continue Workout" instead of "Start Workout".
     */
    private fun startObservingInProgressWorkouts(enrollmentId: Long) {
        inProgressWorkoutJob?.cancel()
        inProgressWorkoutJob = viewModelScope.launch {
            workoutLogRepository.getInProgressWorkoutLogsForDate(enrollmentId, LocalDate.now())
                .collect { inProgressLogs ->
                    val current = _uiState.value as? TodayUiState.WeekView ?: return@collect
                    val inProgressSessionIds = inProgressLogs.map { it.sessionId }.toSet()
                    _uiState.value = current.copy(inProgressSessionIds = inProgressSessionIds)
                }
        }
    }

    private suspend fun resolveWeekSchedule(enrollment: EnrollmentEntity) {
        val program = programRepository.getProgramById(enrollment.programId)
            ?: run {
                _uiState.value = TodayUiState.Error("Program not found")
                return
            }

        val totalTrainingDays = programRepository.getTotalTrainingDays(enrollment.programId)

        when (val result = getTodaySessionUseCase(enrollment)) {
            is WeekScheduleResult.ActiveWeek -> {
                val todaySummary = result.days.first { it.dayOfWeek == result.todayDayOfWeek }
                // Load session details for today's selected day.
                val sessions = loadSessionsForDay(todaySummary)
                val allDaySessionIds = loadAllDaySessionIds(result.days)
                // Eagerly load in-progress state so "Continue Workout" shows immediately on
                // first render and whenever enrollment re-emits (e.g. after incrementCompletedDays).
                val inProgressSessionIds = loadInProgressSessionIds(enrollment.id, sessions)

                _uiState.value = TodayUiState.WeekView(
                    enrollment = enrollment,
                    program = program,
                    weekNumber = result.weekNumber,
                    weekFocus = result.weekFocus,
                    days = result.days,
                    todayDayOfWeek = result.todayDayOfWeek,
                    selectedDayOfWeek = result.todayDayOfWeek,
                    selectedDaySessions = sessions,
                    selectedDaySummary = todaySummary,
                    completedDays = enrollment.completedDays,
                    totalTrainingDays = totalTrainingDays,
                    currentWeekNumber = result.weekNumber,
                    totalWeeks = program.durationWeeks,
                    allDaySessionIds = allDaySessionIds,
                    inProgressSessionIds = inProgressSessionIds
                )
                // Completion indicators will be applied on the next finishedWorkoutJob emission.
            }
            is WeekScheduleResult.ProgramComplete -> {
                _uiState.value = TodayUiState.ProgramComplete(enrollment, program)
            }
            is WeekScheduleResult.NotStartedYet -> {
                _uiState.value = TodayUiState.NotStartedYet(enrollment, program, result.startDate)
            }
            is WeekScheduleResult.Error -> {
                _uiState.value = TodayUiState.Error(result.message)
            }
        }
    }

    /**
     * Called when the user taps a different day in the week selector.
     * Loads sessions for the new day and recomputes completion indicators.
     */
    fun selectDay(dayOfWeek: Int) {
        val current = _uiState.value as? TodayUiState.WeekView ?: return
        if (dayOfWeek == current.selectedDayOfWeek) return

        val daySummary = current.days.first { it.dayOfWeek == dayOfWeek }

        viewModelScope.launch {
            val sessions = loadSessionsForDay(daySummary)
            // Eagerly check which sessions for the newly selected day are in-progress.
            val inProgressSessionIds = loadInProgressSessionIds(current.enrollment.id, sessions)
            val newState = current.copy(
                selectedDayOfWeek = dayOfWeek,
                selectedDaySessions = sessions,
                selectedDaySummary = daySummary
            )
            _uiState.value = updateCompletedIndicators(_finishedKeys.value, newState)
                .copy(inProgressSessionIds = inProgressSessionIds)
        }
    }

    /**
     * Returns a map of dayOfWeek (1=Mon..7=Sun) → list of sessionIds scheduled for that day.
     * Used by [updateCompletedIndicators] to determine which calendar day a finished session
     * belongs to, regardless of the actual date it was performed.
     */
    private suspend fun loadAllDaySessionIds(days: List<WeekDaySummary>): Map<Int, List<Long>> {
        val result = mutableMapOf<Int, List<Long>>()
        for (day in days) {
            if (!day.isRestDay && day.dayEntity != null) {
                val sessions = programRepository.getSessionsByDay(day.dayEntity.id)
                result[day.dayOfWeek] = sessions.map { it.id }
            }
        }
        return result
    }

    private suspend fun loadSessionsForDay(day: WeekDaySummary): List<SessionDetail> {
        if (day.isRestDay || day.dayEntity == null) return emptyList()
        return getTodaySessionUseCase.getSessionDetailsForDay(day.dayEntity)
    }

    /**
     * Returns the set of session IDs from [selectedDaySessions] that have been started today
     * but not yet finished (i.e. [WorkoutLogEntity.durationMinutes] IS NULL).
     *
     * Uses the one-shot [WorkoutLogRepository.getActiveWorkoutLog] suspend query — the same
     * path that [WorkoutViewModel.initWorkout] already relies on, so it is known to work.
     */
    private suspend fun loadInProgressSessionIds(
        enrollmentId: Long,
        selectedDaySessions: List<SessionDetail>
    ): Set<Long> {
        val today = LocalDate.now()
        return selectedDaySessions
            .filter { sessionDetail ->
                workoutLogRepository.getActiveWorkoutLog(sessionDetail.session.id, today) != null
            }
            .map { it.session.id }
            .toSet()
    }

    /**
     * Re-checks which sessions are currently in-progress and updates [TodayUiState.WeekView].
     * Call this whenever the screen becomes visible again (e.g. after navigating back from
     * WorkoutScreen) to guarantee "Continue Workout" is shown without waiting for a Flow emission.
     */
    fun refreshInProgress() {
        val current = _uiState.value as? TodayUiState.WeekView ?: return
        viewModelScope.launch {
            val inProgressSessionIds = loadInProgressSessionIds(
                current.enrollment.id,
                current.selectedDaySessions
            )
            _uiState.value = current.copy(inProgressSessionIds = inProgressSessionIds)
        }
    }

    fun abandonProgram() {
        viewModelScope.launch {
            val result = abandonProgramUseCase()
            _abandonEvent.value = result
        }
    }

    fun clearAbandonEvent() {
        _abandonEvent.value = null
    }

    /**
     * Navigates the week strip forward or backward by [delta] weeks (+1 = next, -1 = previous).
     * Clamps to [1, totalWeeks] so the user cannot go out of bounds.
     * The selected day resets to Monday (day 1) of the new week.
     * Completion indicators are recomputed for the new week.
     */
    fun navigateWeek(delta: Int) {
        val current = _uiState.value as? TodayUiState.WeekView ?: return
        val newWeekNumber = (current.weekNumber + delta).coerceIn(1, current.totalWeeks)
        if (newWeekNumber == current.weekNumber) return  // already at boundary

        viewModelScope.launch {
            val result = getWeekScheduleUseCase(current.enrollment, newWeekNumber)
            if (result is WeekScheduleResult.ActiveWeek) {
                val mondaySummary = result.days.first { it.dayOfWeek == 1 }
                val sessions = loadSessionsForDay(mondaySummary)
                val allDaySessionIds = loadAllDaySessionIds(result.days)
                val newState = current.copy(
                    weekNumber = result.weekNumber,
                    weekFocus = result.weekFocus,
                    days = result.days,
                    selectedDayOfWeek = 1,
                    selectedDaySessions = sessions,
                    selectedDaySummary = mondaySummary,
                    allDaySessionIds = allDaySessionIds
                )
                _uiState.value = updateCompletedIndicators(_finishedKeys.value, newState)
                    .copy(inProgressSessionIds = current.inProgressSessionIds)
            } else if (result is WeekScheduleResult.Error) {
                _uiState.value = TodayUiState.Error(result.message)
            }
        }
    }

    /**
     * Recomputes which sessions and days in the displayed week are completed, given the
     * current [finishedKeys] snapshot and the current [state].
     *
     * **Completion definition:** a finished [WorkoutKey] whose [WorkoutKey.date] falls anywhere
     * within the calendar-week window `[weekMonday, weekSunday]` of the displayed week counts
     * as a completion for the session identified by [WorkoutKey.sessionId]. This means a workout
     * done off its scheduled day (e.g. Monday's session completed on Sunday) is still shown
     * as completed.
     *
     * **Day-chip indicator:** resolved via [TodayUiState.WeekView.allDaySessionIds], which maps
     * each dayOfWeek to the sessionIds scheduled for that day, so the chip highlights the
     * *scheduled* day rather than the calendar day the workout was performed on.
     *
     * @param finishedKeys All finished [WorkoutKey] pairs for the enrollment.
     * @param state The current [TodayUiState.WeekView] to update.
     * @return A new [TodayUiState.WeekView] with updated completion sets.
     */
    private fun updateCompletedIndicators(
        finishedKeys: List<WorkoutKey>,
        state: TodayUiState.WeekView
    ): TodayUiState.WeekView {
        val today = LocalDate.now()
        val todayDayOfWeek = today.dayOfWeek.value

        // Compute the Monday of the displayed week, then derive Sunday.
        val weekMonday = today
            .minusDays((todayDayOfWeek - 1).toLong())
            .plusDays(((state.weekNumber - state.currentWeekNumber) * 7).toLong())
        val weekSunday = weekMonday.plusDays(6)

        // Collect sessionIds finished on ANY date within the displayed week's window.
        val finishedInWeek: Set<Long> = finishedKeys
            .filter { !it.date.isBefore(weekMonday) && !it.date.isAfter(weekSunday) }
            .map { it.sessionId }
            .toSet()

        // Sessions in the selected day that were completed this week (regardless of which date).
        val completedSessionIds: Set<Long> = state.selectedDaySessions
            .filter { it.session.id in finishedInWeek }
            .map { it.session.id }
            .toSet()

        // Day chips: highlight the scheduled day whose sessionIds appear in finishedInWeek.
        val completedDaysOfWeek: Set<Int> = state.allDaySessionIds
            .filter { (_, sessionIds) -> sessionIds.any { it in finishedInWeek } }
            .keys
            .toSet()

        return state.copy(
            completedSessionIds = completedSessionIds,
            completedDaysOfWeek = completedDaysOfWeek
        )
    }
}

sealed interface TodayUiState {
    data object Loading : TodayUiState
    data object NoActiveProgram : TodayUiState

    /**
     * The main state: shows the current week with a selectable day bar.
     */
    data class WeekView(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity,
        val weekNumber: Int,
        val weekFocus: String?,
        val days: List<WeekDaySummary>,
        val todayDayOfWeek: Int,
        val selectedDayOfWeek: Int,
        val selectedDaySessions: List<SessionDetail>,
        val selectedDaySummary: WeekDaySummary,
        val completedDays: Int,
        val totalTrainingDays: Int,
        val currentWeekNumber: Int,
        val totalWeeks: Int,
        /** IDs of sessions in the currently selected day that were fully finished on their date. */
        val completedSessionIds: Set<Long> = emptySet(),
        /** dayOfWeek values (1=Mon..7=Sun) in the displayed week that have ≥1 finished session. */
        val completedDaysOfWeek: Set<Int> = emptySet(),
        /** Session IDs that have been started today but not yet completed. Shows "Continue Workout". */
        val inProgressSessionIds: Set<Long> = emptySet(),
        /**
         * Map of dayOfWeek (1=Mon..7=Sun) → list of sessionIds scheduled for that day this week.
         * Used by completion indicators to highlight the *scheduled* day regardless of which
         * calendar date the workout was actually performed on.
         */
        val allDaySessionIds: Map<Int, List<Long>> = emptyMap()
    ) : TodayUiState {
        /** True when the user is viewing the week that contains today. */
        val isCurrentWeek: Boolean get() = weekNumber == currentWeekNumber
    }

    data class ProgramComplete(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity
    ) : TodayUiState

    data class NotStartedYet(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity,
        val startDate: java.time.LocalDate
    ) : TodayUiState

    data class Error(val message: String) : TodayUiState
}

// End of TodayViewModel.kt — Week-aware ViewModel with day selection, week navigation, reactive completed-workout indicators, and in-progress workout detection for the Today screen.
