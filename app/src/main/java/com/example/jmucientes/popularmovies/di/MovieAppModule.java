package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.network.MoviesWebService;

import dagger.Module;
import dagger.Provides;

@Module(includes = PersistenceModule.class)
public abstract class MovieAppModule {

    @Provides
    @ApplicationScope
    static MoviesWebService providesMoviesWebService() {
        return new MoviesWebService();
    }

}
