package com.athletiq.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Athletiq.
 *
 * Annotated with [@HiltAndroidApp] to trigger Hilt's code generation and serve as
 * the root dependency container for the entire app.
 */
@HiltAndroidApp
class AthletiqApp : Application()

// End of AthletiqApp.kt — Hilt application entry point.
