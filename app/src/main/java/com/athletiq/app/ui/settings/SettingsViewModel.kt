package com.athletiq.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 *
 * **Screen:** [SettingsScreen]
 *
 * **State managed:**
 * - [settingsState]: Current settings values (units, rest timer, notifications).
 *
 * **User actions handled:**
 * - Toggle between kg and lbs.
 * - Change default rest timer duration.
 * - Toggle daily notifications.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    /**
     * Toggles the weight unit between kg and lbs.
     */
    fun toggleWeightUnit() {
        _settingsState.value = _settingsState.value.copy(
            useKg = !_settingsState.value.useKg
        )
        // TODO("Persist setting to DataStore in production")
    }

    /**
     * Updates the default rest timer duration.
     *
     * @param seconds New default rest duration in seconds.
     */
    fun updateDefaultRestSeconds(seconds: Int) {
        _settingsState.value = _settingsState.value.copy(
            defaultRestSeconds = seconds
        )
        // TODO("Persist setting to DataStore in production")
    }

    /**
     * Toggles daily training reminder notifications.
     */
    fun toggleNotifications() {
        _settingsState.value = _settingsState.value.copy(
            notificationsEnabled = !_settingsState.value.notificationsEnabled
        )
        // TODO("Persist setting to DataStore and schedule/cancel notification in production")
    }
}

/**
 * Data class representing the current settings state.
 */
data class SettingsState(
    /** Whether to display weights in kilograms (true) or pounds (false). */
    val useKg: Boolean = true,

    /** Default rest timer duration in seconds, used when exercises don't specify their own. */
    val defaultRestSeconds: Int = 90,

    /** Whether daily training reminder notifications are enabled. */
    val notificationsEnabled: Boolean = false
)

// End of SettingsViewModel.kt — ViewModel for user preferences management.
