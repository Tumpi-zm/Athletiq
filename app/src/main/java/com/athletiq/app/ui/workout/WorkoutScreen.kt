package com.athletiq.app.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.athletiq.app.ui.components.RestTimerBar
import com.athletiq.app.ui.components.SetTrackerCard

/**
 * Guided workout execution screen — the most important screen in the app.
 *
 * Walks the user through their session exercise-by-exercise, set-by-set:
 * 1. Shows the current exercise with coaching metadata (target muscles, tempo, RPE, notes).
 * 2. Accepts weight and reps input for each set.
 * 3. Displays a countdown rest timer between sets.
 * 4. Shows a completion summary when the workout is finished.
 *
 * @param sessionId The session template to execute.
 * @param enrollmentId The active enrollment for logging.
 * @param dayId The parent day ID for session lookup.
 * @param onWorkoutComplete Callback when the workout is finished.
 * @param onNavigateBack Callback to go back (with partial save prompt).
 * @param viewModel The ViewModel provided by Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    sessionId: Long,
    enrollmentId: Long,
    dayId: Long,
    onWorkoutComplete: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Initialize the workout on first composition.
    LaunchedEffect(sessionId) {
        viewModel.initWorkout(sessionId, enrollmentId, dayId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is WorkoutUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is WorkoutUiState.Exercising -> {
                ExercisingContent(
                    state = state,
                    onWeightChanged = viewModel::updateWeightInput,
                    onRepsChanged = viewModel::updateRepsInput,
                    onCompleteSet = viewModel::completeSet,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is WorkoutUiState.Resting -> {
                RestingContent(
                    state = state,
                    onRestComplete = viewModel::onRestComplete,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is WorkoutUiState.Complete -> {
                CompleteContent(
                    durationMinutes = state.durationMinutes,
                    onFinish = onWorkoutComplete,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is WorkoutUiState.Error -> {
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
        }
    }
}

/**
 * Content displayed when the user is actively performing a set.
 */
@Composable
private fun ExercisingContent(
    state: WorkoutUiState.Exercising,
    onWeightChanged: (String) -> Unit,
    onRepsChanged: (String) -> Unit,
    onCompleteSet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Overall exercise progress bar (exercise X of Y).
        val exerciseProgress = (state.currentExerciseIndex + 1f) / state.totalExercises
        LinearProgressIndicator(
            progress = { exerciseProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Exercise ${state.currentExerciseIndex + 1} of ${state.totalExercises}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Block context.
        Text(
            text = state.blockName,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Exercise metadata card.
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (state.targetMuscles != null) {
                    Text(
                        text = "Target: ${state.targetMuscles}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (state.tempo != null) {
                    Text(
                        text = "Tempo: ${state.tempo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (state.rpe != null) {
                    Text(
                        text = "RPE: ${state.rpe}/10",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (state.notes != null) {
                    Text(
                        text = state.notes,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Set tracker with weight/reps input.
        SetTrackerCard(
            exerciseName = state.exerciseName,
            currentSet = state.currentSet,
            totalSets = state.totalSets,
            targetReps = state.targetReps,
            weightKg = state.weightKg,
            repsCompleted = state.repsCompleted,
            onWeightChanged = onWeightChanged,
            onRepsChanged = onRepsChanged,
            onCompleteSet = onCompleteSet
        )
    }
}

/**
 * Content displayed during the rest timer between sets.
 */
@Composable
private fun RestingContent(
    state: WorkoutUiState.Resting,
    onRestComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.exerciseName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Set ${state.completedSet} of ${state.totalSets} complete",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        RestTimerBar(
            totalSeconds = state.restSeconds,
            onTimerFinished = onRestComplete
        )
    }
}

/**
 * Content displayed when the workout is complete.
 */
@Composable
private fun CompleteContent(
    durationMinutes: Int,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Workout Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Duration: $durationMinutes minutes",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onFinish) {
            Text("Done")
        }
    }
}

// End of WorkoutScreen.kt — Guided exercise-by-exercise workout execution with set logging.
