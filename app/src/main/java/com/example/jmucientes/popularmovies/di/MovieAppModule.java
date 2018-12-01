package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.repository.MovieListInMemoryCache;
import com.example.jmucientes.popularmovies.repository.MoviesRepository;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MovieAppModule {

    @Provides
    @ApplicationScope
    static MoviesWebService providesMoviesWebService() {
        return new MoviesWebService();
    }

    @Provides
    @ApplicationScope
    static MoviesRepository providesMoviesRepository(MoviesWebService moviesWebService, MovieListInMemoryCache movieListCache) {
        return new MoviesRepository(moviesWebService, movieListCache);
    }

    @Provides
    @ApplicationScope
    static MovieListInMemoryCache providesMoviesListInMemoryCahce() {
        return new MovieListInMemoryCache();
    }
}
