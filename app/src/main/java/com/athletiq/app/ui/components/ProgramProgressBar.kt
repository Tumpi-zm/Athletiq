package com.athletiq.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Visual progress bar showing how far along a user is in their active program.
 *
 * Displays a percentage-based progress bar with a text label
 * (e.g., "34 of 90 days completed").
 *
 * @param completedDays Number of training days the user has completed.
 * @param totalDays Total training days in the program.
 * @param modifier Modifier for the container.
 */
@Composable
fun ProgramProgressBar(
    completedDays: Int,
    totalDays: Int,
    modifier: Modifier = Modifier
) {
    val fraction = if (totalDays > 0) completedDays.toFloat() / totalDays else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 600),
        label = "program_progress"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$completedDays of $totalDays days completed",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

// End of ProgramProgressBar.kt — Animated progress bar for program completion tracking.
