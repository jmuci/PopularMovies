package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.adapter.MoviesAdapter;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.presenters.MainActivityPresenter;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MoviesApplicationModule {

/*    @Provides
    @Singleton
    MainActivityPresenter providesPrsenter(MainActivityViewBinder viewBinder) {
        return new MainActivityPresenter(viewBinder);
    }

    @Provides
    @Singleton
    MoviesAdapter providesMoviesAdapter() {
        return new MoviesAdapter(providesMoviesList());
    }*/

    @Provides
    @Singleton
    static List<Movie> providesMoviesList() {
        return new ArrayList<>(0);
    }

}
