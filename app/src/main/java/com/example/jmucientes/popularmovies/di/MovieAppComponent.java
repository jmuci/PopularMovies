package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MovieApp;
import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidSupportInjectionModule.class,
        MovieAppModule.class,
        ViewModelModule.class,
        ActivityBindingModule.class})
@ApplicationScope
public interface MovieAppComponent extends AndroidInjector<MovieApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        MovieAppComponent.Builder application(MovieApp application);

        MovieAppComponent build();
    }
}
