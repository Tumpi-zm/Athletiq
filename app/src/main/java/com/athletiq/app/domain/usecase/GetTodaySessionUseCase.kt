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
 * Determines what the user should train today based on their active enrollment,
 * and provides the full week's schedule so the user can pick any day.
 */
class GetTodaySessionUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {

    /**
     * Resolves the current week's full schedule for the given enrollment.
     *
     * @param enrollment The user's active enrollment.
     * @param today The current date (injected for testability, defaults to system date).
     * @return A [WeekScheduleResult] with all days of the current week.
     */
    suspend operator fun invoke(
        enrollment: EnrollmentEntity,
        today: LocalDate = LocalDate.now()
    ): WeekScheduleResult {
        val program = programRepository.getProgramById(enrollment.programId)
            ?: return WeekScheduleResult.Error("Program not found")

        val elapsedDays = ChronoUnit.DAYS.between(enrollment.startDate, today).toInt()

        if (elapsedDays < 0) {
            return WeekScheduleResult.NotStartedYet(enrollment.startDate)
        }

        val weekNumber = (elapsedDays / 7) + 1

        if (weekNumber > program.durationWeeks) {
            return WeekScheduleResult.ProgramComplete
        }

        val todayDayOfWeek = today.dayOfWeek.value

        val week = programRepository.getWeek(enrollment.programId, weekNumber)
            ?: return WeekScheduleResult.Error("Week $weekNumber not found")

        // Load ALL days for this week.
        val allDays = programRepository.getDaysByWeek(week.id)
        val dayMap = allDays.associateBy { it.dayOfWeek }

        // Build a summary for each day of the week (Mon=1 .. Sun=7).
        val daySummaries = (1..7).map { dow ->
            val day = dayMap[dow]
            if (day == null || day.isRestDay) {
                WeekDaySummary(
                    dayOfWeek = dow,
                    isRestDay = true,
                    dayName = day?.name ?: "Rest Day",
                    restDayNotes = day?.restDayNotes,
                    dayEntity = day
                )
            } else {
                WeekDaySummary(
                    dayOfWeek = dow,
                    isRestDay = false,
                    dayName = day.name,
                    restDayNotes = null,
                    dayEntity = day
                )
            }
        }

        return WeekScheduleResult.ActiveWeek(
            weekNumber = weekNumber,
            weekFocus = week.focus,
            todayDayOfWeek = todayDayOfWeek,
            days = daySummaries
        )
    }

    /**
     * Resolves full session details for a specific day (called when user selects a day).
     */
    suspend fun getSessionDetailsForDay(dayEntity: DayEntity): List<SessionDetail> {
        val sessions = programRepository.getSessionsByDay(dayEntity.id)
        return sessions.mapNotNull { session ->
            programRepository.getSessionDetail(session.id, dayEntity)
        }
    }
}

// ── Result types ─────────────────────────────────────────────────────────────

/**
 * Summary of a single day within the current week (used for the day selector).
 */
data class WeekDaySummary(
    val dayOfWeek: Int,
    val isRestDay: Boolean,
    val dayName: String,
    val restDayNotes: String?,
    val dayEntity: DayEntity?
)

/**
 * Sealed result type for the week schedule resolution.
 */
sealed interface WeekScheduleResult {

    /**
     * The current week is active with a full 7-day schedule.
     */
    data class ActiveWeek(
        val weekNumber: Int,
        val weekFocus: String?,
        val todayDayOfWeek: Int,
        val days: List<WeekDaySummary>
    ) : WeekScheduleResult

    data class NotStartedYet(val startDate: LocalDate) : WeekScheduleResult

    data object ProgramComplete : WeekScheduleResult

    data class Error(val message: String) : WeekScheduleResult
}

// End of GetTodaySessionUseCase.kt — Week-aware session resolution from enrollment dates.
