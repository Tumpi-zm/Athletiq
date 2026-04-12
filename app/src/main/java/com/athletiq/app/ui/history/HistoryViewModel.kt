package com.athletiq.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.ExerciseLogEntity
import com.athletiq.app.data.local.entity.WorkoutLogEntity
import com.athletiq.app.data.repository.WorkoutLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the workout history screen.
 *
 * **Screen:** [HistoryScreen]
 *
 * **State managed:**
 * - [uiState]: List of all completed workout logs across all enrollments.
 * - Detail view: exercise logs for a selected workout.
 *
 * **User actions handled:**
 * - Browse all completed workouts.
 * - Tap a workout to view its exercise log details.
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutLogRepository: WorkoutLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _selectedWorkoutLogs = MutableStateFlow<List<ExerciseLogEntity>>(emptyList())
    val selectedWorkoutLogs: StateFlow<List<ExerciseLogEntity>> = _selectedWorkoutLogs.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            workoutLogRepository.getAllWorkoutLogs().collect { logs ->
                _uiState.value = if (logs.isEmpty()) {
                    HistoryUiState.Empty
                } else {
                    HistoryUiState.Success(logs)
                }
            }
        }
    }

    /**
     * Loads exercise logs for a specific workout when the user taps on it.
     *
     * @param workoutLogId The workout to show details for.
     */
    fun loadWorkoutDetail(workoutLogId: Long) {
        viewModelScope.launch {
            val logs = workoutLogRepository.getExerciseLogsByWorkout(workoutLogId)
            _selectedWorkoutLogs.value = logs
        }
    }
}

/**
 * Sealed UI state for the history screen.
 */
sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data object Empty : HistoryUiState
    data class Success(val workoutLogs: List<WorkoutLogEntity>) : HistoryUiState
}

// End of HistoryViewModel.kt — ViewModel for workout history and exercise log details.
