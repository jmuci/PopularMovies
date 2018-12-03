package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.view.MainActivity;
import com.example.jmucientes.popularmovies.view.MoviesDetailsActivity;
import com.example.jmucientes.popularmovies.di.scopes.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    @ActivityScope
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector(modules = MovieDetailsModule.class)
    @ActivityScope
    abstract MoviesDetailsActivity movieDetails();
}
