package com.example.jmucientes.popularmovies.di;

import android.arch.persistence.room.Room;

import com.example.jmucientes.popularmovies.MovieApp;
import com.example.jmucientes.popularmovies.db.MovieDAO;
import com.example.jmucientes.popularmovies.db.MovieDataBase;
import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.repository.MovieListInMemoryCache;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistenceModule {

    @Provides
    @Named("topRatedCache")
    @ApplicationScope
    static MovieListInMemoryCache providesMoviesListInMemoryCacheTopRated() {
        return new MovieListInMemoryCache();
    }

    @Provides
    @Named("mostPopularCache")
    @ApplicationScope
    static MovieListInMemoryCache providesMoviesListInMemoryCacheMostPopular() {
        return new MovieListInMemoryCache();
    }

    @Provides
    @ApplicationScope
    MovieDAO providesMovieDAO(MovieDataBase movieDataBase) {
        return movieDataBase.movieDAO();
    }


    @Provides
    @ApplicationScope
    MovieDataBase providesMovieDataBase(MovieApp app) {
        return Room.databaseBuilder(app, MovieDataBase.class, "favs-movie.db").build();
    }
}
