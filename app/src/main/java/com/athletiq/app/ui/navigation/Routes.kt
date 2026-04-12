package com.athletiq.app.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the Athletiq app.
 *
 * Uses kotlinx.serialization to enable type-safe argument passing with Compose Navigation.
 * Each route is a sealed member that defines the screen's required arguments (if any).
 *
 * **Navigation flow:**
 * ```
 * Catalog ──(select program)──> Today ──(start workout)──> Workout
 *    ↑                            │                            │
 *    └──(abandon program)─────────┘                            │
 *                                 └──(view history)──> History ┘
 *                                 └──(my programs)──> MyPrograms
 *                                 └──(settings)──> Settings
 * ```
 */
object Routes {

    /** Program catalog / browse screen. Entry point when no program is active. */
    @Serializable
    data object Catalog

    /**
     * Today's session overview. Shows the current training day or rest day.
     * This is the home screen when a program is active.
     */
    @Serializable
    data object Today

    /**
     * Guided workout execution screen.
     *
     * @property sessionId The session template to execute.
     * @property enrollmentId The active enrollment for logging.
     * @property dayId The day entity ID for context.
     */
    @Serializable
    data class Workout(
        val sessionId: Long,
        val enrollmentId: Long,
        val dayId: Long
    )

    /** Workout history screen showing completed sessions and exercise logs. */
    @Serializable
    data object History

    /** "My Programs" screen listing all past and current enrollments. */
    @Serializable
    data object MyPrograms

    /** Settings screen (units, rest timer defaults, notifications). */
    @Serializable
    data object Settings
}

// End of Routes.kt — Type-safe navigation route definitions for Compose Navigation.
