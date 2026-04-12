package com.athletiq.app.ui.catalog;

import com.athletiq.app.data.repository.ProgramRepository;
import com.athletiq.app.domain.usecase.StartProgramUseCase;
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
public final class CatalogViewModel_Factory implements Factory<CatalogViewModel> {
  private final Provider<ProgramRepository> programRepositoryProvider;

  private final Provider<StartProgramUseCase> startProgramUseCaseProvider;

  public CatalogViewModel_Factory(Provider<ProgramRepository> programRepositoryProvider,
      Provider<StartProgramUseCase> startProgramUseCaseProvider) {
    this.programRepositoryProvider = programRepositoryProvider;
    this.startProgramUseCaseProvider = startProgramUseCaseProvider;
  }

  @Override
  public CatalogViewModel get() {
    return newInstance(programRepositoryProvider.get(), startProgramUseCaseProvider.get());
  }

  public static CatalogViewModel_Factory create(
      Provider<ProgramRepository> programRepositoryProvider,
      Provider<StartProgramUseCase> startProgramUseCaseProvider) {
    return new CatalogViewModel_Factory(programRepositoryProvider, startProgramUseCaseProvider);
  }

  public static CatalogViewModel newInstance(ProgramRepository programRepository,
      StartProgramUseCase startProgramUseCase) {
    return new CatalogViewModel(programRepository, startProgramUseCase);
  }
}
