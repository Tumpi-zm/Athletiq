package com.athletiq.app.domain.usecase;

import com.athletiq.app.data.repository.ProgramRepository;
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
public final class GetTodaySessionUseCase_Factory implements Factory<GetTodaySessionUseCase> {
  private final Provider<ProgramRepository> programRepositoryProvider;

  public GetTodaySessionUseCase_Factory(Provider<ProgramRepository> programRepositoryProvider) {
    this.programRepositoryProvider = programRepositoryProvider;
  }

  @Override
  public GetTodaySessionUseCase get() {
    return newInstance(programRepositoryProvider.get());
  }

  public static GetTodaySessionUseCase_Factory create(
      Provider<ProgramRepository> programRepositoryProvider) {
    return new GetTodaySessionUseCase_Factory(programRepositoryProvider);
  }

  public static GetTodaySessionUseCase newInstance(ProgramRepository programRepository) {
    return new GetTodaySessionUseCase(programRepository);
  }
}
