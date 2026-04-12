package com.athletiq.app.data.repository;

import com.athletiq.app.data.local.dao.WorkoutLogDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class WorkoutLogRepository_Factory implements Factory<WorkoutLogRepository> {
  private final Provider<WorkoutLogDao> workoutLogDaoProvider;

  public WorkoutLogRepository_Factory(Provider<WorkoutLogDao> workoutLogDaoProvider) {
    this.workoutLogDaoProvider = workoutLogDaoProvider;
  }

  @Override
  public WorkoutLogRepository get() {
    return newInstance(workoutLogDaoProvider.get());
  }

  public static WorkoutLogRepository_Factory create(Provider<WorkoutLogDao> workoutLogDaoProvider) {
    return new WorkoutLogRepository_Factory(workoutLogDaoProvider);
  }

  public static WorkoutLogRepository newInstance(WorkoutLogDao workoutLogDao) {
    return new WorkoutLogRepository(workoutLogDao);
  }
}
