package com.athletiq.app.data.seed;

import android.content.Context;
import com.athletiq.app.data.repository.ProgramRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SeedDataProvider_Factory implements Factory<SeedDataProvider> {
  private final Provider<Context> contextProvider;

  private final Provider<ProgramRepository> programRepositoryProvider;

  public SeedDataProvider_Factory(Provider<Context> contextProvider,
      Provider<ProgramRepository> programRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.programRepositoryProvider = programRepositoryProvider;
  }

  @Override
  public SeedDataProvider get() {
    return newInstance(contextProvider.get(), programRepositoryProvider.get());
  }

  public static SeedDataProvider_Factory create(Provider<Context> contextProvider,
      Provider<ProgramRepository> programRepositoryProvider) {
    return new SeedDataProvider_Factory(contextProvider, programRepositoryProvider);
  }

  public static SeedDataProvider newInstance(Context context, ProgramRepository programRepository) {
    return new SeedDataProvider(context, programRepository);
  }
}
