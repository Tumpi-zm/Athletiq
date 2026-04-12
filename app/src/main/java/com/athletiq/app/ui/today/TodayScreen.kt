package com.athletiq.app.ui.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.athletiq.app.domain.model.BlockType
import com.athletiq.app.domain.model.SessionDetail
import com.athletiq.app.domain.usecase.AbandonProgramResult
import com.athletiq.app.ui.components.ProgramProgressBar

/**
 * Today screen — shows the current training day's session or rest day info.
 *
 * This is the home screen when a program is active. It displays:
 * - Current week and day name.
 * - Program progress bar.
 * - Session overview with all blocks and exercises.
 * - "Start Workout" button to begin guided execution.
 * - Abandon program option in the app bar.
 *
 * @param onStartWorkout Callback with (sessionId, enrollmentId, dayId) to navigate to workout.
 * @param onProgramAbandoned Callback when the program has been successfully abandoned.
 * @param onNavigateToHistory Navigate to workout history.
 * @param onNavigateToMyPrograms Navigate to My Programs.
 * @param onNavigateToSettings Navigate to Settings.
 * @param viewModel The ViewModel provided by Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onStartWorkout: (sessionId: Long, enrollmentId: Long, dayId: Long) -> Unit,
    onProgramAbandoned: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToMyPrograms: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val abandonEvent by viewModel.abandonEvent.collectAsStateWithLifecycle()
    var showAbandonDialog by remember { mutableStateOf(false) }

    // Handle abandon result.
    LaunchedEffect(abandonEvent) {
        if (abandonEvent is AbandonProgramResult.Success) {
            viewModel.clearAbandonEvent()
            onProgramAbandoned()
        }
    }

    // Redirect to catalog if no active program.
    LaunchedEffect(uiState) {
        if (uiState is TodayUiState.NoActiveProgram) {
            onProgramAbandoned()
        }
    }

    // Abandon confirmation dialog.
    if (showAbandonDialog) {
        val state = uiState
        val programName = when (state) {
            is TodayUiState.TrainingDay -> state.program.name
            is TodayUiState.RestDay -> state.program.name
            else -> "this program"
        }
        val weekInfo = when (state) {
            is TodayUiState.TrainingDay -> "Week ${state.weekNumber} of ${state.program.durationWeeks}"
            is TodayUiState.RestDay -> "Week ${state.weekNumber} of ${state.program.durationWeeks}"
            else -> ""
        }

        AlertDialog(
            onDismissRequest = { showAbandonDialog = false },
            title = { Text("Abandon Program?") },
            text = {
                Text(
                    "You're on $weekInfo of $programName. " +
                    "Are you sure you want to stop? Your logged workout history will be kept."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAbandonDialog = false
                        viewModel.abandonProgram()
                    }
                ) {
                    Text("Abandon", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAbandonDialog = false }) {
                    Text("Keep Training")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { showAbandonDialog = true }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Abandon Program",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is TodayUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TodayUiState.TrainingDay -> {
                TrainingDayContent(
                    state = state,
                    paddingValues = paddingValues,
                    onStartWorkout = { sessionDetail ->
                        onStartWorkout(
                            sessionDetail.session.id,
                            state.enrollment.id,
                            sessionDetail.day.id
                        )
                    }
                )
            }

            is TodayUiState.RestDay -> {
                RestDayContent(state = state, paddingValues = paddingValues)
            }

            is TodayUiState.ProgramComplete -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Program Complete!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You've finished ${state.program.name}. Great work!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is TodayUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> { /* NoActiveProgram and NotStartedYet handled by LaunchedEffect */ }
        }
    }
}

/**
 * Content for a training day — shows session overview with blocks and exercises.
 */
@Composable
private fun TrainingDayContent(
    state: TodayUiState.TrainingDay,
    paddingValues: PaddingValues,
    onStartWorkout: (SessionDetail) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header: Week + Day info.
        item {
            Column {
                Text(
                    text = "Week ${state.weekNumber}${state.weekFocus?.let { " — $it" } ?: ""}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = state.dayName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgramProgressBar(
                    completedDays = state.completedDays,
                    totalDays = state.totalTrainingDays
                )
            }
        }

        // Session cards with exercise previews.
        items(state.sessions) { sessionDetail ->
            SessionPreviewCard(
                sessionDetail = sessionDetail,
                onStartWorkout = { onStartWorkout(sessionDetail) }
            )
        }
    }
}

/**
 * Card previewing a session's blocks and exercises with a "Start Workout" button.
 */
@Composable
private fun SessionPreviewCard(
    sessionDetail: SessionDetail,
    onStartWorkout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = sessionDetail.session.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // List blocks with their exercises.
            sessionDetail.blocks.forEach { blockWithExercises ->
                val blockTypeName = BlockType.fromString(blockWithExercises.block.blockType).name
                Text(
                    text = blockWithExercises.block.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                blockWithExercises.exercises.forEach { exercise ->
                    Text(
                        text = "  ${exercise.name} — ${exercise.sets}×${exercise.reps}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onStartWorkout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Text(
                    text = "  Start Workout",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Content for a rest day — shows recovery suggestions.
 */
@Composable
private fun RestDayContent(
    state: TodayUiState.RestDay,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SelfImprovement,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Rest Day",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Week ${state.weekNumber} · ${state.program.name}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProgramProgressBar(
            completedDays = state.completedDays,
            totalDays = state.totalTrainingDays,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!state.notes.isNullOrBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Recovery Suggestions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// End of TodayScreen.kt — Home screen showing today's training session or rest day.
