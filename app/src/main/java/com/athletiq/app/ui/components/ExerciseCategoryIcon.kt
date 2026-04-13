package com.athletiq.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Displays a circular icon badge representing the exercise category.
 *
 * Determines category from either the exercise name or target muscles string.
 */
@Composable
fun ExerciseCategoryIcon(
    exerciseName: String,
    targetMuscles: String?,
    modifier: Modifier = Modifier
) {
    val (icon, tint, bg) = resolveCategory(exerciseName, targetMuscles)

    Box(
        modifier = modifier
            .size(40.dp)
            .background(bg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

private data class CategoryStyle(
    val icon: ImageVector,
    val tint: Color,
    val background: Color
)

// ── Palette ────────────────────────────────────────────────────────────────────
// Muted pastel tones that work on both light and dark themes.
private val StrengthColor = Color(0xFF1565C0)    // Blue
private val StrengthBg    = Color(0xFFBBDEFB)
private val CardioColor   = Color(0xFFC62828)     // Red
private val CardioBg      = Color(0xFFFFCDD2)
private val CoreColor     = Color(0xFFE65100)     // Orange
private val CoreBg        = Color(0xFFFFE0B2)
private val PlyoColor     = Color(0xFF6A1B9A)     // Purple
private val PlyoBg        = Color(0xFFE1BEE7)
private val MobilityColor = Color(0xFF2E7D32)     // Green
private val MobilityBg    = Color(0xFFC8E6C9)
private val SportColor    = Color(0xFF00838F)     // Teal
private val SportBg       = Color(0xFFB2EBF2)

private fun resolveCategory(name: String, muscles: String?): CategoryStyle {
    val n = name.lowercase()
    val m = muscles?.lowercase().orEmpty()

    return when {
        // Sport / active recovery.
        n.contains("football") || n.contains("pick-up game") ->
            CategoryStyle(Icons.Filled.SportsSoccer, SportColor, SportBg)

        // Stretching / mobility / foam rolling / recovery.
        n.contains("stretch") || n.contains("foam roll") || n.contains("lacrosse ball") ||
        n.contains("child's pose") || n.contains("cat-cow") || n.contains("wall slide") ||
        n.contains("couch stretch") || n.contains("pigeon") || n.contains("spinal twist") ||
        n.contains("cool-down") || n.contains("90/90") || n.contains("dislocate") ||
        n.contains("pull-apart") ->
            CategoryStyle(Icons.Filled.SelfImprovement, MobilityColor, MobilityBg)

        // Cardio / conditioning machines.
        n.contains("assault bike") || n.contains("rowing") || n.contains("ski erg") ||
        n.contains("sled push") || n.contains("battle rope") || n.contains("jog") ||
        n.contains("burpee") ->
            CategoryStyle(Icons.AutoMirrored.Filled.DirectionsRun, CardioColor, CardioBg)

        // Plyometrics / explosive jumps.
        n.contains("jump") || n.contains("bound") || n.contains("tuck") ||
        n.contains("depth") || n.contains("broad") || n.contains("clapping push") ||
        n.contains("plyometric") ->
            CategoryStyle(Icons.Filled.SportsMartialArts, PlyoColor, PlyoBg)

        // Core-focused exercises.
        n.contains("plank") || n.contains("dead bug") || n.contains("crunch") ||
        n.contains("russian twist") || n.contains("pallof") || n.contains("woodchop") ||
        n.contains("l-sit") || n.contains("dragon flag") || n.contains("ab wheel") ||
        n.contains("rollout") || n.contains("toe tap") || n.contains("bird dog") ||
        n.contains("hanging knee") || n.contains("hanging leg") || n.contains("toes-to-bar") ||
        m.contains("abs") || m.contains("obliques") || m == "core" ->
            CategoryStyle(Icons.Filled.FitnessCenter, CoreColor, CoreBg)

        // Default — strength / weightlifting.
        else ->
            CategoryStyle(Icons.Filled.FitnessCenter, StrengthColor, StrengthBg)
    }
}
