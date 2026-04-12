package com.athletiq.app.data.repository

import com.athletiq.app.data.local.dao.ProgramDao
import com.athletiq.app.data.local.entity.BlockEntity
import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.ExerciseEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.local.entity.SessionEntity
import com.athletiq.app.data.local.entity.WeekEntity
import com.athletiq.app.domain.model.BlockWithExercises
import com.athletiq.app.domain.model.SessionDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for program template data — the immutable workout program content.
 *
 * Provides access to the program catalog (for browsing), individual program structures
 * (for day/session resolution), and fully hydrated session details (for workout execution).
 *
 * This repository wraps [ProgramDao] and adds higher-level operations that join multiple
 * entity types into domain models (e.g., [SessionDetail]).
 *
 * All data is read-only after the initial seed; write methods exist solely for seeding.
 *
 * @see ProgramDao for the underlying database queries
 * @see SessionDetail for the rich domain model built by [getSessionDetail]
 */
@Singleton
class ProgramRepository @Inject constructor(
    private val programDao: ProgramDao
) {

    // ── Catalog Queries ────────────────────────────────────────────────────────

    /**
     * Returns a reactive stream of all available programs for the catalog screen.
     *
     * @return [Flow] emitting the full program list whenever it changes.
     */
    fun getAllPrograms(): Flow<List<ProgramEntity>> = programDao.getAllPrograms()

    /**
     * Returns a single program by ID.
     *
     * @param programId The program's unique identifier.
     * @return The program, or null if not found.
     */
    suspend fun getProgramById(programId: Long): ProgramEntity? =
        programDao.getProgramById(programId)

    /**
     * Returns the total number of non-rest training days in a program.
     *
     * @param programId The program to count days for.
     * @return Total training days (e.g., 90 for a 12-week, 3x/week program with Sunday sessions).
     */
    suspend fun getTotalTrainingDays(programId: Long): Int =
        programDao.getTotalTrainingDays(programId)

    // ── Schedule Resolution ────────────────────────────────────────────────────

    /**
     * Finds the specific week entity for a given program and week number.
     *
     * @param programId The parent program.
     * @param weekNumber 1-indexed week number.
     * @return The week entity, or null if out of range.
     */
    suspend fun getWeek(programId: Long, weekNumber: Int): WeekEntity? =
        programDao.getWeek(programId, weekNumber)

    /**
     * Finds the specific day entity for a week and ISO day-of-week.
     *
     * @param weekId The parent week's ID.
     * @param dayOfWeek ISO-8601 day (1=Monday, 7=Sunday).
     * @return The day entity, or null if that day isn't defined.
     */
    suspend fun getDay(weekId: Long, dayOfWeek: Int): DayEntity? =
        programDao.getDay(weekId, dayOfWeek)

    suspend fun getDayById(dayId: Long): DayEntity? =
        programDao.getDayById(dayId)

    /**
     * Returns all days for a week, used by the weekly schedule view.
     *
     * @param weekId The parent week's ID.
     * @return All days in Monday→Sunday order.
     */
    suspend fun getDaysByWeek(weekId: Long): List<DayEntity> =
        programDao.getDaysByWeek(weekId)

    /**
     * Returns all weeks for a program.
     *
     * @param programId The parent program.
     * @return All weeks in ascending order.
     */
    suspend fun getWeeksByProgram(programId: Long): List<WeekEntity> =
        programDao.getWeeksByProgram(programId)

    // ── Session Detail ─────────────────────────────────────────────────────────

    /**
     * Returns all sessions for a day.
     *
     * @param dayId The parent day's ID.
     * @return Sessions in prescribed order.
     */
    suspend fun getSessionsByDay(dayId: Long): List<SessionEntity> =
        programDao.getSessionsByDay(dayId)

    /**
     * Builds a fully hydrated [SessionDetail] for workout execution.
     *
     * This is the key method for the workout screen — it joins Session → Blocks → Exercises
     * into a single hierarchical structure ready for rendering.
     *
     * @param sessionId The session to hydrate.
     * @param day The parent day entity (for display context like day name).
     * @return The complete session detail with all blocks and exercises, or null if not found.
     */
    suspend fun getSessionDetail(sessionId: Long, day: DayEntity): SessionDetail? {
        val sessions = programDao.getSessionsByDay(day.id)
        val session = sessions.firstOrNull { it.id == sessionId } ?: return null

        val blocks = programDao.getBlocksBySession(sessionId)
        val blocksWithExercises = blocks.map { block ->
            val exercises = programDao.getExercisesByBlock(block.id)
            BlockWithExercises(block = block, exercises = exercises)
        }

        return SessionDetail(
            session = session,
            day = day,
            blocks = blocksWithExercises
        )
    }

    // ── Seed Data ──────────────────────────────────────────────────────────────

    /**
     * Checks if any programs exist in the database.
     *
     * @return True if the database needs seeding (no programs exist).
     */
    suspend fun needsSeeding(): Boolean = programDao.getProgramCount() == 0

    /** Inserts a program and returns its generated ID. */
    suspend fun insertProgram(program: ProgramEntity): Long = programDao.insertProgram(program)

    /** Inserts a week and returns its generated ID. */
    suspend fun insertWeek(week: WeekEntity): Long = programDao.insertWeek(week)

    /** Inserts a day and returns its generated ID. */
    suspend fun insertDay(day: DayEntity): Long = programDao.insertDay(day)

    /** Inserts a session and returns its generated ID. */
    suspend fun insertSession(session: SessionEntity): Long = programDao.insertSession(session)

    /** Inserts a block and returns its generated ID. */
    suspend fun insertBlock(block: BlockEntity): Long = programDao.insertBlock(block)

    /** Inserts an exercise and returns its generated ID. */
    suspend fun insertExercise(exercise: ExerciseEntity): Long = programDao.insertExercise(exercise)
}

// End of ProgramRepository.kt — Repository for program catalog and session detail queries.
