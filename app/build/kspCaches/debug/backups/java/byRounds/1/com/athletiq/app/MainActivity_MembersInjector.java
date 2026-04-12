package com.athletiq.app;

import com.athletiq.app.data.repository.EnrollmentRepository;
import com.athletiq.app.data.repository.ProgramRepository;
import com.athletiq.app.data.seed.SeedDataProvider;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<EnrollmentRepository> enrollmentRepositoryProvider;

  private final Provider<ProgramRepository> programRepositoryProvider;

  private final Provider<SeedDataProvider> seedDataProvider;

  public MainActivity_MembersInjector(Provider<EnrollmentRepository> enrollmentRepositoryProvider,
      Provider<ProgramRepository> programRepositoryProvider,
      Provider<SeedDataProvider> seedDataProvider) {
    this.enrollmentRepositoryProvider = enrollmentRepositoryProvider;
    this.programRepositoryProvider = programRepositoryProvider;
    this.seedDataProvider = seedDataProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<EnrollmentRepository> enrollmentRepositoryProvider,
      Provider<ProgramRepository> programRepositoryProvider,
      Provider<SeedDataProvider> seedDataProvider) {
    return new MainActivity_MembersInjector(enrollmentRepositoryProvider, programRepositoryProvider, seedDataProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectEnrollmentRepository(instance, enrollmentRepositoryProvider.get());
    injectProgramRepository(instance, programRepositoryProvider.get());
    injectSeedDataProvider(instance, seedDataProvider.get());
  }

  @InjectedFieldSignature("com.athletiq.app.MainActivity.enrollmentRepository")
  public static void injectEnrollmentRepository(MainActivity instance,
      EnrollmentRepository enrollmentRepository) {
    instance.enrollmentRepository = enrollmentRepository;
  }

  @InjectedFieldSignature("com.athletiq.app.MainActivity.programRepository")
  public static void injectProgramRepository(MainActivity instance,
      ProgramRepository programRepository) {
    instance.programRepository = programRepository;
  }

  @InjectedFieldSignature("com.athletiq.app.MainActivity.seedDataProvider")
  public static void injectSeedDataProvider(MainActivity instance,
      SeedDataProvider seedDataProvider) {
    instance.seedDataProvider = seedDataProvider;
  }
}
