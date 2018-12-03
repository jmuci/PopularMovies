package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.view.MainActivity;
import com.example.jmucientes.popularmovies.adapter.MoviesAdapter;
import com.example.jmucientes.popularmovies.di.scopes.ActivityScope;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.presenters.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    @ActivityScope
    MainActivityPresenter providesMainActivityPresenter(MainActivity viewBinder, MoviesWebService moviesWebService) {
        return new MainActivityPresenter(viewBinder, moviesWebService);
    }

    @Provides
    @ActivityScope
    MoviesAdapter providesMoviesAdapter(List<Movie> movieList) {
        return new MoviesAdapter(movieList);
    }

    @Provides
    @ActivityScope
    List<Movie> providesEmptyMovieList() {
        return new ArrayList<>(0);
    }
}
