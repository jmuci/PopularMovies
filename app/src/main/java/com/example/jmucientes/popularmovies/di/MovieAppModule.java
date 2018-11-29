package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.model.MoviesViewModel;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
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

/*    @Provides
    @ApplicationScope
    static MoviesViewModel providesMoviesViewModel() {
        return new MoviesViewModel();
    }*/

    @Provides
    @ApplicationScope
    static MoviesRepository providesMoviesRepository(MoviesWebService moviesWebService) {
        return new MoviesRepository(moviesWebService);
    }
}
