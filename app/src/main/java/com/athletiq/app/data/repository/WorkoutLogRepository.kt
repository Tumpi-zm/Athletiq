package com.athletiq.app.data.repository

import com.athletiq.app.data.local.dao.WorkoutLogDao
import com.athletiq.app.data.local.entity.ExerciseLogEntity
import com.athletiq.app.data.local.entity.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for workout logging and exercise history retrieval.
 *
 * Handles two key flows:
 * 1. **Logging:** Creating workout sessions and recording per-set exercise data during
 *    guided workout execution.
 * 2. **History:** Querying cross-program exercise history for progressive overload
 *    decisions and weight pre-fill.
 *
 * @see WorkoutLogDao for the underlying database queries
 */
@Singleton
class WorkoutLogRepository @Inject constructor(
    private val workoutLogDao: WorkoutLogDao
) {

    // ── Logging ────────────────────────────────────────────────────────────────

    /**
     * Creates a workout log header for a session being started.
     *
     * @param enrollmentId The active enrollment ID.
     * @param sessionId The session template being executed.
     * @param date Today's date.
     * @return The generated workout log ID.
     */
    suspend fun startWorkoutLog(
        enrollmentId: Long,
        sessionId: Long,
        date: LocalDate
    ): Long {
        val log = WorkoutLogEntity(
            enrollmentId = enrollmentId,
            sessionId = sessionId,
            date = date
        )
        return workoutLogDao.insertWorkoutLog(log)
    }

    /**
     * Records a completed set for an exercise.
     *
     * @param exerciseLog The set-level performance data.
     * @return The generated exercise log ID.
     */
    suspend fun logExerciseSet(exerciseLog: ExerciseLogEntity): Long =
        workoutLogDao.insertExerciseLog(exerciseLog)

    /**
     * Records multiple sets at once (batch save on workout completion).
     *
     * @param exerciseLogs The list of set data to save.
     */
    suspend fun logExerciseSets(exerciseLogs: List<ExerciseLogEntity>) {
        workoutLogDao.insertExerciseLogs(exerciseLogs)
    }

    /**
     * Updates the total duration of a completed workout.
     *
     * @param workoutLogId The workout to update.
     * @param durationMinutes Total session time in minutes.
     */
    suspend fun finishWorkout(workoutLogId: Long, durationMinutes: Int) {
        workoutLogDao.updateWorkoutDuration(workoutLogId, durationMinutes)
    }

    // ── History Queries ────────────────────────────────────────────────────────

    /**
     * Returns all workout logs for a given enrollment.
     *
     * @param enrollmentId The enrollment to query.
     * @return [Flow] of workout logs, newest first.
     */
    fun getWorkoutLogsByEnrollment(enrollmentId: Long): Flow<List<WorkoutLogEntity>> =
        workoutLogDao.getWorkoutLogsByEnrollment(enrollmentId)

    /**
     * Returns all workout logs globally.
     *
     * @return [Flow] of all workout logs, newest first.
     */
    fun getAllWorkoutLogs(): Flow<List<WorkoutLogEntity>> =
        workoutLogDao.getAllWorkoutLogs()

    /**
     * Returns cross-program exercise history for progressive overload display.
     *
     * @param exerciseName The exercise to query (exact name match).
     * @return [Flow] of all sets for this exercise across all programs.
     */
    fun getExerciseHistory(exerciseName: String): Flow<List<ExerciseLogEntity>> =
        workoutLogDao.getExerciseHistory(exerciseName)

    /**
     * Returns the most recent weight used for an exercise.
     * Drives the weight pre-fill feature in the workout screen.
     *
     * @param exerciseName The exercise to look up.
     * @return Last logged weight in kg, or null if never logged.
     */
    suspend fun getLastWeightForExercise(exerciseName: String): Float? =
        workoutLogDao.getLastWeightForExercise(exerciseName)

    /**
     * Returns exercise log entries for a specific workout.
     *
     * @param workoutLogId The workout to inspect.
     * @return All exercise logs ordered by exercise and set number.
     */
    suspend fun getExerciseLogsByWorkout(workoutLogId: Long): List<ExerciseLogEntity> =
        workoutLogDao.getExerciseLogsByWorkout(workoutLogId)

    /**
     * Checks if a session has already been completed on a given date.
     *
     * @param sessionId The session template ID.
     * @param date The date to check.
     * @return True if the session was already logged on this date.
     */
    suspend fun isSessionCompletedOnDate(sessionId: Long, date: LocalDate): Boolean =
        workoutLogDao.getWorkoutLogForSessionOnDate(sessionId, date) != null

    /**
     * Returns dates with completed workouts for calendar display.
     *
     * @param enrollmentId The enrollment to query.
     * @return [Flow] of dates in ascending order.
     */
    fun getCompletedDates(enrollmentId: Long): Flow<List<LocalDate>> =
        workoutLogDao.getCompletedDates(enrollmentId)

    /**
     * Returns the count of completed sessions for an enrollment.
     *
     * @param enrollmentId The enrollment to count.
     * @return Number of completed workout sessions.
     */
    suspend fun getCompletedSessionCount(enrollmentId: Long): Int =
        workoutLogDao.getCompletedSessionCount(enrollmentId)
}

// End of WorkoutLogRepository.kt — Repository for workout logging and exercise history.
