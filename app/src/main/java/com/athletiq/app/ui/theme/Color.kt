package com.athletiq.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Athletiq color palette.
 *
 * Primary colors use a bold, energetic teal/cyan — evoking athletic performance and vitality.
 * Secondary colors use warm amber tones for accents (timers, progress, CTAs).
 * Dark theme defaults are designed for gym environments with low lighting.
 */

// ── Primary ────────────────────────────────────────────────────────────────────
val AthletiqPrimary = Color(0xFF00BFA5)           // Teal — primary actions, app bar
val AthletiqOnPrimary = Color(0xFFFFFFFF)          // White on teal
val AthletiqPrimaryContainer = Color(0xFF004D40)   // Dark teal — containers in dark theme
val AthletiqOnPrimaryContainer = Color(0xFF70EFDE) // Light teal — text on dark teal container

// ── Secondary ──────────────────────────────────────────────────────────────────
val AthletiqSecondary = Color(0xFFFFAB00)          // Amber — timers, badges, accents
val AthletiqOnSecondary = Color(0xFF1B1B1B)        // Dark on amber
val AthletiqSecondaryContainer = Color(0xFF3E2723) // Dark brown — secondary containers
val AthletiqOnSecondaryContainer = Color(0xFFFFD54F) // Light amber on dark brown

// ── Tertiary ───────────────────────────────────────────────────────────────────
val AthletiqTertiary = Color(0xFF7C4DFF)           // Purple — completed states, badges
val AthletiqOnTertiary = Color(0xFFFFFFFF)

// ── Error ──────────────────────────────────────────────────────────────────────
val AthletiqError = Color(0xFFCF6679)
val AthletiqOnError = Color(0xFF1B1B1B)

// ── Surface / Background (Dark Theme) ──────────────────────────────────────────
val AthletiqBackground = Color(0xFF121212)
val AthletiqOnBackground = Color(0xFFE0E0E0)
val AthletiqSurface = Color(0xFF1E1E1E)
val AthletiqOnSurface = Color(0xFFE0E0E0)
val AthletiqSurfaceVariant = Color(0xFF2C2C2C)
val AthletiqOnSurfaceVariant = Color(0xFFBDBDBD)

// ── Surface / Background (Light Theme) ─────────────────────────────────────────
val AthletiqBackgroundLight = Color(0xFFF5F5F5)
val AthletiqOnBackgroundLight = Color(0xFF1B1B1B)
val AthletiqSurfaceLight = Color(0xFFFFFFFF)
val AthletiqOnSurfaceLight = Color(0xFF1B1B1B)
val AthletiqSurfaceVariantLight = Color(0xFFE8E8E8)
val AthletiqOnSurfaceVariantLight = Color(0xFF616161)

// ── Outline ────────────────────────────────────────────────────────────────────
val AthletiqOutline = Color(0xFF444444)
val AthletiqOutlineLight = Color(0xFFBDBDBD)

// End of Color.kt — Athletiq Material 3 color tokens.
