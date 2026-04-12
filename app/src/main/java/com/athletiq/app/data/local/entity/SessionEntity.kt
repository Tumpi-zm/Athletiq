package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a training session within a [DayEntity].
 *
 * A single day may contain multiple sessions (e.g., "AM: Strength" and "PM: Conditioning").
 * For the 90-Day program, each training day has exactly one session, but the model supports
 * multiple sessions per day for future flexibility (e.g., two-a-day programming).
 *
 * Sessions are ordered by [orderIndex] and contain one or more [BlockEntity] entries.
 *
 * @see DayEntity for the parent day
 * @see BlockEntity for workout blocks within this session
 */
@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dayId")]
)
data class SessionEntity(
    /**
     * Auto-generated unique identifier for this session.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the parent [DayEntity.id].
     */
    val dayId: Long,

    /**
     * Display name for the session (e.g., "Functional Power", "Hyrox Conditioning").
     */
    val name: String,

    /**
     * Sort order for sessions within the same day. Lower values appear first.
     * Allows deterministic ordering for multi-session days.
     */
    val orderIndex: Int
)

// End of SessionEntity.kt — Room entity for a training session within a day.
