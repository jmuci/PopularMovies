package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = {MainActivityModule.class, MoviesApplicationModule.class})
    abstract MainActivity bindMainActivity();
}
