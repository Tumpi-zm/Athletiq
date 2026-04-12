package com.athletiq.app.ui.today;

import com.athletiq.app.data.repository.EnrollmentRepository;
import com.athletiq.app.data.repository.ProgramRepository;
import com.athletiq.app.domain.usecase.AbandonProgramUseCase;
import com.athletiq.app.domain.usecase.GetTodaySessionUseCase;
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
public final class TodayViewModel_Factory implements Factory<TodayViewModel> {
  private final Provider<EnrollmentRepository> enrollmentRepositoryProvider;

  private final Provider<ProgramRepository> programRepositoryProvider;

  private final Provider<GetTodaySessionUseCase> getTodaySessionUseCaseProvider;

  private final Provider<AbandonProgramUseCase> abandonProgramUseCaseProvider;

  public TodayViewModel_Factory(Provider<EnrollmentRepository> enrollmentRepositoryProvider,
      Provider<ProgramRepository> programRepositoryProvider,
      Provider<GetTodaySessionUseCase> getTodaySessionUseCaseProvider,
      Provider<AbandonProgramUseCase> abandonProgramUseCaseProvider) {
    this.enrollmentRepositoryProvider = enrollmentRepositoryProvider;
    this.programRepositoryProvider = programRepositoryProvider;
    this.getTodaySessionUseCaseProvider = getTodaySessionUseCaseProvider;
    this.abandonProgramUseCaseProvider = abandonProgramUseCaseProvider;
  }

  @Override
  public TodayViewModel get() {
    return newInstance(enrollmentRepositoryProvider.get(), programRepositoryProvider.get(), getTodaySessionUseCaseProvider.get(), abandonProgramUseCaseProvider.get());
  }

  public static TodayViewModel_Factory create(
      Provider<EnrollmentRepository> enrollmentRepositoryProvider,
      Provider<ProgramRepository> programRepositoryProvider,
      Provider<GetTodaySessionUseCase> getTodaySessionUseCaseProvider,
      Provider<AbandonProgramUseCase> abandonProgramUseCaseProvider) {
    return new TodayViewModel_Factory(enrollmentRepositoryProvider, programRepositoryProvider, getTodaySessionUseCaseProvider, abandonProgramUseCaseProvider);
  }

  public static TodayViewModel newInstance(EnrollmentRepository enrollmentRepository,
      ProgramRepository programRepository, GetTodaySessionUseCase getTodaySessionUseCase,
      AbandonProgramUseCase abandonProgramUseCase) {
    return new TodayViewModel(enrollmentRepository, programRepository, getTodaySessionUseCase, abandonProgramUseCase);
  }
}
