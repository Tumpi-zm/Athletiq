package com.athletiq.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.athletiq.app.data.local.entity.EnrollmentEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for user enrollment management and program lifecycle operations.
 *
 * Handles the full enrollment lifecycle: creating new enrollments, querying the active
 * enrollment, transitioning statuses (Active → Abandoned/Completed), and listing
 * historical enrollments for the "My Programs" screen.
 *
 * **Single-active constraint:** At most one enrollment may be "ACTIVE" at any time.
 * This DAO provides [getActiveEnrollment] to check the current state, but the constraint
 * is enforced at the use-case layer, not via database constraints.
 *
 * @see com.athletiq.app.data.repository.EnrollmentRepository for the repository above this DAO
 * @see com.athletiq.app.domain.usecase.StartProgramUseCase for enrollment creation logic
 * @see com.athletiq.app.domain.usecase.AbandonProgramUseCase for abandonment logic
 */
@Dao
interface EnrollmentDao {

    // ── Active Enrollment Queries ──────────────────────────────────────────────

    /**
     * Returns the currently active enrollment, if any.
     * Emits null when no program is active (user is on the catalog screen).
     *
     * @return Reactive [Flow] of the active enrollment or null.
     */
    @Query("SELECT * FROM enrollments WHERE status = 'ACTIVE' LIMIT 1")
    fun getActiveEnrollment(): Flow<EnrollmentEntity?>

    /**
     * Suspending variant — returns the active enrollment without Flow wrapping.
     * Used by use cases that need a one-shot check (e.g., before starting a new program).
     *
     * @return The active enrollment, or null if none exists.
     */
    @Query("SELECT * FROM enrollments WHERE status = 'ACTIVE' LIMIT 1")
    suspend fun getActiveEnrollmentOnce(): EnrollmentEntity?

    // ── Enrollment History ─────────────────────────────────────────────────────

    /**
     * Returns all enrollments ordered by status change date (most recent first).
     * Used by the "My Programs" screen to show Active, Completed, and Abandoned programs.
     *
     * @return Reactive [Flow] of all enrollments.
     */
    @Query("SELECT * FROM enrollments ORDER BY statusChangedDate DESC")
    fun getAllEnrollments(): Flow<List<EnrollmentEntity>>

    /**
     * Returns a single enrollment by ID.
     *
     * @param enrollmentId The unique identifier.
     * @return The enrollment entity, or null if not found.
     */
    @Query("SELECT * FROM enrollments WHERE id = :enrollmentId")
    suspend fun getEnrollmentById(enrollmentId: Long): EnrollmentEntity?

    /**
     * Returns all enrollments for a specific program (a user might enroll in the same
     * program multiple times after abandoning/completing it).
     *
     * @param programId The program to find enrollments for.
     * @return All enrollments for the given program.
     */
    @Query("SELECT * FROM enrollments WHERE programId = :programId ORDER BY startDate DESC")
    suspend fun getEnrollmentsByProgram(programId: Long): List<EnrollmentEntity>

    // ── Write Operations ───────────────────────────────────────────────────────

    /**
     * Creates a new enrollment record.
     *
     * @param enrollment The enrollment to insert. Must have status = "ACTIVE".
     * @return The auto-generated enrollment ID.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEnrollment(enrollment: EnrollmentEntity): Long

    /**
     * Updates an existing enrollment (used for status transitions and completed day increments).
     *
     * @param enrollment The updated enrollment entity.
     */
    @Update
    suspend fun updateEnrollment(enrollment: EnrollmentEntity)

    /**
     * Transitions the active enrollment to Abandoned status.
     * Performed as a direct query for atomicity — avoids read-modify-write race conditions.
     *
     * @param date The date of abandonment (stored as epoch day via TypeConverter).
     */
    @Query("""
        UPDATE enrollments 
        SET status = 'ABANDONED', statusChangedDate = :date 
        WHERE status = 'ACTIVE'
    """)
    suspend fun abandonActiveEnrollment(date: LocalDate)

    /**
     * Increments the completed days counter for a given enrollment.
     *
     * @param enrollmentId The enrollment to update.
     */
    @Query("UPDATE enrollments SET completedDays = completedDays + 1 WHERE id = :enrollmentId")
    suspend fun incrementCompletedDays(enrollmentId: Long)

    @Query("DELETE FROM enrollments WHERE id = :enrollmentId")
    suspend fun deleteEnrollment(enrollmentId: Long)
}

// End of EnrollmentDao.kt — DAO for enrollment lifecycle management and history queries.
