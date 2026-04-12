package com.athletiq.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a workout block within a [SessionEntity].
 *
 * Blocks group related exercises together and define how they should be performed:
 * - **STANDARD**: Exercises performed sequentially with rest between sets.
 * - **SUPERSET**: Exercises performed back-to-back before a shared rest.
 * - **CIRCUIT**: Multiple exercises cycled through for a given number of rounds.
 * - **EMOM**: Every Minute On the Minute — perform the prescribed work at the start of each minute.
 * - **AMRAP**: As Many Rounds As Possible within a time cap.
 * - **FOR_TIME**: Complete the prescribed work as fast as possible.
 *
 * In the 90-Day program, blocks map to lettered sections:
 * A = Strength/Skill (STANDARD or SUPERSET), B = WOD (varies), C = Core (STANDARD), D = Recovery (STANDARD).
 *
 * @see SessionEntity for the parent session
 * @see ExerciseEntity for exercises within this block
 */
@Entity(
    tableName = "blocks",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId")]
)
data class BlockEntity(
    /**
     * Auto-generated unique identifier for this block.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key referencing the parent [SessionEntity.id].
     */
    val sessionId: Long,

    /**
     * Display name for the block (e.g., "A. Strength", "B. WOD", "C. Core", "D. Recovery").
     */
    val name: String,

    /**
     * The execution format for this block. Determines how the workout screen renders and
     * times the exercises. Must be one of the values defined in [BlockType].
     *
     * @see com.athletiq.app.domain.model.BlockType
     */
    val blockType: String,

    /**
     * Sort order within the parent session. Lower values appear first.
     */
    val orderIndex: Int,

    /**
     * Duration in seconds for timed blocks (EMOM, AMRAP, FOR_TIME).
     * Null for untimed blocks like STANDARD or SUPERSET.
     */
    val timerSeconds: Int? = null,

    /**
     * Number of rounds for CIRCUIT blocks or round-based WODs.
     * Null when rounds are not applicable.
     */
    val rounds: Int? = null
)

// End of BlockEntity.kt — Room entity representing a workout block with execution format metadata.
