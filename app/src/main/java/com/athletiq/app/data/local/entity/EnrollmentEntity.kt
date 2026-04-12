package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Represents a user's enrollment in a [ProgramEntity].
 *
 * Enrollments track the relationship between a user and a program over time, including
 * when they started, their current status, and how many training days they've completed.
 *
 * **Lifecycle rules:**
 * - Only one enrollment may have [status] = [ACTIVE] at any time. This is enforced
 *   in the use-case layer ([StartProgramUseCase]) rather than at the database level.
 * - When a user abandons a program, the status transitions to [ABANDONED] and all
 *   associated [WorkoutLogEntity] records are preserved for historical reference.
 * - A program is automatically marked [COMPLETED] when all training days are finished.
 *
 * @see ProgramEntity for the program being enrolled in
 * @see WorkoutLogEntity for workout data logged during this enrollment
 */
@Entity(
    tableName = "enrollments",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("programId")]
)
data class EnrollmentEntity(
    /**
     * Auto-generated unique identifier for this enrollment.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the enrolled [ProgramEntity.id].
     */
    val programId: Long,

    /**
     * The date the user started this program. Combined with the current date to calculate
     * which week and day the user is on. Stored as epoch day via [Converters].
     */
    val startDate: LocalDate,

    /**
     * Current status of this enrollment. Stored as a string representation of the enum:
     * - "ACTIVE" — Program is currently in progress (at most one at a time).
     * - "COMPLETED" — All training days have been finished.
     * - "ABANDONED" — User chose to stop before completing.
     *
     * @see com.athletiq.app.domain.model.EnrollmentStatus
     */
    val status: String,

    /**
     * The date when [status] was last updated. Allows displaying "Abandoned on Mar 15"
     * or "Completed on Jun 30" in the My Programs section.
     */
    val statusChangedDate: LocalDate,

    /**
     * Running count of training days the user has fully completed.
     * Incremented when all exercises in a day's session(s) are logged.
     * Used for progress display (e.g., "Completed 34 of 90 days").
     */
    val completedDays: Int = 0
)

// End of EnrollmentEntity.kt — Room entity tracking user enrollment and program lifecycle.
