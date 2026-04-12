package com.athletiq.app.domain.usecase

import com.athletiq.app.data.repository.EnrollmentRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Handles abandoning the currently active program enrollment.
 *
 * **Business rules:**
 * 1. Only an ACTIVE enrollment can be abandoned.
 * 2. Abandonment is irreversible — the enrollment transitions to ABANDONED permanently.
 * 3. All workout log data is preserved (cascade delete is NOT triggered).
 * 4. After abandonment, the user is free to start a new program.
 *
 * The UI layer is responsible for showing a confirmation dialog before calling this use case.
 *
 * @see StartProgramUseCase for the companion enrollment creation operation
 * @see com.athletiq.app.data.repository.EnrollmentRepository for persistence
 */
class AbandonProgramUseCase @Inject constructor(
    private val enrollmentRepository: EnrollmentRepository
) {

    /**
     * Abandons the currently active enrollment.
     *
     * @param date The date of abandonment (defaults to today).
     * @return [AbandonProgramResult] indicating the outcome.
     */
    suspend operator fun invoke(
        date: LocalDate = LocalDate.now()
    ): AbandonProgramResult {
        val activeEnrollment = enrollmentRepository.getActiveEnrollmentOnce()
            ?: return AbandonProgramResult.NoActiveProgram

        enrollmentRepository.abandonActiveEnrollment(date)

        return AbandonProgramResult.Success(
            abandonedEnrollmentId = activeEnrollment.id,
            completedDays = activeEnrollment.completedDays
        )
    }
}

/**
 * Sealed result type for [AbandonProgramUseCase].
 */
sealed interface AbandonProgramResult {

    /**
     * The active enrollment was successfully abandoned.
     *
     * @property abandonedEnrollmentId The ID of the now-abandoned enrollment.
     * @property completedDays How many training days were completed before abandonment.
     */
    data class Success(
        val abandonedEnrollmentId: Long,
        val completedDays: Int
    ) : AbandonProgramResult

    /** No active enrollment exists to abandon. */
    data object NoActiveProgram : AbandonProgramResult
}

// End of AbandonProgramUseCase.kt — Active program abandonment with data preservation.
