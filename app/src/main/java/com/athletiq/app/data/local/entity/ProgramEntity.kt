package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a training program template in the Athletiq database.
 *
 * A program is the top-level container for a structured training plan (e.g., "90-Day Elite
 * Performance Plan"). It defines the overall duration and focus areas but delegates the actual
 * workout content to its child entities: [WeekEntity] → [DayEntity] → [SessionEntity] →
 * [BlockEntity] → [ExerciseEntity].
 *
 * Programs are seeded on first launch from a JSON asset file and are immutable once created.
 * Users interact with programs through [EnrollmentEntity], which tracks their progress.
 *
 * @see WeekEntity for the week-level breakdown of this program
 * @see EnrollmentEntity for user enrollment and progress tracking
 */
@Entity(tableName = "programs")
data class ProgramEntity(
    /**
     * Auto-generated unique identifier for this program.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Display name shown in the program catalog (e.g., "90-Day Elite Performance Plan").
     */
    val name: String,

    /**
     * Short summary describing the program's training philosophy and goals.
     */
    val description: String,

    /**
     * Total number of weeks in the program. Used to calculate program completion percentage
     * and to determine the valid range of week numbers (1..durationWeeks).
     */
    val durationWeeks: Int,

    /**
     * JSON-serialized list of tags for categorization and filtering
     * (e.g., ["CrossFit", "Hyrox", "Strength"]). Stored as a JSON string via [Converters].
     */
    val tags: List<String>
)

// End of ProgramEntity.kt — Room entity for the top-level training program template.
