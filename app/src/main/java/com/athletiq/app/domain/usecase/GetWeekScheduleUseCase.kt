package com.athletiq.app.domain.usecase

import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.repository.ProgramRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Loads any week's full schedule by week number for the Today screen week navigation.
 * Used when the user browses backwards or forwards through the 90-day program weeks.
 *
 * This use case does NOT calculate which week is "today's" week — that is the job of
 * [GetTodaySessionUseCase]. Instead, it takes an explicit [weekNumber] and returns the
 * full 7-day schedule for that week, plus [WeekScheduleResult.ActiveWeek.todayDayOfWeek]
 * so the screen can highlight the correct day when the user returns to the current week.
 */
class GetWeekScheduleUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {

    /**
     * @param enrollment  The user's active enrollment (provides programId).
     * @param weekNumber  The 1-based week to load (must be ≥ 1 and ≤ program.durationWeeks).
     * @param today       The current date (injected for testability; defaults to system date).
     * @return [WeekScheduleResult.ActiveWeek] on success, [WeekScheduleResult.Error] if the
     *         week is not found in the database.
     */
    suspend operator fun invoke(
        enrollment: EnrollmentEntity,
        weekNumber: Int,
        today: LocalDate = LocalDate.now()
    ): WeekScheduleResult {

        val week = programRepository.getWeek(enrollment.programId, weekNumber)
            ?: return WeekScheduleResult.Error("Week $weekNumber not found")

        val allDays = programRepository.getDaysByWeek(week.id)
        val dayMap = allDays.associateBy { it.dayOfWeek }

        // Build a summary for each day of the week (Mon=1 .. Sun=7),
        // using the exact same logic as GetTodaySessionUseCase.
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

        // todayDayOfWeek reflects the *real* current day, not a day inside the browsed week.
        // This allows the screen to highlight today correctly when the user returns to
        // the current week, even if they are currently viewing a different week.
        return WeekScheduleResult.ActiveWeek(
            weekNumber = weekNumber,
            weekFocus = week.focus,
            todayDayOfWeek = today.dayOfWeek.value,
            days = daySummaries
        )
    }
}

// End of GetWeekScheduleUseCase.kt — Loads any week by number for the Today screen week navigation.
