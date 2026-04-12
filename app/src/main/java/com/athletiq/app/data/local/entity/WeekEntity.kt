package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single week within a [ProgramEntity].
 *
 * Weeks are numbered sequentially (1..N) and may carry an optional focus label describing
 * the mesocycle theme (e.g., "Foundation & Capacity", "Maximal Strength", "Deload").
 *
 * Each week contains up to 7 [DayEntity] entries — one per day of the week.
 *
 * @see ProgramEntity for the parent program
 * @see DayEntity for daily breakdowns within this week
 */
@Entity(
    tableName = "weeks",
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
data class WeekEntity(
    /**
     * Auto-generated unique identifier for this week.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the parent [ProgramEntity.id].
     */
    val programId: Long,

    /**
     * 1-indexed week number within the program (e.g., 1 for the first week).
     * Used with enrollment start date to determine which week the user is currently on.
     */
    val weekNumber: Int,

    /**
     * Optional mesocycle theme or focus for this week
     * (e.g., "Foundation & Capacity", "Deload Week").
     */
    val focus: String? = null
)

// End of WeekEntity.kt — Room entity representing a week within a training program.
