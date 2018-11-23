package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MainActivity;
import com.example.jmucientes.popularmovies.di.scopes.ActivityScope;
import com.example.jmucientes.popularmovies.presenters.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {


    @Provides
    @ActivityScope
    MainActivityPresenter providesMainActivityPresenter(MainActivity viewBinder) {
        return new MainActivityPresenter(viewBinder);
    }

}
