package com.athletiq.app.domain.model

/**
 * Represents the lifecycle status of a user's enrollment in a training program.
 *
 * Programs follow a strict state machine with only two valid transitions:
 * - [Active] → [Completed] (all training days finished)
 * - [Active] → [Abandoned] (user chose to stop early)
 *
 * Once a program is [Completed] or [Abandoned], it cannot be reactivated.
 * The user must create a new enrollment to restart the same program.
 *
 * Stored in Room as a String via [EnrollmentEntity.status].
 */
enum class EnrollmentStatus {
    /** The program is currently in progress. At most one enrollment may be Active at any time. */
    ACTIVE,

    /** All training days in the program have been completed successfully. Terminal state. */
    COMPLETED,

    /** The user chose to stop the program before completion. Terminal state. */
    ABANDONED;

    companion object {
        /**
         * Converts a stored string value to the corresponding [EnrollmentStatus].
         *
         * @param value The string representation from Room storage.
         * @return The matching enum value.
         * @throws IllegalArgumentException if the value doesn't match any status.
         */
        fun fromString(value: String): EnrollmentStatus = valueOf(value.uppercase())
    }
}

// End of EnrollmentStatus.kt — Enum modeling the valid states of a program enrollment.
