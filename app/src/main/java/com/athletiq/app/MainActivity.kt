package com.athletiq.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.athletiq.app.data.repository.EnrollmentRepository
import com.athletiq.app.data.repository.ProgramRepository
import com.athletiq.app.data.seed.SeedDataProvider
import com.athletiq.app.ui.navigation.AthletiqNavGraph
import com.athletiq.app.ui.navigation.Routes
import com.athletiq.app.ui.theme.AthletiqTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Single-activity host for the Athletiq app.
 *
 * **Responsibilities:**
 * 1. Seeds the database on first launch via [SeedDataProvider].
 * 2. Determines the start destination: [Routes.Today] if a program is active,
 *    or [Routes.Catalog] if the user needs to choose a program.
 * 3. Sets up the Compose content with edge-to-edge theme and navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var enrollmentRepository: EnrollmentRepository
    @Inject lateinit var programRepository: ProgramRepository
    @Inject lateinit var seedDataProvider: SeedDataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var startDestination: Any by mutableStateOf(Routes.Catalog as Any)
        var isReady by mutableStateOf(false)

        lifecycleScope.launch {
            // Seed the database if it's empty (first launch).
            if (programRepository.needsSeeding()) {
                seedDataProvider.seedDatabase()
            }

            // Determine start destination based on active enrollment.
            val activeEnrollment = enrollmentRepository.getActiveEnrollmentOnce()
            startDestination = if (activeEnrollment != null) Routes.Today else Routes.Catalog
            isReady = true
        }

        setContent {
            AthletiqTheme {
                if (isReady) {
                    val navController = rememberNavController()
                    AthletiqNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}

// End of MainActivity.kt — Single-activity entry point with seed and start destination logic.
