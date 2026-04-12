package com.athletiq.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Card component for tracking individual sets during workout execution.
 *
 * Displays:
 * - Current set number (e.g., "Set 2 of 4").
 * - Weight input field pre-filled with the last-used weight.
 * - Reps input field.
 * - "Complete Set" button that advances to the next set.
 *
 * @param exerciseName The name of the exercise being performed.
 * @param currentSet The 1-indexed current set number.
 * @param totalSets The total number of sets prescribed.
 * @param targetReps The prescribed rep range as a string (e.g., "8-10").
 * @param weightKg The current weight input value as a string.
 * @param repsCompleted The current reps input value as a string.
 * @param onWeightChanged Callback when the weight input changes.
 * @param onRepsChanged Callback when the reps input changes.
 * @param onCompleteSet Callback when the "Complete Set" button is tapped.
 * @param modifier Modifier for the card container.
 */
@Composable
fun SetTrackerCard(
    exerciseName: String,
    currentSet: Int,
    totalSets: Int,
    targetReps: String,
    weightKg: String,
    repsCompleted: String,
    onWeightChanged: (String) -> Unit,
    onRepsChanged: (String) -> Unit,
    onCompleteSet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Exercise name header.
            Text(
                text = exerciseName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Set progress indicator (e.g., "Set 2 of 4 · Target: 8-10 reps").
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Set $currentSet of $totalSets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "  ·  Target: $targetReps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weight and reps input fields side by side.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = weightKg,
                    onValueChange = onWeightChanged,
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = repsCompleted,
                    onValueChange = onRepsChanged,
                    label = { Text("Reps") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Complete set button.
            Button(
                onClick = onCompleteSet,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (currentSet < totalSets) "Complete Set" else "Finish Exercise",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

// End of SetTrackerCard.kt — Per-set tracking card with weight/reps input.
