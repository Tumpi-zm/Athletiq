package com.athletiq.app.data.repository;

import com.athletiq.app.data.local.dao.ProgramDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ProgramRepository_Factory implements Factory<ProgramRepository> {
  private final Provider<ProgramDao> programDaoProvider;

  public ProgramRepository_Factory(Provider<ProgramDao> programDaoProvider) {
    this.programDaoProvider = programDaoProvider;
  }

  @Override
  public ProgramRepository get() {
    return newInstance(programDaoProvider.get());
  }

  public static ProgramRepository_Factory create(Provider<ProgramDao> programDaoProvider) {
    return new ProgramRepository_Factory(programDaoProvider);
  }

  public static ProgramRepository newInstance(ProgramDao programDao) {
    return new ProgramRepository(programDao);
  }
}
