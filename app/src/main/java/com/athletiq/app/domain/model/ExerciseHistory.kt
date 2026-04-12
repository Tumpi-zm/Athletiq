package com.athletiq.app.domain.model

import com.athletiq.app.data.local.entity.ExerciseLogEntity
import java.time.LocalDate

/**
 * Domain model for displaying an exercise's historical performance data.
 *
 * Aggregates all logged sets for a specific exercise name across all programs and enrollments.
 * Used by the exercise history screen and the workout screen's "previous lift" reference.
 *
 * @see com.athletiq.app.domain.usecase.GetExerciseHistoryUseCase
 */
data class ExerciseHistory(
    /** The exercise name being queried (e.g., "Back Squat"). */
    val exerciseName: String,

    /** All historical set entries, grouped by workout date. */
    val entries: List<ExerciseHistoryEntry>
)

/**
 * A single historical session's worth of data for one exercise.
 *
 * Groups sets from the same workout date together for display
 * (e.g., "Mar 15 — 4 sets: 80kg × 8, 85kg × 6, 85kg × 6, 80kg × 8").
 */
data class ExerciseHistoryEntry(
    /** The date this exercise was performed. */
    val date: LocalDate,

    /** All sets logged for this exercise on this date. */
    val sets: List<ExerciseLogEntity>
)

// End of ExerciseHistory.kt — Domain models for cross-program exercise history display.
