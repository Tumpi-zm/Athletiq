package com.athletiq.app.domain.usecase

import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.SessionEntity
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.domain.model.SessionDetail
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Determines what the user should train today based on their active enrollment.
 *
 * Performs the following calculation:
 * 1. Takes the enrollment's [startDate] and today's date.
 * 2. Computes how many days have elapsed → derives the current week number and day of week.
 * 3. Looks up the matching [DayEntity] in the program structure.
 * 4. If it's a training day, hydrates the full [SessionDetail] for workout execution.
 * 5. If it's a rest day, returns rest day information.
 *
 * **Week calculation logic:**
 * - Day 0 (start date) = Week 1, with the day-of-week of the start date.
 * - Week boundaries advance every 7 days from the start date.
 * - If the calculated week exceeds the program's duration, the program is over.
 *
 * @see SessionDetail for the full session model returned for training days
 * @see TodaySessionResult for the sealed result type
 */
class GetTodaySessionUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {

    /**
     * Resolves today's training session for the given enrollment.
     *
     * @param enrollment The user's active enrollment.
     * @param today The current date (injected for testability, defaults to system date).
     * @return A [TodaySessionResult] describing what the user should do today.
     */
    suspend operator fun invoke(
        enrollment: EnrollmentEntity,
        today: LocalDate = LocalDate.now()
    ): TodaySessionResult {
        val program = programRepository.getProgramById(enrollment.programId)
            ?: return TodaySessionResult.Error("Program not found")

        // Calculate elapsed days since program start.
        // Day 0 = start date, Day 1 = next day, etc.
        val elapsedDays = ChronoUnit.DAYS.between(enrollment.startDate, today).toInt()

        if (elapsedDays < 0) {
            return TodaySessionResult.NotStartedYet(enrollment.startDate)
        }

        // Week number is 1-indexed: days 0–6 = Week 1, days 7–13 = Week 2, etc.
        val weekNumber = (elapsedDays / 7) + 1

        if (weekNumber > program.durationWeeks) {
            return TodaySessionResult.ProgramComplete
        }

        // Get the ISO day-of-week for today (1=Monday, 7=Sunday).
        val dayOfWeek = today.dayOfWeek.value

        // Resolve the week and day entities from the program structure.
        val week = programRepository.getWeek(enrollment.programId, weekNumber)
            ?: return TodaySessionResult.RestDay(
                weekNumber = weekNumber,
                dayOfWeek = dayOfWeek,
                notes = null
            )

        val day = programRepository.getDay(week.id, dayOfWeek)
            ?: return TodaySessionResult.RestDay(
                weekNumber = weekNumber,
                dayOfWeek = dayOfWeek,
                notes = null
            )

        // Rest day — show recovery suggestions.
        if (day.isRestDay) {
            return TodaySessionResult.RestDay(
                weekNumber = weekNumber,
                dayOfWeek = dayOfWeek,
                notes = day.restDayNotes
            )
        }

        // Training day — build the full session detail.
        val sessions = programRepository.getSessionsByDay(day.id)
        if (sessions.isEmpty()) {
            return TodaySessionResult.RestDay(
                weekNumber = weekNumber,
                dayOfWeek = dayOfWeek,
                notes = "No sessions scheduled."
            )
        }

        // Build session details for each session in the day.
        val sessionDetails = sessions.mapNotNull { session ->
            programRepository.getSessionDetail(session.id, day)
        }

        return TodaySessionResult.TrainingDay(
            weekNumber = weekNumber,
            weekFocus = week.focus,
            day = day,
            sessions = sessionDetails
        )
    }
}

/**
 * Sealed result type for [GetTodaySessionUseCase].
 *
 * Represents all possible outcomes when resolving today's training plan.
 */
sealed interface TodaySessionResult {

    /**
     * Today is a training day with one or more sessions to execute.
     *
     * @property weekNumber Current week number (1-indexed).
     * @property weekFocus Optional mesocycle focus label.
     * @property day The day entity with metadata.
     * @property sessions Full session details ready for the workout screen.
     */
    data class TrainingDay(
        val weekNumber: Int,
        val weekFocus: String?,
        val day: DayEntity,
        val sessions: List<SessionDetail>
    ) : TodaySessionResult

    /**
     * Today is a rest day — no training scheduled.
     *
     * @property weekNumber Current week number.
     * @property dayOfWeek ISO day of week.
     * @property notes Optional recovery/mobility suggestions.
     */
    data class RestDay(
        val weekNumber: Int,
        val dayOfWeek: Int,
        val notes: String?
    ) : TodaySessionResult

    /**
     * The program hasn't started yet (start date is in the future).
     *
     * @property startDate When the program begins.
     */
    data class NotStartedYet(val startDate: LocalDate) : TodaySessionResult

    /** The program's duration has been exceeded — it's complete or overdue. */
    data object ProgramComplete : TodaySessionResult

    /**
     * An error occurred during session resolution.
     *
     * @property message Human-readable error description.
     */
    data class Error(val message: String) : TodaySessionResult
}

// End of GetTodaySessionUseCase.kt — Day-aware session resolution from enrollment dates.
