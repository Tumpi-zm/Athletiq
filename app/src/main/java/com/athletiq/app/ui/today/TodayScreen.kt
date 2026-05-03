package com.athletiq.app.ui.today

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.athletiq.app.domain.model.BlockType
import com.athletiq.app.domain.model.SessionDetail
import com.athletiq.app.domain.usecase.AbandonProgramResult
import com.athletiq.app.domain.usecase.WeekDaySummary
import com.athletiq.app.ui.components.ProgramProgressBar

private val DAY_LABELS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onStartWorkout: (sessionId: Long, enrollmentId: Long, dayId: Long) -> Unit,
    onProgramAbandoned: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToMyPrograms: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCatalog: () -> Unit, // <-- Added callback for navigating back to Catalog
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val abandonEvent by viewModel.abandonEvent.collectAsStateWithLifecycle()
    var showAbandonDialog by remember { mutableStateOf(false) }

    LaunchedEffect(abandonEvent) {
        if (abandonEvent is AbandonProgramResult.Success) {
            viewModel.clearAbandonEvent()
            onProgramAbandoned()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is TodayUiState.NoActiveProgram) {
            onProgramAbandoned()
        }
    }

    if (showAbandonDialog) {
        val state = uiState
        val programName = (state as? TodayUiState.WeekView)?.program?.name ?: "this program"
        val weekInfo = (state as? TodayUiState.WeekView)?.let {
            "Week ${it.weekNumber} of ${it.program.durationWeeks}"
        } ?: ""

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
                navigationIcon = {
                    IconButton(onClick = onNavigateToCatalog) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Catalog"
                        )
                    }
                },
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

            is TodayUiState.WeekView -> {
                WeekViewContent(
                    state = state,
                    paddingValues = paddingValues,
                    onDaySelected = { viewModel.selectDay(it) },
                    onStartWorkout = { sessionDetail ->
                        val dayEntity = state.selectedDaySummary.dayEntity ?: return@WeekViewContent
                        onStartWorkout(
                            sessionDetail.session.id,
                            state.enrollment.id,
                            dayEntity.id
                        )
                    },
                    onPreviousWeek = { viewModel.navigateWeek(-1) },
                    onNextWeek = { viewModel.navigateWeek(+1) }
                )
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

// ── Week View ────────────────────────────────────────────────────────────────

@Composable
private fun WeekViewContent(
    state: TodayUiState.WeekView,
    paddingValues: PaddingValues,
    onDaySelected: (Int) -> Unit,
    onStartWorkout: (SessionDetail) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Week header + progress.
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onPreviousWeek,
                        enabled = state.weekNumber > 1
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous week"
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Week ${state.weekNumber} of ${state.totalWeeks}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        if (state.weekFocus != null) {
                            Text(
                                text = state.weekFocus,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (!state.isCurrentWeek) {
                            Text(
                                text = "Browsing — not your current week",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    IconButton(
                        onClick = onNextWeek,
                        enabled = state.weekNumber < state.totalWeeks
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next week"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                ProgramProgressBar(
                    completedDays = state.completedDays,
                    totalDays = state.totalTrainingDays
                )
            }
        }

        // Day selector bar.
        item {
            DaySelectorRow(
                days = state.days,
                todayDayOfWeek = state.todayDayOfWeek,
                selectedDayOfWeek = state.selectedDayOfWeek,
                isCurrentWeek = state.isCurrentWeek,
                completedDaysOfWeek = state.completedDaysOfWeek,
                onDaySelected = onDaySelected
            )
        }

        // Selected day name.
        item {
            Text(
                text = state.selectedDaySummary.dayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // Day content: sessions or rest day.
        if (state.selectedDaySummary.isRestDay) {
            item {
                RestDayCard(notes = state.selectedDaySummary.restDayNotes)
            }
        } else {
            items(state.selectedDaySessions) { sessionDetail ->
                SessionPreviewCard(
                    sessionDetail = sessionDetail,
                    showStartButton = state.isCurrentWeek,
                    isCompleted = sessionDetail.session.id in state.completedSessionIds,
                    onStartWorkout = { onStartWorkout(sessionDetail) }
                )
            }
        }
    }
}

// ── Day Selector Row ─────────────────────────────────────────────────────────

@Composable
private fun DaySelectorRow(
    days: List<WeekDaySummary>,
    todayDayOfWeek: Int,
    selectedDayOfWeek: Int,
    isCurrentWeek: Boolean,
    completedDaysOfWeek: Set<Int>,
    onDaySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        days.forEach { day ->
            val isSelected = day.dayOfWeek == selectedDayOfWeek
            val isToday = isCurrentWeek && day.dayOfWeek == todayDayOfWeek
            val isTraining = !day.isRestDay
            val isDayCompleted = isTraining && day.dayOfWeek in completedDaysOfWeek

            val containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.surface
            }
            val contentColor = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isTraining -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            }
            val border = if (isToday && !isSelected) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                null
            }

            Surface(
                onClick = { onDaySelected(day.dayOfWeek) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                color = containerColor,
                contentColor = contentColor,
                border = border,
                tonalElevation = if (isSelected) 0.dp else 1.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        text = DAY_LABELS[day.dayOfWeek - 1],
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    if (isTraining) {
                        if (isDayCompleted && !isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Session Preview Card ─────────────────────────────────────────────────────

@Composable
private fun SessionPreviewCard(
    sessionDetail: SessionDetail,
    showStartButton: Boolean,
    isCompleted: Boolean,
    onStartWorkout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Completed banner — shown at the top of the card when the session is finished.
            if (isCompleted) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = sessionDetail.session.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            sessionDetail.blocks.forEach { blockWithExercises ->
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

            if (isCompleted) {
                // Disabled "Completed" button — the workout is done for this day.
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Text(text = "  Completed", style = MaterialTheme.typography.labelLarge)
                }
            } else if (showStartButton) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onStartWorkout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Text(text = "  Start Workout", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

// ── Rest Day Card ────────────────────────────────────────────────────────────

@Composable
private fun RestDayCard(notes: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SelfImprovement,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Rest Day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (!notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// End of TodayScreen.kt — Displays the user's current training day with week navigation, day selector, completed-workout indicators, and primary workout actions.
