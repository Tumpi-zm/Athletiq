package com.athletiq.app.di;

import com.athletiq.app.data.local.AthletiqDatabase;
import com.athletiq.app.data.local.dao.EnrollmentDao;
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
public final class DatabaseModule_ProvideEnrollmentDaoFactory implements Factory<EnrollmentDao> {
  private final Provider<AthletiqDatabase> databaseProvider;

  public DatabaseModule_ProvideEnrollmentDaoFactory(Provider<AthletiqDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public EnrollmentDao get() {
    return provideEnrollmentDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideEnrollmentDaoFactory create(
      Provider<AthletiqDatabase> databaseProvider) {
    return new DatabaseModule_ProvideEnrollmentDaoFactory(databaseProvider);
  }

  public static EnrollmentDao provideEnrollmentDao(AthletiqDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEnrollmentDao(database));
  }
}
