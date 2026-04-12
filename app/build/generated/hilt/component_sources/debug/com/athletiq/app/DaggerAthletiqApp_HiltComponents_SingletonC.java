package com.athletiq.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.athletiq.app.data.local.AthletiqDatabase;
import com.athletiq.app.data.local.dao.EnrollmentDao;
import com.athletiq.app.data.local.dao.ProgramDao;
import com.athletiq.app.data.local.dao.WorkoutLogDao;
import com.athletiq.app.data.repository.EnrollmentRepository;
import com.athletiq.app.data.repository.ProgramRepository;
import com.athletiq.app.data.repository.WorkoutLogRepository;
import com.athletiq.app.data.seed.SeedDataProvider;
import com.athletiq.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.athletiq.app.di.DatabaseModule_ProvideEnrollmentDaoFactory;
import com.athletiq.app.di.DatabaseModule_ProvideProgramDaoFactory;
import com.athletiq.app.di.DatabaseModule_ProvideWorkoutLogDaoFactory;
import com.athletiq.app.domain.usecase.AbandonProgramUseCase;
import com.athletiq.app.domain.usecase.GetTodaySessionUseCase;
import com.athletiq.app.domain.usecase.StartProgramUseCase;
import com.athletiq.app.ui.catalog.CatalogViewModel;
import com.athletiq.app.ui.catalog.CatalogViewModel_HiltModules;
import com.athletiq.app.ui.history.HistoryViewModel;
import com.athletiq.app.ui.history.HistoryViewModel_HiltModules;
import com.athletiq.app.ui.overview.ProgramOverviewViewModel;
import com.athletiq.app.ui.overview.ProgramOverviewViewModel_HiltModules;
import com.athletiq.app.ui.programs.MyProgramsViewModel;
import com.athletiq.app.ui.programs.MyProgramsViewModel_HiltModules;
import com.athletiq.app.ui.settings.SettingsViewModel;
import com.athletiq.app.ui.settings.SettingsViewModel_HiltModules;
import com.athletiq.app.ui.today.TodayViewModel;
import com.athletiq.app.ui.today.TodayViewModel_HiltModules;
import com.athletiq.app.ui.workout.WorkoutViewModel;
import com.athletiq.app.ui.workout.WorkoutViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerAthletiqApp_HiltComponents_SingletonC {
  private DaggerAthletiqApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public AthletiqApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements AthletiqApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements AthletiqApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements AthletiqApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements AthletiqApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements AthletiqApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements AthletiqApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements AthletiqApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public AthletiqApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends AthletiqApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends AthletiqApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends AthletiqApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends AthletiqApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(7).put(LazyClassKeyProvider.com_athletiq_app_ui_catalog_CatalogViewModel, CatalogViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_athletiq_app_ui_history_HistoryViewModel, HistoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_athletiq_app_ui_programs_MyProgramsViewModel, MyProgramsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_athletiq_app_ui_overview_ProgramOverviewViewModel, ProgramOverviewViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_athletiq_app_ui_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_athletiq_app_ui_today_TodayViewModel, TodayViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_athletiq_app_ui_workout_WorkoutViewModel, WorkoutViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectEnrollmentRepository(instance, singletonCImpl.enrollmentRepositoryProvider.get());
      MainActivity_MembersInjector.injectProgramRepository(instance, singletonCImpl.programRepositoryProvider.get());
      MainActivity_MembersInjector.injectSeedDataProvider(instance, singletonCImpl.seedDataProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_athletiq_app_ui_settings_SettingsViewModel = "com.athletiq.app.ui.settings.SettingsViewModel";

      static String com_athletiq_app_ui_overview_ProgramOverviewViewModel = "com.athletiq.app.ui.overview.ProgramOverviewViewModel";

      static String com_athletiq_app_ui_today_TodayViewModel = "com.athletiq.app.ui.today.TodayViewModel";

      static String com_athletiq_app_ui_history_HistoryViewModel = "com.athletiq.app.ui.history.HistoryViewModel";

      static String com_athletiq_app_ui_workout_WorkoutViewModel = "com.athletiq.app.ui.workout.WorkoutViewModel";

      static String com_athletiq_app_ui_catalog_CatalogViewModel = "com.athletiq.app.ui.catalog.CatalogViewModel";

      static String com_athletiq_app_ui_programs_MyProgramsViewModel = "com.athletiq.app.ui.programs.MyProgramsViewModel";

      @KeepFieldType
      SettingsViewModel com_athletiq_app_ui_settings_SettingsViewModel2;

      @KeepFieldType
      ProgramOverviewViewModel com_athletiq_app_ui_overview_ProgramOverviewViewModel2;

      @KeepFieldType
      TodayViewModel com_athletiq_app_ui_today_TodayViewModel2;

      @KeepFieldType
      HistoryViewModel com_athletiq_app_ui_history_HistoryViewModel2;

      @KeepFieldType
      WorkoutViewModel com_athletiq_app_ui_workout_WorkoutViewModel2;

      @KeepFieldType
      CatalogViewModel com_athletiq_app_ui_catalog_CatalogViewModel2;

      @KeepFieldType
      MyProgramsViewModel com_athletiq_app_ui_programs_MyProgramsViewModel2;
    }
  }

  private static final class ViewModelCImpl extends AthletiqApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CatalogViewModel> catalogViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<MyProgramsViewModel> myProgramsViewModelProvider;

    private Provider<ProgramOverviewViewModel> programOverviewViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<TodayViewModel> todayViewModelProvider;

    private Provider<WorkoutViewModel> workoutViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private StartProgramUseCase startProgramUseCase() {
      return new StartProgramUseCase(singletonCImpl.enrollmentRepositoryProvider.get());
    }

    private GetTodaySessionUseCase getTodaySessionUseCase() {
      return new GetTodaySessionUseCase(singletonCImpl.programRepositoryProvider.get());
    }

    private AbandonProgramUseCase abandonProgramUseCase() {
      return new AbandonProgramUseCase(singletonCImpl.enrollmentRepositoryProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.catalogViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.myProgramsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.programOverviewViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.todayViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.workoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(7).put(LazyClassKeyProvider.com_athletiq_app_ui_catalog_CatalogViewModel, ((Provider) catalogViewModelProvider)).put(LazyClassKeyProvider.com_athletiq_app_ui_history_HistoryViewModel, ((Provider) historyViewModelProvider)).put(LazyClassKeyProvider.com_athletiq_app_ui_programs_MyProgramsViewModel, ((Provider) myProgramsViewModelProvider)).put(LazyClassKeyProvider.com_athletiq_app_ui_overview_ProgramOverviewViewModel, ((Provider) programOverviewViewModelProvider)).put(LazyClassKeyProvider.com_athletiq_app_ui_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_athletiq_app_ui_today_TodayViewModel, ((Provider) todayViewModelProvider)).put(LazyClassKeyProvider.com_athletiq_app_ui_workout_WorkoutViewModel, ((Provider) workoutViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_athletiq_app_ui_history_HistoryViewModel = "com.athletiq.app.ui.history.HistoryViewModel";

      static String com_athletiq_app_ui_overview_ProgramOverviewViewModel = "com.athletiq.app.ui.overview.ProgramOverviewViewModel";

      static String com_athletiq_app_ui_settings_SettingsViewModel = "com.athletiq.app.ui.settings.SettingsViewModel";

      static String com_athletiq_app_ui_programs_MyProgramsViewModel = "com.athletiq.app.ui.programs.MyProgramsViewModel";

      static String com_athletiq_app_ui_today_TodayViewModel = "com.athletiq.app.ui.today.TodayViewModel";

      static String com_athletiq_app_ui_workout_WorkoutViewModel = "com.athletiq.app.ui.workout.WorkoutViewModel";

      static String com_athletiq_app_ui_catalog_CatalogViewModel = "com.athletiq.app.ui.catalog.CatalogViewModel";

      @KeepFieldType
      HistoryViewModel com_athletiq_app_ui_history_HistoryViewModel2;

      @KeepFieldType
      ProgramOverviewViewModel com_athletiq_app_ui_overview_ProgramOverviewViewModel2;

      @KeepFieldType
      SettingsViewModel com_athletiq_app_ui_settings_SettingsViewModel2;

      @KeepFieldType
      MyProgramsViewModel com_athletiq_app_ui_programs_MyProgramsViewModel2;

      @KeepFieldType
      TodayViewModel com_athletiq_app_ui_today_TodayViewModel2;

      @KeepFieldType
      WorkoutViewModel com_athletiq_app_ui_workout_WorkoutViewModel2;

      @KeepFieldType
      CatalogViewModel com_athletiq_app_ui_catalog_CatalogViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.athletiq.app.ui.catalog.CatalogViewModel 
          return (T) new CatalogViewModel(singletonCImpl.programRepositoryProvider.get(), viewModelCImpl.startProgramUseCase());

          case 1: // com.athletiq.app.ui.history.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.workoutLogRepositoryProvider.get());

          case 2: // com.athletiq.app.ui.programs.MyProgramsViewModel 
          return (T) new MyProgramsViewModel(singletonCImpl.enrollmentRepositoryProvider.get(), singletonCImpl.programRepositoryProvider.get());

          case 3: // com.athletiq.app.ui.overview.ProgramOverviewViewModel 
          return (T) new ProgramOverviewViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.programRepositoryProvider.get());

          case 4: // com.athletiq.app.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel();

          case 5: // com.athletiq.app.ui.today.TodayViewModel 
          return (T) new TodayViewModel(singletonCImpl.enrollmentRepositoryProvider.get(), singletonCImpl.programRepositoryProvider.get(), viewModelCImpl.getTodaySessionUseCase(), viewModelCImpl.abandonProgramUseCase());

          case 6: // com.athletiq.app.ui.workout.WorkoutViewModel 
          return (T) new WorkoutViewModel(singletonCImpl.programRepositoryProvider.get(), singletonCImpl.workoutLogRepositoryProvider.get(), singletonCImpl.enrollmentRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends AthletiqApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends AthletiqApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends AthletiqApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AthletiqDatabase> provideDatabaseProvider;

    private Provider<EnrollmentDao> provideEnrollmentDaoProvider;

    private Provider<EnrollmentRepository> enrollmentRepositoryProvider;

    private Provider<ProgramDao> provideProgramDaoProvider;

    private Provider<ProgramRepository> programRepositoryProvider;

    private Provider<SeedDataProvider> seedDataProvider;

    private Provider<WorkoutLogDao> provideWorkoutLogDaoProvider;

    private Provider<WorkoutLogRepository> workoutLogRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AthletiqDatabase>(singletonCImpl, 2));
      this.provideEnrollmentDaoProvider = DoubleCheck.provider(new SwitchingProvider<EnrollmentDao>(singletonCImpl, 1));
      this.enrollmentRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<EnrollmentRepository>(singletonCImpl, 0));
      this.provideProgramDaoProvider = DoubleCheck.provider(new SwitchingProvider<ProgramDao>(singletonCImpl, 4));
      this.programRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ProgramRepository>(singletonCImpl, 3));
      this.seedDataProvider = DoubleCheck.provider(new SwitchingProvider<SeedDataProvider>(singletonCImpl, 5));
      this.provideWorkoutLogDaoProvider = DoubleCheck.provider(new SwitchingProvider<WorkoutLogDao>(singletonCImpl, 7));
      this.workoutLogRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<WorkoutLogRepository>(singletonCImpl, 6));
    }

    @Override
    public void injectAthletiqApp(AthletiqApp athletiqApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.athletiq.app.data.repository.EnrollmentRepository 
          return (T) new EnrollmentRepository(singletonCImpl.provideEnrollmentDaoProvider.get());

          case 1: // com.athletiq.app.data.local.dao.EnrollmentDao 
          return (T) DatabaseModule_ProvideEnrollmentDaoFactory.provideEnrollmentDao(singletonCImpl.provideDatabaseProvider.get());

          case 2: // com.athletiq.app.data.local.AthletiqDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.athletiq.app.data.repository.ProgramRepository 
          return (T) new ProgramRepository(singletonCImpl.provideProgramDaoProvider.get());

          case 4: // com.athletiq.app.data.local.dao.ProgramDao 
          return (T) DatabaseModule_ProvideProgramDaoFactory.provideProgramDao(singletonCImpl.provideDatabaseProvider.get());

          case 5: // com.athletiq.app.data.seed.SeedDataProvider 
          return (T) new SeedDataProvider(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.programRepositoryProvider.get());

          case 6: // com.athletiq.app.data.repository.WorkoutLogRepository 
          return (T) new WorkoutLogRepository(singletonCImpl.provideWorkoutLogDaoProvider.get());

          case 7: // com.athletiq.app.data.local.dao.WorkoutLogDao 
          return (T) DatabaseModule_ProvideWorkoutLogDaoFactory.provideWorkoutLogDao(singletonCImpl.provideDatabaseProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
