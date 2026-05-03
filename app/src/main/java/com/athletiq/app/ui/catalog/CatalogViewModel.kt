package com.athletiq.app.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.domain.model.ProgramOverview
import com.athletiq.app.domain.usecase.StartProgramResult
import com.athletiq.app.domain.usecase.StartProgramUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the program catalog (browse & select) screen.
 *
 * **Screen:** [CatalogScreen]
 *
 * **State managed:**
 * - [uiState]: The list of available programs with enrollment info, or loading/error states.
 *
 * **User actions handled:**
 * - Browse available programs.
 * - Start a program (triggers enrollment creation via [StartProgramUseCase]).
 */
@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val startProgramUseCase: StartProgramUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)

    /** The current UI state, observed by CatalogScreen. */
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val _startProgramEvent = MutableStateFlow<StartProgramResult?>(null)

    /** One-shot event for the result of starting a program. */
    val startProgramEvent: StateFlow<StartProgramResult?> = _startProgramEvent.asStateFlow()

    init {
        loadPrograms()
    }

    /**
     * Loads all programs and the active enrollment, combining them reactively so the
     * active-program banner updates automatically when enrollment state changes.
     */
    private fun loadPrograms() {
        viewModelScope.launch {
            try {
                combine(
                    programRepository.getAllPrograms(),
                    enrollmentRepository.getActiveEnrollment()
                ) { programs, activeEnrollment ->
                    programs to activeEnrollment
                }.collect { (programs, activeEnrollment) ->
                    val overviews = programs.map { program ->
                        val totalDays = programRepository.getTotalTrainingDays(program.id)
                        ProgramOverview(program = program, totalTrainingDays = totalDays)
                    }
                    val activeProgram = activeEnrollment?.let { enrollment ->
                        val programOverview = overviews.firstOrNull { it.program.id == enrollment.programId }
                        programOverview?.let {
                            ActiveProgramInfo(
                                enrollmentId = enrollment.id,
                                programName = it.program.name,
                                completedDays = enrollment.completedDays,
                                totalTrainingDays = it.totalTrainingDays
                            )
                        }
                    }
                    _uiState.value = CatalogUiState.Success(
                        programs = overviews,
                        activeProgram = activeProgram
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CatalogUiState.Error(
                    message = e.message ?: "Failed to load programs"
                )
            }
        }
    }

    /**
     * Attempts to start a new program. If another program is active, emits
     * [StartProgramResult.ActiveProgramExists] so the UI can prompt abandonment.
     *
     * @param programId The program to start.
     */
    fun startProgram(programId: Long) {
        viewModelScope.launch {
            val result = startProgramUseCase(programId)
            _startProgramEvent.value = result
        }
    }

    /** Clears the start program event after the UI has consumed it. */
    fun clearStartProgramEvent() {
        _startProgramEvent.value = null
    }
}

/**
 * Sealed UI state for the catalog screen.
 */
sealed interface CatalogUiState {
    /** Programs are being loaded from the database. */
    data object Loading : CatalogUiState

    /**
     * Programs loaded successfully.
     * @property programs The list of programs with enriched metadata.
     * @property activeProgram The currently active enrollment summary, or null if none.
     */
    data class Success(
        val programs: List<ProgramOverview>,
        val activeProgram: ActiveProgramInfo? = null
    ) : CatalogUiState

    /**
     * An error occurred while loading.
     * @property message Human-readable error description.
     */
    data class Error(val message: String) : CatalogUiState
}

/**
 * Summary of the currently active enrollment for display in the catalog banner.
 *
 * @property enrollmentId The active enrollment ID.
 * @property programName The name of the active program.
 * @property completedDays Number of training days completed so far.
 * @property totalTrainingDays Total training days in the program.
 */
data class ActiveProgramInfo(
    val enrollmentId: Long,
    val programName: String,
    val completedDays: Int,
    val totalTrainingDays: Int
)

// End of CatalogViewModel.kt — ViewModel for program catalog with active program banner and enrollment initiation.
