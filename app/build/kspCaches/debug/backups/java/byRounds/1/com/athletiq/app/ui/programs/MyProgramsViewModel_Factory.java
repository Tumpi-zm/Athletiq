package com.athletiq.app.ui.programs;

import com.athletiq.app.data.repository.EnrollmentRepository;
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
public final class MyProgramsViewModel_Factory implements Factory<MyProgramsViewModel> {
  private final Provider<EnrollmentRepository> enrollmentRepositoryProvider;

  private final Provider<ProgramRepository> programRepositoryProvider;

  public MyProgramsViewModel_Factory(Provider<EnrollmentRepository> enrollmentRepositoryProvider,
      Provider<ProgramRepository> programRepositoryProvider) {
    this.enrollmentRepositoryProvider = enrollmentRepositoryProvider;
    this.programRepositoryProvider = programRepositoryProvider;
  }

  @Override
  public MyProgramsViewModel get() {
    return newInstance(enrollmentRepositoryProvider.get(), programRepositoryProvider.get());
  }

  public static MyProgramsViewModel_Factory create(
      Provider<EnrollmentRepository> enrollmentRepositoryProvider,
      Provider<ProgramRepository> programRepositoryProvider) {
    return new MyProgramsViewModel_Factory(enrollmentRepositoryProvider, programRepositoryProvider);
  }

  public static MyProgramsViewModel newInstance(EnrollmentRepository enrollmentRepository,
      ProgramRepository programRepository) {
    return new MyProgramsViewModel(enrollmentRepository, programRepository);
  }
}
