package com.athletiq.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.domain.usecase.AbandonProgramResult
import com.athletiq.app.domain.usecase.AbandonProgramUseCase
import com.athletiq.app.domain.usecase.GetTodaySessionUseCase
import com.athletiq.app.domain.usecase.TodaySessionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Today screen — the home screen when a program is active.
 *
 * **Screen:** [TodayScreen]
 *
 * **State managed:**
 * - [uiState]: Today's session info (training day details or rest day), loading, or error.
 * - [abandonEvent]: One-shot event for the result of abandoning the current program.
 *
 * **User actions handled:**
 * - View today's scheduled training session.
 * - Abandon the current program (triggers confirmation in UI, then calls [AbandonProgramUseCase]).
 */
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val enrollmentRepository: EnrollmentRepository,
    private val programRepository: ProgramRepository,
    private val getTodaySessionUseCase: GetTodaySessionUseCase,
    private val abandonProgramUseCase: AbandonProgramUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodayUiState>(TodayUiState.Loading)
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val _abandonEvent = MutableStateFlow<AbandonProgramResult?>(null)
    val abandonEvent: StateFlow<AbandonProgramResult?> = _abandonEvent.asStateFlow()

    init {
        observeActiveEnrollment()
    }

    /**
     * Observes the active enrollment and resolves today's session whenever it changes.
     *
     * When a new enrollment is activated (or the current one is abandoned), this
     * automatically recalculates what the user should train today.
     */
    private fun observeActiveEnrollment() {
        viewModelScope.launch {
            enrollmentRepository.getActiveEnrollment()
                .collect { enrollment ->
                    if (enrollment == null) {
                        _uiState.value = TodayUiState.NoActiveProgram
                        return@collect
                    }
                    resolveTodaySession(enrollment)
                }
        }
    }

    /**
     * Resolves today's session using the GetTodaySessionUseCase.
     *
     * @param enrollment The currently active enrollment.
     */
    private suspend fun resolveTodaySession(enrollment: EnrollmentEntity) {
        val program = programRepository.getProgramById(enrollment.programId)
            ?: run {
                _uiState.value = TodayUiState.Error("Program not found")
                return
            }

        val result = getTodaySessionUseCase(enrollment)
        val totalTrainingDays = programRepository.getTotalTrainingDays(enrollment.programId)

        _uiState.value = when (result) {
            is TodaySessionResult.TrainingDay -> TodayUiState.TrainingDay(
                enrollment = enrollment,
                program = program,
                weekNumber = result.weekNumber,
                weekFocus = result.weekFocus,
                dayName = result.day.name,
                sessions = result.sessions,
                completedDays = enrollment.completedDays,
                totalTrainingDays = totalTrainingDays
            )
            is TodaySessionResult.RestDay -> TodayUiState.RestDay(
                enrollment = enrollment,
                program = program,
                weekNumber = result.weekNumber,
                notes = result.notes,
                completedDays = enrollment.completedDays,
                totalTrainingDays = totalTrainingDays
            )
            is TodaySessionResult.ProgramComplete -> TodayUiState.ProgramComplete(
                enrollment = enrollment,
                program = program
            )
            is TodaySessionResult.NotStartedYet -> TodayUiState.NotStartedYet(
                enrollment = enrollment,
                program = program,
                startDate = result.startDate
            )
            is TodaySessionResult.Error -> TodayUiState.Error(result.message)
        }
    }

    /**
     * Triggers program abandonment. The UI should show a confirmation dialog before calling this.
     */
    fun abandonProgram() {
        viewModelScope.launch {
            val result = abandonProgramUseCase()
            _abandonEvent.value = result
        }
    }

    /** Clears the abandon event after the UI has consumed it. */
    fun clearAbandonEvent() {
        _abandonEvent.value = null
    }
}

/**
 * Sealed UI state for the Today screen.
 */
sealed interface TodayUiState {
    /** Loading enrollment and session data. */
    data object Loading : TodayUiState

    /** No active program — user should be redirected to catalog. */
    data object NoActiveProgram : TodayUiState

    /**
     * Today is a training day with one or more sessions.
     */
    data class TrainingDay(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity,
        val weekNumber: Int,
        val weekFocus: String?,
        val dayName: String,
        val sessions: List<com.athletiq.app.domain.model.SessionDetail>,
        val completedDays: Int,
        val totalTrainingDays: Int
    ) : TodayUiState

    /**
     * Today is a rest day.
     */
    data class RestDay(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity,
        val weekNumber: Int,
        val notes: String?,
        val completedDays: Int,
        val totalTrainingDays: Int
    ) : TodayUiState

    /** The program has been fully completed. */
    data class ProgramComplete(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity
    ) : TodayUiState

    /** The program hasn't started yet. */
    data class NotStartedYet(
        val enrollment: EnrollmentEntity,
        val program: ProgramEntity,
        val startDate: java.time.LocalDate
    ) : TodayUiState

    /** An error occurred. */
    data class Error(val message: String) : TodayUiState
}

// End of TodayViewModel.kt — ViewModel for today's session resolution and program management.
