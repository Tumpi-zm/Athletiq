package com.athletiq.app.domain.usecase;

import com.athletiq.app.data.repository.WorkoutLogRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class GetExerciseHistoryUseCase_Factory implements Factory<GetExerciseHistoryUseCase> {
  private final Provider<WorkoutLogRepository> workoutLogRepositoryProvider;

  public GetExerciseHistoryUseCase_Factory(
      Provider<WorkoutLogRepository> workoutLogRepositoryProvider) {
    this.workoutLogRepositoryProvider = workoutLogRepositoryProvider;
  }

  @Override
  public GetExerciseHistoryUseCase get() {
    return newInstance(workoutLogRepositoryProvider.get());
  }

  public static GetExerciseHistoryUseCase_Factory create(
      Provider<WorkoutLogRepository> workoutLogRepositoryProvider) {
    return new GetExerciseHistoryUseCase_Factory(workoutLogRepositoryProvider);
  }

  public static GetExerciseHistoryUseCase newInstance(WorkoutLogRepository workoutLogRepository) {
    return new GetExerciseHistoryUseCase(workoutLogRepository);
  }
}
