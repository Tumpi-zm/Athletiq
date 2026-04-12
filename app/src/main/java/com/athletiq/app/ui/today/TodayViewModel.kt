package com.athletiq.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.domain.model.SessionDetail
import com.athletiq.app.domain.usecase.AbandonProgramResult
import com.athletiq.app.domain.usecase.AbandonProgramUseCase
import com.athletiq.app.domain.usecase.GetTodaySessionUseCase
import com.athletiq.app.domain.usecase.WeekDaySummary
import com.athletiq.app.domain.usecase.WeekScheduleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private fun observeActiveEnrollment() {
        viewModelScope.launch {
            enrollmentRepository.getActiveEnrollment()
                .collect { enrollment ->
                    if (enrollment == null) {
                        _uiState.value = TodayUiState.NoActiveProgram
                        return@collect
                    }
                    resolveWeekSchedule(enrollment)
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
                    totalTrainingDays = totalTrainingDays
                )
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
     */
    fun selectDay(dayOfWeek: Int) {
        val current = _uiState.value as? TodayUiState.WeekView ?: return
        if (dayOfWeek == current.selectedDayOfWeek) return

        val daySummary = current.days.first { it.dayOfWeek == dayOfWeek }

        viewModelScope.launch {
            val sessions = loadSessionsForDay(daySummary)
            _uiState.value = current.copy(
                selectedDayOfWeek = dayOfWeek,
                selectedDaySessions = sessions,
                selectedDaySummary = daySummary
            )
        }
    }

    private suspend fun loadSessionsForDay(day: WeekDaySummary): List<SessionDetail> {
        if (day.isRestDay || day.dayEntity == null) return emptyList()
        return getTodaySessionUseCase.getSessionDetailsForDay(day.dayEntity)
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
        val totalTrainingDays: Int
    ) : TodayUiState

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

// End of TodayViewModel.kt
