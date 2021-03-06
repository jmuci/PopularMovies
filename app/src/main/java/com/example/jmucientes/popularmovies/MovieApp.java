package com.example.jmucientes.popularmovies;

import com.example.jmucientes.popularmovies.di.DaggerMovieAppComponent;
import com.facebook.stetho.Stetho;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MovieApp extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerMovieAppComponent.builder().application(this).build();
    }

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
