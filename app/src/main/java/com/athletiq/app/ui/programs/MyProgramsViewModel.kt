package com.athletiq.app.ui.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the "My Programs" screen.
 *
 * **Screen:** [MyProgramsScreen]
 *
 * **State managed:**
 * - [uiState]: All enrollments enriched with program names and status info.
 *
 * **User actions handled:**
 * - View list of all past and current program enrollments with status.
 */
@HiltViewModel
class MyProgramsViewModel @Inject constructor(
    private val enrollmentRepository: EnrollmentRepository,
    private val programRepository: ProgramRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyProgramsUiState>(MyProgramsUiState.Loading)
    val uiState: StateFlow<MyProgramsUiState> = _uiState.asStateFlow()

    init {
        loadEnrollments()
    }

    private fun loadEnrollments() {
        viewModelScope.launch {
            enrollmentRepository.getAllEnrollments().collect { enrollments ->
                if (enrollments.isEmpty()) {
                    _uiState.value = MyProgramsUiState.Empty
                } else {
                    val enriched = enrollments.mapNotNull { enrollment ->
                        val program = programRepository.getProgramById(enrollment.programId)
                        program?.let {
                            val totalDays = programRepository.getTotalTrainingDays(it.id)
                            EnrollmentWithProgram(
                                enrollment = enrollment,
                                program = it,
                                totalTrainingDays = totalDays
                            )
                        }
                    }
                    _uiState.value = MyProgramsUiState.Success(enriched)
                }
            }
        }
    }

    fun deleteEnrollment(enrollmentId: Long) {
        viewModelScope.launch {
            enrollmentRepository.deleteEnrollment(enrollmentId)
        }
    }
}

/**
 * An enrollment enriched with its program info for display.
 */
data class EnrollmentWithProgram(
    val enrollment: EnrollmentEntity,
    val program: ProgramEntity,
    val totalTrainingDays: Int
)

/**
 * Sealed UI state for the My Programs screen.
 */
sealed interface MyProgramsUiState {
    data object Loading : MyProgramsUiState
    data object Empty : MyProgramsUiState
    data class Success(val enrollments: List<EnrollmentWithProgram>) : MyProgramsUiState
}

// End of MyProgramsViewModel.kt — ViewModel for enrollment history display.
