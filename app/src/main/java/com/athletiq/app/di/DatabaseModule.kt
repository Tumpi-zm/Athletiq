package com.athletiq.app.di

import android.content.Context
import androidx.room.Room
import com.athletiq.app.data.local.AthletiqDatabase
import com.athletiq.app.data.local.dao.EnrollmentDao
import com.athletiq.app.data.local.dao.ProgramDao
import com.athletiq.app.data.local.dao.WorkoutLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the Room database singleton and all DAO instances.
 *
 * Installed in [SingletonComponent] to ensure a single database instance lives for the
 * entire application lifecycle. DAOs are scoped to the same component since they're
 * lightweight interfaces backed by the single database.
 *
 * @see AthletiqDatabase for the database definition
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the Room database singleton.
     *
     * Built with [fallbackToDestructiveMigration] for MVP — in production, proper
     * migration strategies would be implemented to preserve user data across schema changes.
     *
     * @param context Application context for database file creation.
     * @return The singleton database instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AthletiqDatabase {
        return Room.databaseBuilder(
            context,
            AthletiqDatabase::class.java,
            AthletiqDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Provides the [ProgramDao] for program template queries.
     */
    @Provides
    @Singleton
    fun provideProgramDao(database: AthletiqDatabase): ProgramDao = database.programDao()

    /**
     * Provides the [EnrollmentDao] for enrollment lifecycle management.
     */
    @Provides
    @Singleton
    fun provideEnrollmentDao(database: AthletiqDatabase): EnrollmentDao = database.enrollmentDao()

    /**
     * Provides the [WorkoutLogDao] for workout logging and history queries.
     */
    @Provides
    @Singleton
    fun provideWorkoutLogDao(database: AthletiqDatabase): WorkoutLogDao = database.workoutLogDao()
}

// End of DatabaseModule.kt — Hilt DI module for Room database and DAOs.
