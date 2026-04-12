package com.athletiq.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.athletiq.app.ui.catalog.CatalogScreen
import com.athletiq.app.ui.history.HistoryScreen
import com.athletiq.app.ui.overview.ProgramOverviewScreen
import com.athletiq.app.ui.programs.MyProgramsScreen
import com.athletiq.app.ui.settings.SettingsScreen
import com.athletiq.app.ui.today.TodayScreen
import com.athletiq.app.ui.workout.WorkoutScreen

/**
 * Root navigation graph for the Athletiq app.
 *
 * Defines all screen destinations and their composable content.
 * Uses type-safe routes from [Routes] for argument passing.
 *
 * **Start destination logic:**
 * - If no program is active → [Routes.Catalog] (browse and select a program)
 * - If a program is active → [Routes.Today] (today's session)
 *
 * The start destination is determined by [MainActivity] based on the enrollment state
 * before setting up the nav graph.
 *
 * @param navController The navigation controller managing the back stack.
 * @param startDestination The initial route (Catalog or Today).
 * @param modifier Modifier for the NavHost container.
 */
@Composable
fun AthletiqNavGraph(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ── Program Catalog ────────────────────────────────────────────────────
        composable<Routes.Catalog> {
            CatalogScreen(
                onProgramStarted = {
                    // After starting a program, navigate to Today and clear the back stack
                    // so pressing back doesn't go back to the catalog.
                    navController.navigate(Routes.Today) {
                        popUpTo(Routes.Catalog) { inclusive = true }
                    }
                },
                onNavigateToMyPrograms = {
                    navController.navigate(Routes.MyPrograms)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.Settings)
                },
                onViewProgram = { programId ->
                    navController.navigate(Routes.ProgramOverview(programId = programId))
                }
            )
        }

        // ── Today's Session ────────────────────────────────────────────────────
        composable<Routes.Today> {
            TodayScreen(
                onStartWorkout = { sessionId, enrollmentId, dayId ->
                    navController.navigate(
                        Routes.Workout(
                            sessionId = sessionId,
                            enrollmentId = enrollmentId,
                            dayId = dayId
                        )
                    )
                },
                onProgramAbandoned = {
                    navController.navigate(Routes.Catalog) {
                        popUpTo(Routes.Today) { inclusive = true }
                    }
                },
                onNavigateToHistory = {
                    navController.navigate(Routes.History)
                },
                onNavigateToMyPrograms = {
                    navController.navigate(Routes.MyPrograms)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.Settings)
                }
            )
        }

        // ── Workout Execution ──────────────────────────────────────────────────
        composable<Routes.Workout> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.Workout>()
            WorkoutScreen(
                sessionId = route.sessionId,
                enrollmentId = route.enrollmentId,
                dayId = route.dayId,
                onWorkoutComplete = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ── History ────────────────────────────────────────────────────────────
        composable<Routes.History> {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── My Programs ────────────────────────────────────────────────────────
        composable<Routes.MyPrograms> {
            MyProgramsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Settings ───────────────────────────────────────────────────────────
        composable<Routes.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Routes.ProgramOverview> {
            ProgramOverviewScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// End of AthletiqNavGraph.kt — Compose Navigation graph with type-safe routes.
