package com.athletiq.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Countdown rest timer displayed between sets during workout execution.
 *
 * Features:
 * - Visual progress bar that depletes as time passes.
 * - "Skip" button to end rest early and proceed to the next set.
 * - "+30s" button to extend rest when more recovery is needed.
 * - Large countdown display readable at a glance in gym conditions.
 *
 * The timer calls [onTimerFinished] when it reaches zero or is skipped.
 *
 * @param totalSeconds The initial rest duration in seconds.
 * @param onTimerFinished Callback when rest period ends (naturally or via skip).
 * @param modifier Modifier for the timer container.
 */
@Composable
fun RestTimerBar(
    totalSeconds: Int,
    onTimerFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var remainingSeconds by remember(totalSeconds) { mutableIntStateOf(totalSeconds) }
    var maxSeconds by remember(totalSeconds) { mutableIntStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(true) }

    // Countdown tick — runs every second while the timer is active.
    LaunchedEffect(remainingSeconds, isRunning) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        } else if (remainingSeconds <= 0 && isRunning) {
            isRunning = false
            onTimerFinished()
        }
    }

    // Animate the progress bar for smooth depletion.
    val progress by animateFloatAsState(
        targetValue = if (maxSeconds > 0) remainingSeconds.toFloat() / maxSeconds else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "rest_timer_progress"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "REST",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Large countdown display — minutes:seconds format.
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            Text(
                text = String.format("%d:%02d", minutes, seconds),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = if (remainingSeconds <= 10) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar.
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = if (remainingSeconds <= 10) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                trackColor = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons: Skip and +30s.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = {
                        isRunning = false
                        onTimerFinished()
                    }
                ) {
                    Text("Skip")
                }

                Spacer(modifier = Modifier.width(16.dp))

                ElevatedButton(
                    onClick = {
                        val extensionSeconds = 30
                        remainingSeconds += extensionSeconds
                        maxSeconds += extensionSeconds
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("+30s")
                }
            }
        }
    }
}

// End of RestTimerBar.kt — Countdown timer composable with skip and extend functionality.
