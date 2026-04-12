package com.athletiq.app.domain.model

/**
 * Defines the execution format for a workout block.
 *
 * Each block type determines how the workout screen renders exercises and manages timing:
 * - **STANDARD / SUPERSET** — Set-by-set tracker with rest timers.
 * - **EMOM / AMRAP / FOR_TIME** — Running clock with designated work periods.
 * - **CIRCUIT** — Multi-exercise rounds with shared rest.
 *
 * Stored in Room as a String via [BlockEntity.blockType].
 */
enum class BlockType {
    /** Exercises performed sequentially with rest between sets. The default format. */
    STANDARD,

    /** Exercises paired back-to-back before a shared rest period. */
    SUPERSET,

    /** Multiple exercises cycled through for a prescribed number of rounds. */
    CIRCUIT,

    /** Every Minute On the Minute — start prescribed work at the top of each minute. */
    EMOM,

    /** As Many Rounds/Reps As Possible within a time cap. */
    AMRAP,

    /** Complete the prescribed work as fast as possible. Time is the score. */
    FOR_TIME;

    companion object {
        /**
         * Converts a stored string value to the corresponding [BlockType].
         * Falls back to [STANDARD] for unrecognized values to prevent crashes from bad seed data.
         *
         * @param value The string representation from Room storage.
         * @return The matching enum value, or [STANDARD] as a safe default.
         */
        fun fromString(value: String): BlockType =
            entries.firstOrNull { it.name == value.uppercase() } ?: STANDARD
    }
}

// End of BlockType.kt — Enum defining workout block execution formats.
