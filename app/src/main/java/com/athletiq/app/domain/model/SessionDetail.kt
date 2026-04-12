package com.athletiq.app.domain.model

import com.athletiq.app.data.local.entity.BlockEntity
import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.ExerciseEntity
import com.athletiq.app.data.local.entity.SessionEntity

/**
 * Rich domain model representing a complete session ready for workout execution.
 *
 * This is the primary model consumed by the workout screen. It aggregates data from
 * multiple Room entities into a single hierarchical structure:
 * Session → Blocks → Exercises.
 *
 * Built by [ProgramRepository.getSessionDetail] by joining Session, Block, and Exercise tables.
 *
 * @see com.athletiq.app.data.repository.ProgramRepository
 */
data class SessionDetail(
    /** The session entity with basic session info. */
    val session: SessionEntity,

    /** The day entity this session belongs to, used for display context. */
    val day: DayEntity,

    /** Ordered list of blocks with their exercises. */
    val blocks: List<BlockWithExercises>
)

/**
 * A workout block paired with its ordered exercises.
 *
 * Represents one section of a session (e.g., "A. Strength" with its exercises).
 */
data class BlockWithExercises(
    /** The block's metadata (name, type, timer, rounds). */
    val block: BlockEntity,

    /** Exercises within this block, ordered by [ExerciseEntity.orderIndex]. */
    val exercises: List<ExerciseEntity>
)

// End of SessionDetail.kt — Domain models for session detail hierarchy used by workout screen.
