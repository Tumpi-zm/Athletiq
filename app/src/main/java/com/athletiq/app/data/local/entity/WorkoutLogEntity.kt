package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Represents a completed workout session in the user's training log.
 *
 * Each WorkoutLog corresponds to one [SessionEntity] performed on a specific date during
 * a particular [EnrollmentEntity]. It serves as the header record for a group of
 * [ExerciseLogEntity] entries that capture per-set performance data.
 *
 * **Data preservation guarantee:** Workout logs persist regardless of the parent enrollment's
 * status. If a user abandons a program, all logged workout data remains accessible for
 * historical analysis and exercise history lookups.
 *
 * @see EnrollmentEntity for the enrollment this log belongs to
 * @see SessionEntity for the session template that was performed
 * @see ExerciseLogEntity for per-exercise, per-set performance data
 */
@Entity(
    tableName = "workout_logs",
    foreignKeys = [
        ForeignKey(
            entity = EnrollmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["enrollmentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("enrollmentId"), Index("sessionId")]
)
data class WorkoutLogEntity(
    /**
     * Auto-generated unique identifier for this workout log.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the [EnrollmentEntity.id] under which this workout was performed.
     */
    val enrollmentId: Long,

    /**
     * Foreign key referencing the [SessionEntity.id] template that was executed.
     */
    val sessionId: Long,

    /**
     * The calendar date this workout was performed. Stored as epoch day via [Converters].
     */
    val date: LocalDate,

    /**
     * Total session duration in minutes, tracked from "Start Workout" to completion.
     * Null if the user didn't complete the session through the guided flow.
     */
    val durationMinutes: Int? = null,

    /**
     * Index of the current exercise in the flattened exercise list.
     * Used to resume an interrupted workout.
     */
    val currentExerciseIndex: Int = 0,

    /**
     * Current set number (1-indexed) within the current exercise.
     * Used to resume an interrupted workout.
     */
    val currentSetNumber: Int = 1,

    /**
     * Epoch milliseconds when the workout was started.
     * Used to calculate duration when resuming.
     */
    val startTimeMillis: Long = 0L
)

// End of WorkoutLogEntity.kt — Room entity for completed workout session headers.
