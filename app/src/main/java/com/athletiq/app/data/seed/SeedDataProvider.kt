package com.athletiq.app.data.seed

import android.content.Context
import com.athletiq.app.data.local.entity.BlockEntity
import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.ExerciseEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.local.entity.SessionEntity
import com.athletiq.app.data.local.entity.WeekEntity
import com.athletiq.app.data.repository.ProgramRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeds the Room database with the bundled 90-day training program on first launch.
 *
 * The program is split into one file per week (`week1_seed.json` … `week12_seed.json`)
 * to keep each file small and avoid network/parsing issues.
 *
 * The first file (`week1_seed.json`) carries a `program` header used to create the
 * single [ProgramEntity]. Every file contains exactly one [SeedWeek] wrapped inside
 * a [SeedWeekFile].
 *
 * Called from [MainActivity] when [ProgramRepository.needsSeeding] returns true.
 *
 * @param context Application context for asset access.
 * @param programRepository Repository for insert operations.
 */
@Singleton
class SeedDataProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val programRepository: ProgramRepository
) {

    private val json = Json { ignoreUnknownKeys = true }

    /** Asset file names in insertion order (one per week). */
    private val seedFiles = (1..12).map { "week${it}_seed.json" }

    /**
     * Reads the seed JSON files from assets and inserts all entities into the database.
     *
     * This method is idempotent — it should only be called after checking
     * [ProgramRepository.needsSeeding].
     */
    suspend fun seedDatabase() {
        var programId: Long = -1

        for (fileName in seedFiles) {
            val fileExists = try {
                context.assets.open(fileName).close()
                true
            } catch (_: Exception) {
                false
            }
            if (!fileExists) continue

            val jsonString = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }

            val seedWeekFile = json.decodeFromString<SeedWeekFile>(jsonString)

            // Create the program entity from the first file only.
            if (programId == -1L && seedWeekFile.program != null) {
                programId = programRepository.insertProgram(
                    ProgramEntity(
                        name = seedWeekFile.program.name,
                        description = seedWeekFile.program.description,
                        durationWeeks = seedWeekFile.program.durationWeeks,
                        tags = seedWeekFile.program.tags
                    )
                )
            }

            if (programId == -1L) continue

            insertWeek(programId, seedWeekFile.week)
        }
    }

    /**
     * Inserts a single week and its full hierarchy (days → sessions → blocks → exercises).
     */
    private suspend fun insertWeek(programId: Long, seedWeek: SeedWeek) {
        val weekId = programRepository.insertWeek(
            WeekEntity(
                programId = programId,
                weekNumber = seedWeek.weekNumber,
                focus = seedWeek.focusLabel
            )
        )

        seedWeek.days.forEach { seedDay ->
            val dayId = programRepository.insertDay(
                DayEntity(
                    weekId = weekId,
                    dayOfWeek = seedDay.dayOfWeek,
                    name = if (seedDay.isRestDay) "Rest Day" else seedDay.sessions.firstOrNull()?.name ?: "",
                    isRestDay = seedDay.isRestDay,
                    restDayNotes = seedDay.restDayNotes
                )
            )

            seedDay.sessions.forEach { seedSession ->
                val sessionId = programRepository.insertSession(
                    SessionEntity(
                        dayId = dayId,
                        name = seedSession.name,
                        orderIndex = seedSession.orderIndex
                    )
                )

                seedSession.blocks.forEach { seedBlock ->
                    val blockId = programRepository.insertBlock(
                        BlockEntity(
                            sessionId = sessionId,
                            name = seedBlock.label ?: "",
                            blockType = seedBlock.blockType,
                            timerSeconds = seedBlock.timerSeconds,
                            rounds = seedBlock.rounds,
                            orderIndex = seedBlock.orderIndex
                        )
                    )

                    seedBlock.exercises.forEach { seedExercise ->
                        programRepository.insertExercise(
                            ExerciseEntity(
                                blockId = blockId,
                                name = seedExercise.name,
                                targetMuscles = seedExercise.targetMuscles.joinToString(", "),
                                sets = seedExercise.sets,
                                reps = seedExercise.reps,
                                restSeconds = seedExercise.restSeconds ?: 0,
                                tempo = seedExercise.tempo,
                                rpe = seedExercise.rpe?.toInt(),
                                notes = seedExercise.notes,
                                orderIndex = seedExercise.orderIndex
                            )
                        )
                    }
                }
            }
        }
    }
}

// ── Seed Data Models ─────────────────────────────────────────────────────────

/**
 * Top-level model for each weekly seed file.
 *
 * The first file (week1) includes a [program] header; subsequent files may omit it.
 */
@Serializable
data class SeedWeekFile(
    val program: SeedProgramHeader? = null,
    val week: SeedWeek
)

@Serializable
data class SeedProgramHeader(
    val name: String,
    val description: String,
    val durationWeeks: Int,
    val tags: List<String>
)

@Serializable
data class SeedWeek(
    val weekNumber: Int,
    val focusLabel: String? = null,
    val days: List<SeedDay>
)

@Serializable
data class SeedDay(
    val dayOfWeek: Int,
    val isRestDay: Boolean = false,
    val restDayNotes: String? = null,
    val sessions: List<SeedSession> = emptyList()
)

@Serializable
data class SeedSession(
    val name: String,
    val orderIndex: Int = 0,
    val blocks: List<SeedBlock>
)

@Serializable
data class SeedBlock(
    val blockType: String = "STANDARD",
    val label: String? = null,
    val timerSeconds: Int? = null,
    val rounds: Int? = null,
    val orderIndex: Int = 0,
    val exercises: List<SeedExercise>
)

@Serializable
data class SeedExercise(
    val name: String,
    val targetMuscles: List<String> = emptyList(),
    val sets: Int,
    val reps: String,
    val restSeconds: Int? = null,
    val tempo: String? = null,
    val rpe: Float? = null,
    val notes: String? = null,
    val orderIndex: Int = 0
)

// End of SeedDataProvider.kt — Asset-based JSON parser for initial program seeding.
