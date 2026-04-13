package com.athletiq.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athletiq.app.data.local.entity.ExerciseLogEntity
import com.athletiq.app.data.local.entity.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for workout logging and exercise history queries.
 *
 * Handles two primary concerns:
 * 1. **Write path:** Inserting workout logs and exercise logs during guided workout execution.
 * 2. **Read path:** Querying historical data for the progress screen, exercise history lookups,
 *    and weight pre-fill during workout execution.
 *
 * Exercise history queries use [ExerciseLogEntity.exerciseName] (denormalized) for efficient
 * cross-program lookups without joining through the full program → week → day → session chain.
 *
 * @see com.athletiq.app.data.repository.WorkoutLogRepository for the repository layer
 */
@Dao
interface WorkoutLogDao {

    // ── Workout Log Writes ─────────────────────────────────────────────────────

    /**
     * Creates a new workout log header for a started session.
     *
     * @param workoutLog The workout log to insert.
     * @return The auto-generated workout log ID, used as the foreign key for exercise logs.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(workoutLog: WorkoutLogEntity): Long

    /**
     * Logs a single completed set for an exercise within a workout.
     *
     * @param exerciseLog The set-level performance data.
     * @return The auto-generated exercise log ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLog(exerciseLog: ExerciseLogEntity): Long

    /**
     * Batch-inserts multiple exercise log entries (e.g., when saving all sets at once).
     *
     * @param exerciseLogs The list of set-level performance data.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLogs(exerciseLogs: List<ExerciseLogEntity>)

    // ── Workout Log Queries ────────────────────────────────────────────────────

    /**
     * Returns all workout logs for a specific enrollment, ordered by date.
     * Used by the progress screen to show completed sessions.
     *
     * @param enrollmentId The enrollment to query logs for.
     * @return Reactive [Flow] of workout logs newest first.
     */
    @Query("""
        SELECT * FROM workout_logs 
        WHERE enrollmentId = :enrollmentId 
        ORDER BY date DESC
    """)
    fun getWorkoutLogsByEnrollment(enrollmentId: Long): Flow<List<WorkoutLogEntity>>

    /**
     * Returns a workout log for a specific session on a specific date, if it exists.
     * Used to determine if today's session has already been completed.
     *
     * @param sessionId The session template ID.
     * @param date The date to check.
     * @return The existing workout log, or null if the session hasn't been completed on that date.
     */
    @Query("""
        SELECT * FROM workout_logs 
        WHERE sessionId = :sessionId AND date = :date 
        LIMIT 1
    """)
    suspend fun getWorkoutLogForSessionOnDate(sessionId: Long, date: LocalDate): WorkoutLogEntity?

    /**
     * Returns all workout logs across all enrollments, ordered by date descending.
     * Used by the global history screen.
     *
     * @return Reactive [Flow] of all workout logs.
     */
    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLogEntity>>

    /**
     * Updates the duration of a workout log when the session is finished.
     *
     * @param workoutLogId The log to update.
     * @param durationMinutes The total session duration in minutes.
     */
    @Query("UPDATE workout_logs SET durationMinutes = :durationMinutes WHERE id = :workoutLogId")
    suspend fun updateWorkoutDuration(workoutLogId: Long, durationMinutes: Int)

    /**
     * Updates the workout progress position (exercise index and set number).
     */
    @Query("UPDATE workout_logs SET currentExerciseIndex = :exerciseIndex, currentSetNumber = :setNumber WHERE id = :workoutLogId")
    suspend fun updateWorkoutProgress(workoutLogId: Long, exerciseIndex: Int, setNumber: Int)

    /**
     * Finds an in-progress (unfinished) workout log for a session on today's date.
     * A workout is in-progress if durationMinutes is null (not yet completed).
     */
    @Query("""
        SELECT * FROM workout_logs 
        WHERE sessionId = :sessionId AND date = :date AND durationMinutes IS NULL
        LIMIT 1
    """)
    suspend fun getActiveWorkoutLog(sessionId: Long, date: LocalDate): WorkoutLogEntity?

    // ── Exercise History Queries ───────────────────────────────────────────────

    /**
     * Returns all logged sets for a given exercise name across all programs.
     * Used for the exercise history screen and progressive overload analysis.
     *
     * @param exerciseName The exercise name to query (must match exactly).
     * @return Reactive [Flow] of all matching exercise logs, newest first.
     */
    @Query("""
        SELECT * FROM exercise_logs 
        WHERE exerciseName = :exerciseName 
        ORDER BY id DESC
    """)
    fun getExerciseHistory(exerciseName: String): Flow<List<ExerciseLogEntity>>

    /**
     * Returns the most recent weight used for a specific exercise.
     * Used to pre-fill the weight input field during workout execution.
     *
     * This query joins with workout_logs to get the latest entry by date,
     * then picks the last set's weight from that session.
     *
     * @param exerciseName The exercise to find the last weight for.
     * @return The most recently logged weight in kg, or null if never logged.
     */
    @Query("""
        SELECT el.weightKg FROM exercise_logs el
        INNER JOIN workout_logs wl ON el.workoutLogId = wl.id
        WHERE el.exerciseName = :exerciseName AND el.weightKg IS NOT NULL
        ORDER BY wl.date DESC, el.setNumber DESC
        LIMIT 1
    """)
    suspend fun getLastWeightForExercise(exerciseName: String): Float?

    /**
     * Returns all exercise logs for a specific workout log.
     * Used when viewing a completed workout's details.
     *
     * @param workoutLogId The parent workout log ID.
     * @return All exercise logs for that workout, ordered by exercise and set number.
     */
    @Query("""
        SELECT * FROM exercise_logs 
        WHERE workoutLogId = :workoutLogId 
        ORDER BY exerciseId ASC, setNumber ASC
    """)
    suspend fun getExerciseLogsByWorkout(workoutLogId: Long): List<ExerciseLogEntity>

    /**
     * Returns the count of completed workout sessions for an enrollment.
     * A lightweight alternative to counting all logs when only the number is needed.
     *
     * @param enrollmentId The enrollment to count sessions for.
     * @return Number of completed workout sessions.
     */
    @Query("SELECT COUNT(*) FROM workout_logs WHERE enrollmentId = :enrollmentId")
    suspend fun getCompletedSessionCount(enrollmentId: Long): Int

    /**
     * Returns all dates that have a workout log for a given enrollment.
     * Used for the calendar progress view.
     *
     * @param enrollmentId The enrollment to query.
     * @return List of dates with completed workouts.
     */
    @Query("SELECT DISTINCT date FROM workout_logs WHERE enrollmentId = :enrollmentId ORDER BY date ASC")
    fun getCompletedDates(enrollmentId: Long): Flow<List<LocalDate>>
}

// End of WorkoutLogDao.kt — DAO for workout logging and cross-program exercise history.
