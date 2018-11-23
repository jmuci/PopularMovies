package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MainActivity;
import com.example.jmucientes.popularmovies.MoviesDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector(modules = MovieDetailsModule.class)
    abstract MoviesDetailsActivity movieDetails();
}
