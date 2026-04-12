package com.athletiq.app.domain.usecase;

import com.athletiq.app.data.repository.EnrollmentRepository;
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
public final class StartProgramUseCase_Factory implements Factory<StartProgramUseCase> {
  private final Provider<EnrollmentRepository> enrollmentRepositoryProvider;

  public StartProgramUseCase_Factory(Provider<EnrollmentRepository> enrollmentRepositoryProvider) {
    this.enrollmentRepositoryProvider = enrollmentRepositoryProvider;
  }

  @Override
  public StartProgramUseCase get() {
    return newInstance(enrollmentRepositoryProvider.get());
  }

  public static StartProgramUseCase_Factory create(
      Provider<EnrollmentRepository> enrollmentRepositoryProvider) {
    return new StartProgramUseCase_Factory(enrollmentRepositoryProvider);
  }

  public static StartProgramUseCase newInstance(EnrollmentRepository enrollmentRepository) {
    return new StartProgramUseCase(enrollmentRepository);
  }
}
