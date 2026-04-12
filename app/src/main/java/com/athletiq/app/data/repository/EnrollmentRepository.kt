package com.athletiq.app.data.repository

import com.athletiq.app.data.local.dao.EnrollmentDao
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.domain.model.EnrollmentStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for user enrollment management and program lifecycle operations.
 *
 * Orchestrates enrollment CRUD and status transitions through [EnrollmentDao].
 * The single-active-enrollment constraint is enforced at the use-case layer
 * (see [StartProgramUseCase] and [AbandonProgramUseCase]).
 *
 * @see EnrollmentDao for the underlying database queries
 * @see com.athletiq.app.domain.usecase.StartProgramUseCase for enrollment creation
 * @see com.athletiq.app.domain.usecase.AbandonProgramUseCase for status transitions
 */
@Singleton
class EnrollmentRepository @Inject constructor(
    private val enrollmentDao: EnrollmentDao
) {

    /**
     * Returns a reactive stream of the currently active enrollment, or null.
     * The UI layer observes this to decide between showing the catalog or today's session.
     *
     * @return [Flow] emitting the active enrollment or null.
     */
    fun getActiveEnrollment(): Flow<EnrollmentEntity?> = enrollmentDao.getActiveEnrollment()

    /**
     * One-shot query for the active enrollment. Used by use cases that need
     * a synchronous check (e.g., "is there already an active program?").
     *
     * @return The active enrollment, or null.
     */
    suspend fun getActiveEnrollmentOnce(): EnrollmentEntity? =
        enrollmentDao.getActiveEnrollmentOnce()

    /**
     * Returns all enrollments for the "My Programs" screen.
     *
     * @return [Flow] emitting all enrollments sorted by most recently updated.
     */
    fun getAllEnrollments(): Flow<List<EnrollmentEntity>> = enrollmentDao.getAllEnrollments()

    /**
     * Returns a specific enrollment by ID.
     *
     * @param enrollmentId The unique identifier.
     * @return The enrollment, or null.
     */
    suspend fun getEnrollmentById(enrollmentId: Long): EnrollmentEntity? =
        enrollmentDao.getEnrollmentById(enrollmentId)

    /**
     * Creates a new enrollment for a program. The enrollment starts as ACTIVE.
     *
     * @param programId The program to enroll in.
     * @param startDate The date the program starts (typically today).
     * @return The auto-generated enrollment ID.
     */
    suspend fun createEnrollment(programId: Long, startDate: LocalDate): Long {
        val enrollment = EnrollmentEntity(
            programId = programId,
            startDate = startDate,
            status = EnrollmentStatus.ACTIVE.name,
            statusChangedDate = startDate,
            completedDays = 0
        )
        return enrollmentDao.insertEnrollment(enrollment)
    }

    /**
     * Abandons the currently active enrollment by updating its status to ABANDONED.
     * Uses a direct query for atomicity.
     *
     * @param date The date of abandonment.
     */
    suspend fun abandonActiveEnrollment(date: LocalDate) {
        enrollmentDao.abandonActiveEnrollment(date)
    }

    /**
     * Marks an enrollment as completed (all training days finished).
     *
     * @param enrollmentId The enrollment to complete.
     * @param date The completion date.
     */
    suspend fun completeEnrollment(enrollmentId: Long, date: LocalDate) {
        val enrollment = enrollmentDao.getEnrollmentById(enrollmentId) ?: return
        enrollmentDao.updateEnrollment(
            enrollment.copy(
                status = EnrollmentStatus.COMPLETED.name,
                statusChangedDate = date
            )
        )
    }

    /**
     * Increments the completed days counter for an enrollment.
     * Called after a user finishes all exercises in a training day.
     *
     * @param enrollmentId The enrollment to update.
     */
    suspend fun incrementCompletedDays(enrollmentId: Long) {
        enrollmentDao.incrementCompletedDays(enrollmentId)
    }

    suspend fun deleteEnrollment(enrollmentId: Long) {
        enrollmentDao.deleteEnrollment(enrollmentId)
    }
}

// End of EnrollmentRepository.kt — Repository for enrollment lifecycle management.
