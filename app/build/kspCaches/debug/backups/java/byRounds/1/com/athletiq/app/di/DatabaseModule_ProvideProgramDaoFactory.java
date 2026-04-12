package com.athletiq.app.di;

import com.athletiq.app.data.local.AthletiqDatabase;
import com.athletiq.app.data.local.dao.ProgramDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideProgramDaoFactory implements Factory<ProgramDao> {
  private final Provider<AthletiqDatabase> databaseProvider;

  public DatabaseModule_ProvideProgramDaoFactory(Provider<AthletiqDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ProgramDao get() {
    return provideProgramDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideProgramDaoFactory create(
      Provider<AthletiqDatabase> databaseProvider) {
    return new DatabaseModule_ProvideProgramDaoFactory(databaseProvider);
  }

  public static ProgramDao provideProgramDao(AthletiqDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgramDao(database));
  }
}
