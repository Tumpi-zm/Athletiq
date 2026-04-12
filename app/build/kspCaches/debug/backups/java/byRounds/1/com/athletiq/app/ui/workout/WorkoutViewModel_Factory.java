package com.athletiq.app.ui.workout;

import com.athletiq.app.data.repository.EnrollmentRepository;
import com.athletiq.app.data.repository.ProgramRepository;
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
public final class WorkoutViewModel_Factory implements Factory<WorkoutViewModel> {
  private final Provider<ProgramRepository> programRepositoryProvider;

  private final Provider<WorkoutLogRepository> workoutLogRepositoryProvider;

  private final Provider<EnrollmentRepository> enrollmentRepositoryProvider;

  public WorkoutViewModel_Factory(Provider<ProgramRepository> programRepositoryProvider,
      Provider<WorkoutLogRepository> workoutLogRepositoryProvider,
      Provider<EnrollmentRepository> enrollmentRepositoryProvider) {
    this.programRepositoryProvider = programRepositoryProvider;
    this.workoutLogRepositoryProvider = workoutLogRepositoryProvider;
    this.enrollmentRepositoryProvider = enrollmentRepositoryProvider;
  }

  @Override
  public WorkoutViewModel get() {
    return newInstance(programRepositoryProvider.get(), workoutLogRepositoryProvider.get(), enrollmentRepositoryProvider.get());
  }

  public static WorkoutViewModel_Factory create(
      Provider<ProgramRepository> programRepositoryProvider,
      Provider<WorkoutLogRepository> workoutLogRepositoryProvider,
      Provider<EnrollmentRepository> enrollmentRepositoryProvider) {
    return new WorkoutViewModel_Factory(programRepositoryProvider, workoutLogRepositoryProvider, enrollmentRepositoryProvider);
  }

  public static WorkoutViewModel newInstance(ProgramRepository programRepository,
      WorkoutLogRepository workoutLogRepository, EnrollmentRepository enrollmentRepository) {
    return new WorkoutViewModel(programRepository, workoutLogRepository, enrollmentRepository);
  }
}
