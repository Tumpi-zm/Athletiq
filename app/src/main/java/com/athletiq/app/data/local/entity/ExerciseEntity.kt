package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single exercise within a [BlockEntity].
 *
 * This is the most granular unit of prescribed work. Each exercise defines what the athlete
 * should do (name, sets, reps) along with coaching metadata (tempo, RPE, notes) that the
 * workout screen displays to guide execution.
 *
 * The [name] field is the primary key for cross-program exercise history lookups.
 * When a user logs a set for "Back Squat" in any program, all historical data for that
 * exercise name is queryable via [ExerciseLogEntity.exerciseName].
 *
 * @see BlockEntity for the parent block
 * @see ExerciseLogEntity for logged performance data against this exercise
 */
@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = BlockEntity::class,
            parentColumns = ["id"],
            childColumns = ["blockId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("blockId")]
)
data class ExerciseEntity(
    /**
     * Auto-generated unique identifier for this exercise instance.
     * Note: The same exercise (e.g., "Back Squat") can appear in multiple blocks/weeks
     * with different set/rep schemes, each getting a unique ID.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the parent [BlockEntity.id].
     */
    val blockId: Long,

    /**
     * Exercise name as it should appear in the UI and be used for history lookups
     * (e.g., "Back Squat", "DB Bench Press", "KB Swings").
     * Must be consistent across the program for accurate history tracking.
     */
    val name: String,

    /**
     * Comma-separated target muscle groups (e.g., "Quads, Glutes, Core").
     * Displayed in the workout screen for context. Nullable for conditioning exercises
     * where specific muscle targeting is less relevant.
     */
    val targetMuscles: String? = null,

    /**
     * Number of prescribed sets. For exercises in AMRAP/EMOM blocks, this represents
     * the target work sets within each round/interval.
     */
    val sets: Int,

    /**
     * Flexible rep prescription as a string to accommodate varied formats:
     * - Fixed: "8", "10"
     * - Range: "8-10"
     * - Max effort: "Max"
     * - Time-based: "60 sec", "30 sec"
     * - Distance: "400m", "1km"
     * - Calorie: "15 cal"
     */
    val reps: String,

    /**
     * Rest duration in seconds between sets of this exercise.
     * Overrides the user's default rest timer setting for this specific exercise.
     * The workout screen starts countdown automatically after each completed set.
     */
    val restSeconds: Int,

    /**
     * Prescribed tempo in eccentric-pause-concentric-pause format (e.g., "3-1-1-0").
     * Null when tempo is not specified (athlete chooses their own cadence).
     */
    val tempo: String? = null,

    /**
     * Rate of Perceived Exertion target on a 1–10 scale.
     * Guides the athlete's intensity selection. Null when RPE is not specified.
     */
    val rpe: Int? = null,

    /**
     * Free-text coaching notes displayed below the exercise
     * (e.g., "Use fat grips", "Touch-and-go reps", "Light weight, focus on depth").
     */
    val notes: String? = null,

    /**
     * Detailed exercise description with form cues and technique guidance.
     * Shown on the workout screen to help the athlete execute with proper form.
     */
    val description: String? = null,

    /**
     * Sort order within the parent block. Determines exercise sequence during workout execution.
     */
    val orderIndex: Int
)

// End of ExerciseEntity.kt — Room entity for individual exercises with full coaching metadata.
