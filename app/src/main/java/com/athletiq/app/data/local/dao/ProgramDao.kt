package com.athletiq.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.athletiq.app.data.local.entity.BlockEntity
import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.ExerciseEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.local.entity.SessionEntity
import com.athletiq.app.data.local.entity.WeekEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for program template queries.
 *
 * Provides read operations for browsing the program catalog and retrieving full session
 * details for workout execution. Write operations are limited to seed data insertion
 * (programs are immutable once created).
 *
 * All query methods return [Flow] for reactive UI updates via Compose's `collectAsState`.
 *
 * @see com.athletiq.app.data.repository.ProgramRepository for the repository layer above this DAO
 */
@Dao
interface ProgramDao {

    // ── Program Catalog ────────────────────────────────────────────────────────

    /**
     * Returns all available programs for the catalog screen.
     *
     * @return A reactive [Flow] emitting the list of all programs whenever the table changes.
     */
    @Query("SELECT * FROM programs ORDER BY name ASC")
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    /**
     * Returns a single program by its ID.
     *
     * @param programId The unique identifier of the program.
     * @return The program entity, or null if not found.
     */
    @Query("SELECT * FROM programs WHERE id = :programId")
    suspend fun getProgramById(programId: Long): ProgramEntity?

    /**
     * Returns the total number of training days (non-rest days) across all weeks for a program.
     * Used for progress calculation (e.g., "Completed 34 of 90 days").
     *
     * @param programId The program to count training days for.
     * @return Total number of non-rest days across all weeks.
     */
    @Query("""
        SELECT COUNT(*) FROM days d
        INNER JOIN weeks w ON d.weekId = w.id
        WHERE w.programId = :programId AND d.isRestDay = 0
    """)
    suspend fun getTotalTrainingDays(programId: Long): Int

    // ── Week & Day Queries ─────────────────────────────────────────────────────

    /**
     * Returns all weeks for a program, ordered by week number.
     *
     * @param programId The parent program.
     * @return All weeks in natural order.
     */
    @Query("SELECT * FROM weeks WHERE programId = :programId ORDER BY weekNumber ASC")
    suspend fun getWeeksByProgram(programId: Long): List<WeekEntity>

    /**
     * Returns a specific week by program ID and week number.
     *
     * @param programId The parent program.
     * @param weekNumber The 1-indexed week number.
     * @return The week entity, or null if it doesn't exist (e.g., week number out of range).
     */
    @Query("SELECT * FROM weeks WHERE programId = :programId AND weekNumber = :weekNumber")
    suspend fun getWeek(programId: Long, weekNumber: Int): WeekEntity?

    /**
     * Returns all days for a given week, ordered by day of week.
     *
     * @param weekId The parent week's ID.
     * @return Days in Monday–Sunday order.
     */
    @Query("SELECT * FROM days WHERE weekId = :weekId ORDER BY dayOfWeek ASC")
    suspend fun getDaysByWeek(weekId: Long): List<DayEntity>

    /**
     * Returns a specific day by week ID and day of week.
     *
     * @param weekId The parent week's ID.
     * @param dayOfWeek ISO-8601 day of week (1 = Monday, 7 = Sunday).
     * @return The day entity, or null if not defined for that day.
     */
    @Query("SELECT * FROM days WHERE weekId = :weekId AND dayOfWeek = :dayOfWeek")
    suspend fun getDay(weekId: Long, dayOfWeek: Int): DayEntity?

    /**
     * Returns a single day by its ID.
     *
     * @param dayId The unique identifier of the day.
     * @return The day entity, or null if not found.
     */
    @Query("SELECT * FROM days WHERE id = :dayId")
    suspend fun getDayById(dayId: Long): DayEntity?

    // ── Session & Exercise Detail Queries ──────────────────────────────────────

    /**
     * Returns all sessions for a given day, ordered by [SessionEntity.orderIndex].
     *
     * @param dayId The parent day's ID.
     * @return Sessions in their prescribed order.
     */
    @Query("SELECT * FROM sessions WHERE dayId = :dayId ORDER BY orderIndex ASC")
    suspend fun getSessionsByDay(dayId: Long): List<SessionEntity>

    /**
     * Returns all blocks for a given session, ordered by [BlockEntity.orderIndex].
     *
     * @param sessionId The parent session's ID.
     * @return Blocks in their prescribed order.
     */
    @Query("SELECT * FROM blocks WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    suspend fun getBlocksBySession(sessionId: Long): List<BlockEntity>

    /**
     * Returns all exercises for a given block, ordered by [ExerciseEntity.orderIndex].
     *
     * @param blockId The parent block's ID.
     * @return Exercises in their prescribed execution order.
     */
    @Query("SELECT * FROM exercises WHERE blockId = :blockId ORDER BY orderIndex ASC")
    suspend fun getExercisesByBlock(blockId: Long): List<ExerciseEntity>

    // ── Seed Data Insertion ────────────────────────────────────────────────────

    /**
     * Inserts a program and returns its generated ID.
     * Used during seed data population on first launch.
     *
     * @param program The program to insert.
     * @return The auto-generated row ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: ProgramEntity): Long

    /** Inserts a week and returns its generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeek(week: WeekEntity): Long

    /** Inserts a day and returns its generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: DayEntity): Long

    /** Inserts a session and returns its generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    /** Inserts a block and returns its generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlock(block: BlockEntity): Long

    /** Inserts an exercise and returns its generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    /**
     * Checks if any programs exist in the database.
     * Used to determine if seed data needs to be loaded on first launch.
     *
     * @return The count of programs (0 means database needs seeding).
     */
    @Query("SELECT COUNT(*) FROM programs")
    suspend fun getProgramCount(): Int
}

// End of ProgramDao.kt — DAO for program catalog queries and seed data insertion.
