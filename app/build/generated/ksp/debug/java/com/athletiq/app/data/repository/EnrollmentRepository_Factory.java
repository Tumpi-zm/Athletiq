package com.athletiq.app.data.repository;

import com.athletiq.app.data.local.dao.EnrollmentDao;
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
public final class EnrollmentRepository_Factory implements Factory<EnrollmentRepository> {
  private final Provider<EnrollmentDao> enrollmentDaoProvider;

  public EnrollmentRepository_Factory(Provider<EnrollmentDao> enrollmentDaoProvider) {
    this.enrollmentDaoProvider = enrollmentDaoProvider;
  }

  @Override
  public EnrollmentRepository get() {
    return newInstance(enrollmentDaoProvider.get());
  }

  public static EnrollmentRepository_Factory create(Provider<EnrollmentDao> enrollmentDaoProvider) {
    return new EnrollmentRepository_Factory(enrollmentDaoProvider);
  }

  public static EnrollmentRepository newInstance(EnrollmentDao enrollmentDao) {
    return new EnrollmentRepository(enrollmentDao);
  }
}
