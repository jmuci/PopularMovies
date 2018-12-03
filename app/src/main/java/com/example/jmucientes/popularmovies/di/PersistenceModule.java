package com.example.jmucientes.popularmovies.di;

import android.arch.persistence.room.Room;
import android.util.Log;

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

    public static final String FAVS_MOVIE_DB = "favs-movie.db";
    private static MovieDataBase INSTANCE;

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
        String currentDBPath = app.getDatabasePath("favs-movie.db").getAbsolutePath();
        Log.d("PersistenceModule", "Database Path: " + currentDBPath);
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(app, MovieDataBase.class, FAVS_MOVIE_DB).build();
        }
        return INSTANCE;
    }
}
