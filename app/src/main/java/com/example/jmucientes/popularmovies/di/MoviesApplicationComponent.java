package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MoviesApplication;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component(modules = {AndroidInjectionModule.class, MoviesApplicationModule.class, BuildersModule.class})
public interface MoviesApplicationComponent extends AndroidInjector<MoviesApplication> {

}
