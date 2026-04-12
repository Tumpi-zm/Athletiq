package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single day within a [WeekEntity].
 *
 * Each day maps to an ISO-8601 day of the week (1 = Monday, 7 = Sunday).
 * A day can either be a training day (containing one or more [SessionEntity]) or a rest day
 * with optional recovery/mobility suggestions.
 *
 * The app uses the current date and the enrollment start date to determine which DayEntity
 * maps to "today" for the active program.
 *
 * @see WeekEntity for the parent week
 * @see SessionEntity for training sessions within this day
 */
@Entity(
    tableName = "days",
    foreignKeys = [
        ForeignKey(
            entity = WeekEntity::class,
            parentColumns = ["id"],
            childColumns = ["weekId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("weekId")]
)
data class DayEntity(
    /**
     * Auto-generated unique identifier for this day.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the parent [WeekEntity.id].
     */
    val weekId: Long,

    /**
     * ISO-8601 day of week: 1 = Monday, 2 = Tuesday, ..., 7 = Sunday.
     * Matches [java.time.DayOfWeek.getValue] for direct comparison with the current date.
     */
    val dayOfWeek: Int,

    /**
     * Human-readable label for this day (e.g., "Functional Power", "Hyrox Conditioning",
     * "Rest Day", "Active Recovery").
     */
    val name: String,

    /**
     * Whether this day is a rest day with no structured training sessions.
     * When true, [restDayNotes] may contain recovery suggestions.
     */
    val isRestDay: Boolean = false,

    /**
     * Optional recovery or mobility suggestions displayed on rest days
     * (e.g., "Light walking, foam rolling, hydration focus").
     */
    val restDayNotes: String? = null
)

// End of DayEntity.kt — Room entity representing a single training or rest day.
