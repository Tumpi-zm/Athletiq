package com.athletiq.app.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.athletiq.app.domain.model.ProgramOverview
import com.athletiq.app.domain.usecase.StartProgramResult
import com.athletiq.app.ui.components.ProgramProgressBar

/**
 * Program catalog screen — entry point when no program is active.
 *
 * Displays a list of available training programs with their descriptions, tags,
 * and duration. Each program has a "Start Program" button that triggers enrollment.
 * If a program is already active, a banner at the top shows its progress and a
 * "Continue Workout" button that navigates directly to the active session.
 *
 * @param onProgramStarted Callback when a program has been successfully started.
 * @param onContinueToActiveProgram Callback when the user taps "Continue Workout" on the active banner.
 * @param onNavigateToMyPrograms Callback to navigate to the My Programs screen.
 * @param onNavigateToSettings Callback to navigate to the Settings screen.
 * @param viewModel The ViewModel powering this screen, provided by Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CatalogScreen(
    onProgramStarted: () -> Unit,
    onContinueToActiveProgram: () -> Unit = {},
    onNavigateToMyPrograms: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onViewProgram: (Long) -> Unit = {},
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val startEvent by viewModel.startProgramEvent.collectAsStateWithLifecycle()
    var showActiveDialog by remember { mutableStateOf(false) }

    // Handle start program result.

    LaunchedEffect(startEvent) {
        when (startEvent) {
            is StartProgramResult.Success -> {
                viewModel.clearStartProgramEvent()
                onProgramStarted()
            }
            is StartProgramResult.ActiveProgramExists -> {
                showActiveDialog = true
            }
            else -> {}
        }
    }

    if (showActiveDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showActiveDialog = false; viewModel.clearStartProgramEvent() },
            title = { Text("Active Program Exists") },
            text = { Text("You already have an active program. Would you like to go to Today's session?") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showActiveDialog = false
                    viewModel.clearStartProgramEvent()
                    onProgramStarted()
                }) {
                    Text("Go to Today")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showActiveDialog = false
                    viewModel.clearStartProgramEvent()
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Athletiq",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToMyPrograms) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "My Programs"
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
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
            is CatalogUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CatalogUiState.Error -> {
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

            is CatalogUiState.Success -> {
                if (state.programs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No programs available yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Active program banner — shown when the user already has a program running.
                        if (state.activeProgram != null) {
                            item(key = "active_program_banner") {
                                ActiveProgramCard(
                                    info = state.activeProgram,
                                    onContinueWorkout = onContinueToActiveProgram
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Choose Your Program",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Select a structured training program to get started.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        items(state.programs, key = { it.program.id }) { overview ->
                            ProgramCard(
                                overview = overview,
                                onStartProgram = { viewModel.startProgram(overview.program.id) },
                                onViewProgram = { onViewProgram(overview.program.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card displaying a single program in the catalog.
 *
 * @param overview The program data with enrollment metadata.
 * @param onStartProgram Callback when the user taps "Start Program".
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProgramCard(
    overview: ProgramOverview,
    onStartProgram: () -> Unit,
    onViewProgram: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = overview.program.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${overview.program.durationWeeks} weeks · ${overview.totalTrainingDays} training days",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = overview.program.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tags.
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                overview.program.tags.forEach { tag ->
                    AssistChip(
                        onClick = { /* No-op — tags are display only */ },
                        label = { Text(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewProgram,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Program")
                }
                Button(
                    onClick = onStartProgram,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start Program")
                }
            }
        }
    }
}

/**
 * Banner card shown at the top of the catalog when the user has an active program.
 *
 * Displays the program name, an animated progress bar, and a "Continue Workout" button.
 *
 * @param info The active program summary data.
 * @param onContinueWorkout Called when the user taps "Continue Workout".
 */
@Composable
private fun ActiveProgramCard(
    info: ActiveProgramInfo,
    onContinueWorkout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "ACTIVE PROGRAM",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = info.programName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProgramProgressBar(
                completedDays = info.completedDays,
                totalDays = info.totalTrainingDays
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinueWorkout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue Workout")
            }
        }
    }
}

// End of CatalogScreen.kt — Program catalog with active program banner and enrollment initiation.
