package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MainActivity;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainActivityModule {

    @Binds
    abstract MainActivityViewBinder provideMainActivityViewBinder(MainActivity mainActivity);
}