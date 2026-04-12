package com.athletiq.app.ui.history;

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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<WorkoutLogRepository> workoutLogRepositoryProvider;

  public HistoryViewModel_Factory(Provider<WorkoutLogRepository> workoutLogRepositoryProvider) {
    this.workoutLogRepositoryProvider = workoutLogRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(workoutLogRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<WorkoutLogRepository> workoutLogRepositoryProvider) {
    return new HistoryViewModel_Factory(workoutLogRepositoryProvider);
  }

  public static HistoryViewModel newInstance(WorkoutLogRepository workoutLogRepository) {
    return new HistoryViewModel(workoutLogRepository);
  }
}
