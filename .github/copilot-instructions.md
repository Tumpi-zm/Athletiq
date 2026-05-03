# Athletiq — Copilot Instructions

Athletiq is a single-module Android fitness app (Jetpack Compose + Material 3) that guides users through a structured 90-day training program. The entire program is bundled as seed data; the app is offline-first with no backend.

## Build Commands

```bash
# Debug build
./gradlew assembleDebug          # Windows: gradlew.bat assembleDebug

# Release build (minification enabled)
./gradlew assembleRelease

# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

All dependencies are version-pinned in `gradle/libs.versions.toml` (version catalog). When adding a new dependency, add the version to `[versions]`, the library entry to `[libraries]`, and reference it with `libs.*` in `build.gradle.kts`.

## Architecture

Clean Architecture with three layers inside the single `app` module:

```
data/
  local/entity/       Room entities — plain data classes, no logic
  local/dao/          Room DAOs — suspend funs + Flow queries
  local/              AthletiqDatabase, Converters
  repository/         Repositories — wrap DAOs, @Singleton @Inject constructor
  seed/               SeedDataProvider, ExerciseDescriptions

domain/
  model/              Domain models (data classes, sealed interfaces, enums)
  usecase/            Use cases — single-responsibility, return sealed results

ui/
  {screen}/           One sub-package per screen: FooScreen.kt + FooViewModel.kt
  components/         Reusable Compose components
  navigation/         Routes.kt, AthletiqNavGraph.kt
  theme/              Theme.kt, Color.kt, Type.kt

di/                   Hilt modules (DatabaseModule)
```

### Data Model Hierarchy

**Program template data** (read-only after first-run seeding):
```
ProgramEntity → WeekEntity → DayEntity → SessionEntity → BlockEntity → ExerciseEntity
```

**User data** (mutable):
```
EnrollmentEntity → WorkoutLogEntity → ExerciseLogEntity
```

### Navigation

Type-safe Compose Navigation. All routes are `@Serializable` data objects/classes nested inside `object Routes`. The start destination is always `Routes.Catalog` (determined in `MainActivity`).

Navigation flow:
```
Catalog ──(start program)──> Today ──(start workout)──> Workout
   ↑                            │
   └──(abandon program)─────────┘
                            └──> History
                            └──> MyPrograms
                            └──> Settings
                            └──> ProgramOverview
```

Screen composables never navigate directly — they receive navigation callbacks as lambdas (e.g., `onNavigateBack: () -> Unit`, `onStartWorkout: (sessionId, enrollmentId, dayId) -> Unit`). Routing logic lives exclusively in `AthletiqNavGraph`.

### First-Run Seeding

`MainActivity` calls `SeedDataProvider.seedDatabase()` if `ProgramRepository.needsSeeding()` returns true. The 90-day program is split across 12 JSON asset files (`week1_seed.json` … `week12_seed.json`). `week1_seed.json` carries the `program` header; subsequent files only contain a `week`. See `SeedDataProvider.kt` for the `SeedWeekFile` → `SeedWeek` → `SeedDay` → `SeedSession` → `SeedBlock` → `SeedExercise` model used to parse the JSON.

## Key Conventions

### File endings
Every `.kt` source file ends with a single-line comment:
```kotlin
// End of FileName.kt — one-sentence description of the file's purpose.
```

### ViewModels
All ViewModels use `@HiltViewModel` + `@Inject constructor`. UI state is always exposed as a sealed interface backed by `StateFlow`:
```kotlin
private val _uiState = MutableStateFlow<FooUiState>(FooUiState.Loading)
val uiState: StateFlow<FooUiState> = _uiState.asStateFlow()
```
The sealed UI state interface is defined at the bottom of the ViewModel file.

### Use Cases
Use cases are plain classes injected via `@Inject constructor` (no `@Singleton`). They are invoked with `operator fun invoke(...)` and always return a sealed result type defined in the same file — they do not throw exceptions to the caller:
```kotlin
sealed interface StartProgramResult {
    data class Success(val enrollmentId: Long) : StartProgramResult
    data class ActiveProgramExists(...) : StartProgramResult
}
```

### Room Storage
- `LocalDate` ↔ `Long` (epoch day) via `Converters.fromLocalDate` / `toLocalDate`
- `List<String>` ↔ JSON `String` via `Converters.fromStringList` / `toStringList`
- Enum-like values (`BlockType`, `EnrollmentStatus`) are stored as `String` in entities (not as the enum directly). Use `BlockType.fromString(value)` for safe conversion with a `STANDARD` fallback.

### Single-Active-Enrollment Constraint
Only one `EnrollmentEntity` may have `status = "ACTIVE"` at a time. This constraint is **not** enforced at the database level — it is enforced in `StartProgramUseCase` and `AbandonProgramUseCase`.

### Dependency Injection
All DI is in `di/DatabaseModule`. New repositories follow the `@Singleton` + `@Inject constructor` pattern and do **not** need a separate Hilt `@Provides` entry — Hilt resolves them automatically. Only the `AthletiqDatabase` and its DAOs need explicit `@Provides` entries in `DatabaseModule`.

### Theme
Material 3. Dark mode is the primary design target. Custom colors are all prefixed `Athletiq*` in `Color.kt`. Use `MaterialTheme.colorScheme.*` and `MaterialTheme.typography.*` in Compose — never hardcode colors or text styles.
