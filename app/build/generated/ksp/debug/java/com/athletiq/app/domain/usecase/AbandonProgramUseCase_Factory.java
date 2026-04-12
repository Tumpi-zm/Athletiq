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
public final class AbandonProgramUseCase_Factory implements Factory<AbandonProgramUseCase> {
  private final Provider<EnrollmentRepository> enrollmentRepositoryProvider;

  public AbandonProgramUseCase_Factory(
      Provider<EnrollmentRepository> enrollmentRepositoryProvider) {
    this.enrollmentRepositoryProvider = enrollmentRepositoryProvider;
  }

  @Override
  public AbandonProgramUseCase get() {
    return newInstance(enrollmentRepositoryProvider.get());
  }

  public static AbandonProgramUseCase_Factory create(
      Provider<EnrollmentRepository> enrollmentRepositoryProvider) {
    return new AbandonProgramUseCase_Factory(enrollmentRepositoryProvider);
  }

  public static AbandonProgramUseCase newInstance(EnrollmentRepository enrollmentRepository) {
    return new AbandonProgramUseCase(enrollmentRepository);
  }
}
