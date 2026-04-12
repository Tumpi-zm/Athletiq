package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents the performance data for a single set of a single exercise within a workout.
 *
 * This is the most granular logging entity. Each completed set during a workout generates
 * one ExerciseLog record capturing the actual weight, reps, and optional notes.
 *
 * **Cross-program history:** The [exerciseName] field is intentionally denormalized (duplicated
 * from [ExerciseEntity.name]) to enable efficient cross-program exercise history queries.
 * Querying "all Back Squat sets ever" requires only this table, avoiding expensive joins
 * through Block → Session → Day → Week → Program.
 *
 * **Weight pre-fill:** The workout screen queries the most recent ExerciseLog entry for a
 * given [exerciseName] to pre-populate the weight input field, regardless of which program
 * that previous set was logged in.
 *
 * @see WorkoutLogEntity for the parent workout session
 * @see ExerciseEntity for the prescribed exercise template
 */
@Entity(
    tableName = "exercise_logs",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutLogId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("workoutLogId"),
        Index("exerciseId"),
        Index("exerciseName")
    ]
)
data class ExerciseLogEntity(
    /**
     * Auto-generated unique identifier for this exercise log entry.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the parent [WorkoutLogEntity.id].
     */
    val workoutLogId: Long,

    /**
     * Foreign key referencing the [ExerciseEntity.id] template that was performed.
     */
    val exerciseId: Long,

    /**
     * Denormalized exercise name for efficient cross-program history queries.
     * Must exactly match [ExerciseEntity.name] at the time of logging.
     */
    val exerciseName: String,

    /**
     * 1-indexed set number within this exercise (e.g., 1, 2, 3, 4 for a 4-set exercise).
     */
    val setNumber: Int,

    /**
     * Weight used in kilograms (or converted from lbs via the app's unit setting).
     * Null for bodyweight exercises, conditioning pieces, or exercises where weight
     * doesn't apply (e.g., "Box Breathing").
     */
    val weightKg: Float? = null,

    /**
     * Actual number of reps completed for this set.
     * Null for time-based exercises (use [durationSeconds] instead).
     */
    val repsCompleted: Int? = null,

    /**
     * Duration in seconds for time-based sets (e.g., "60 sec plank", "30 sec hold").
     * Null for rep-based exercises.
     */
    val durationSeconds: Int? = null,

    /**
     * Optional per-set notes captured by the athlete
     * (e.g., "Failed on rep 8", "Felt easy — go heavier next time").
     */
    val notes: String? = null
)

// End of ExerciseLogEntity.kt — Room entity for per-set exercise performance logging.
