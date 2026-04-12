package com.athletiq.app.di;

import com.athletiq.app.data.local.AthletiqDatabase;
import com.athletiq.app.data.local.dao.WorkoutLogDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvideWorkoutLogDaoFactory implements Factory<WorkoutLogDao> {
  private final Provider<AthletiqDatabase> databaseProvider;

  public DatabaseModule_ProvideWorkoutLogDaoFactory(Provider<AthletiqDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WorkoutLogDao get() {
    return provideWorkoutLogDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideWorkoutLogDaoFactory create(
      Provider<AthletiqDatabase> databaseProvider) {
    return new DatabaseModule_ProvideWorkoutLogDaoFactory(databaseProvider);
  }

  public static WorkoutLogDao provideWorkoutLogDao(AthletiqDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWorkoutLogDao(database));
  }
}
