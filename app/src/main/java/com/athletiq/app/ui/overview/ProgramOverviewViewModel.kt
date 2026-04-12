package com.athletiq.app.ui.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.local.entity.SessionEntity
import com.athletiq.app.data.local.entity.WeekEntity
import com.athletiq.app.data.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramOverviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val programRepository: ProgramRepository
) : ViewModel() {

    private val programId: Long = savedStateHandle["programId"] ?: -1L

    private val _uiState = MutableStateFlow<ProgramOverviewUiState>(ProgramOverviewUiState.Loading)
    val uiState: StateFlow<ProgramOverviewUiState> = _uiState.asStateFlow()

    init {
        loadProgram()
    }

    private fun loadProgram() {
        viewModelScope.launch {
            try {
                val program = programRepository.getProgramById(programId)
                if (program == null) {
                    _uiState.value = ProgramOverviewUiState.Error("Program not found")
                    return@launch
                }

                val weeks = programRepository.getWeeksByProgram(programId)
                val weekDetails = weeks.map { week ->
                    val days = programRepository.getDaysByWeek(week.id)
                    val dayDetails = days.map { day ->
                        val sessions = if (!day.isRestDay) {
                            programRepository.getSessionsByDay(day.id)
                        } else emptyList()
                        DayOverview(day = day, sessions = sessions)
                    }
                    WeekOverview(week = week, days = dayDetails)
                }

                // Group weeks into months (4 weeks per month).
                val months = weekDetails.chunked(4).mapIndexed { index, monthWeeks ->
                    val focus = monthWeeks.firstOrNull()?.week?.focus ?: "Month ${index + 1}"
                    MonthOverview(
                        monthNumber = index + 1,
                        focus = focus,
                        weeks = monthWeeks
                    )
                }

                val totalDays = programRepository.getTotalTrainingDays(programId)

                _uiState.value = ProgramOverviewUiState.Success(
                    program = program,
                    months = months,
                    allWeeks = weekDetails,
                    totalTrainingDays = totalDays
                )
            } catch (e: Exception) {
                _uiState.value = ProgramOverviewUiState.Error(
                    e.message ?: "Failed to load program"
                )
            }
        }
    }
}

sealed interface ProgramOverviewUiState {
    data object Loading : ProgramOverviewUiState
    data class Success(
        val program: ProgramEntity,
        val months: List<MonthOverview>,
        val allWeeks: List<WeekOverview>,
        val totalTrainingDays: Int
    ) : ProgramOverviewUiState
    data class Error(val message: String) : ProgramOverviewUiState
}

data class MonthOverview(
    val monthNumber: Int,
    val focus: String,
    val weeks: List<WeekOverview>
)

data class WeekOverview(
    val week: WeekEntity,
    val days: List<DayOverview>
)

data class DayOverview(
    val day: DayEntity,
    val sessions: List<SessionEntity>
)
