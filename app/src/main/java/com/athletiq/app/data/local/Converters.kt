package com.athletiq.app.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

/**
 * Room [TypeConverter] definitions for types that Room cannot persist natively.
 *
 * Handles conversion between:
 * - [LocalDate] ↔ [Long] (epoch day) — Used by enrollment dates, workout log dates.
 * - [List]<[String]> ↔ [String] (JSON) — Used by program tags.
 *
 * These converters are registered globally with the [AthletiqDatabase] to avoid
 * repeating conversion logic in individual DAOs.
 */
class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    // ── LocalDate ↔ Long (epoch day) ───────────────────────────────────────────

    /**
     * Converts a [LocalDate] to its epoch day representation for Room storage.
     *
     * @param date The date to convert, or null.
     * @return The number of days since the epoch (1970-01-01), or null if input is null.
     */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? = date?.toEpochDay()

    /**
     * Converts an epoch day value back to a [LocalDate].
     *
     * @param epochDay The epoch day stored in Room, or null.
     * @return The corresponding [LocalDate], or null if input is null.
     */
    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? = epochDay?.let { LocalDate.ofEpochDay(it) }

    // ── List<String> ↔ JSON String ─────────────────────────────────────────────

    /**
     * Serializes a list of strings to a JSON array string for Room storage.
     *
     * @param tags The list to serialize (e.g., ["CrossFit", "Hyrox"]).
     * @return JSON string representation (e.g., `["CrossFit","Hyrox"]`).
     */
    @TypeConverter
    fun fromStringList(tags: List<String>): String = json.encodeToString(tags)

    /**
     * Deserializes a JSON array string back to a list of strings.
     *
     * @param tagsJson The JSON string from Room storage.
     * @return The deserialized list of strings.
     */
    @TypeConverter
    fun toStringList(tagsJson: String): List<String> = json.decodeFromString(tagsJson)
}

// End of Converters.kt — Room TypeConverters for LocalDate and List<String> persistence.
