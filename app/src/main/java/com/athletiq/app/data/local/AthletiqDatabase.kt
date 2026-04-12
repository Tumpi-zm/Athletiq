package com.athletiq.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.athletiq.app.data.local.dao.EnrollmentDao
import com.athletiq.app.data.local.dao.ProgramDao
import com.athletiq.app.data.local.dao.WorkoutLogDao
import com.athletiq.app.data.local.entity.BlockEntity
import com.athletiq.app.data.local.entity.DayEntity
import com.athletiq.app.data.local.entity.EnrollmentEntity
import com.athletiq.app.data.local.entity.ExerciseEntity
import com.athletiq.app.data.local.entity.ExerciseLogEntity
import com.athletiq.app.data.local.entity.ProgramEntity
import com.athletiq.app.data.local.entity.SessionEntity
import com.athletiq.app.data.local.entity.WeekEntity
import com.athletiq.app.data.local.entity.WorkoutLogEntity

/**
 * The main Room database for the Athletiq app.
 *
 * Contains all entities for program templates, user enrollments, and workout logging.
 * The database is created as a singleton via Hilt DI ([DatabaseModule]) and uses
 * [Converters] for non-primitive type persistence.
 *
 * **Seeding:** On first run, [SeedDataProvider] populates the program template tables
 * (programs, weeks, days, sessions, blocks, exercises) from a bundled JSON asset.
 * User data tables (enrollments, workout_logs, exercise_logs) start empty.
 *
 * @see com.athletiq.app.di.DatabaseModule for the Hilt provider
 * @see com.athletiq.app.data.seed.SeedDataProvider for first-run data seeding
 */
@Database(
    entities = [
        ProgramEntity::class,
        WeekEntity::class,
        DayEntity::class,
        SessionEntity::class,
        BlockEntity::class,
        ExerciseEntity::class,
        EnrollmentEntity::class,
        WorkoutLogEntity::class,
        ExerciseLogEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AthletiqDatabase : RoomDatabase() {

    /**
     * DAO for querying program templates (programs, weeks, days, sessions, blocks, exercises).
     */
    abstract fun programDao(): ProgramDao

    /**
     * DAO for managing user enrollments and program lifecycle transitions.
     */
    abstract fun enrollmentDao(): EnrollmentDao

    /**
     * DAO for inserting and querying workout logs and exercise performance history.
     */
    abstract fun workoutLogDao(): WorkoutLogDao

    companion object {
        /** Database file name on device storage. */
        const val DATABASE_NAME = "athletiq_database"
    }
}

// End of AthletiqDatabase.kt — Room database definition with all entities and DAOs.
