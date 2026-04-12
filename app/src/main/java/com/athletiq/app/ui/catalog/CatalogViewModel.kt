package com.athletiq.app.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.domain.model.ProgramOverview
import com.athletiq.app.domain.usecase.StartProgramResult
import com.athletiq.app.domain.usecase.StartProgramUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
     * Loads all programs from the repository and enriches them with training day counts.
     */
    private fun loadPrograms() {
        viewModelScope.launch {
            try {
                programRepository.getAllPrograms().collect { programs ->
                    val overviews = programs.map { program ->
                        val totalDays = programRepository.getTotalTrainingDays(program.id)
                        ProgramOverview(
                            program = program,
                            totalTrainingDays = totalDays
                        )
                    }
                    _uiState.value = CatalogUiState.Success(programs = overviews)
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
     */
    data class Success(val programs: List<ProgramOverview>) : CatalogUiState

    /**
     * An error occurred while loading.
     * @property message Human-readable error description.
     */
    data class Error(val message: String) : CatalogUiState
}

// End of CatalogViewModel.kt — ViewModel for program catalog with enrollment initiation.
