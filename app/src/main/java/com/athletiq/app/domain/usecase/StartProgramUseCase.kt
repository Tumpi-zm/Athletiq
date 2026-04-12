package com.athletiq.app.domain.usecase

import com.athletiq.app.data.repository.EnrollmentRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Handles the creation of a new program enrollment with single-active constraint enforcement.
 *
 * **Business rules:**
 * 1. Only one program can be active at a time.
 * 2. If an active enrollment exists, the caller must abandon it before starting a new one.
 *    This use case does NOT auto-abandon — it returns [StartProgramResult.ActiveProgramExists]
 *    so the UI can prompt the user with a confirmation dialog.
 * 3. New enrollments always start as ACTIVE with today's date as the start date.
 *
 * @see AbandonProgramUseCase for the companion abandonment operation
 * @see com.athletiq.app.data.repository.EnrollmentRepository for persistence
 */
class StartProgramUseCase @Inject constructor(
    private val enrollmentRepository: EnrollmentRepository
) {

    /**
     * Attempts to start a new program enrollment.
     *
     * @param programId The program to enroll in.
     * @param startDate The enrollment start date (defaults to today).
     * @return [StartProgramResult] indicating success or the reason for failure.
     */
    suspend operator fun invoke(
        programId: Long,
        startDate: LocalDate = LocalDate.now()
    ): StartProgramResult {
        // Check the single-active constraint before creating a new enrollment.
        val activeEnrollment = enrollmentRepository.getActiveEnrollmentOnce()

        if (activeEnrollment != null) {
            return StartProgramResult.ActiveProgramExists(
                existingEnrollmentId = activeEnrollment.id,
                existingProgramId = activeEnrollment.programId
            )
        }

        val enrollmentId = enrollmentRepository.createEnrollment(programId, startDate)
        return StartProgramResult.Success(enrollmentId = enrollmentId)
    }
}

/**
 * Sealed result type for [StartProgramUseCase].
 */
sealed interface StartProgramResult {

    /**
     * Enrollment was created successfully.
     *
     * @property enrollmentId The auto-generated enrollment ID.
     */
    data class Success(val enrollmentId: Long) : StartProgramResult

    /**
     * Another program is already active. The UI should prompt the user to abandon it first.
     *
     * @property existingEnrollmentId The active enrollment's ID.
     * @property existingProgramId The active program's ID.
     */
    data class ActiveProgramExists(
        val existingEnrollmentId: Long,
        val existingProgramId: Long
    ) : StartProgramResult
}

// End of StartProgramUseCase.kt — Program enrollment with single-active constraint.
