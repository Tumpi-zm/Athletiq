package com.athletiq.app.domain.model

import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.ProgramEntity

/**
 * Domain model representing a program catalog entry with optional enrollment info.
 *
 * Combines the static program template data with the user's enrollment state (if any)
 * for rich display in the catalog and "My Programs" screens.
 */
data class ProgramOverview(
    /** The program template (name, description, duration, tags). */
    val program: ProgramEntity,

    /** Total number of training (non-rest) days in the program. */
    val totalTrainingDays: Int,

    /** The user's most recent enrollment in this program, or null if never enrolled. */
    val enrollment: EnrollmentEntity? = null
)

// End of ProgramOverview.kt — Domain model combining program info with enrollment state.
