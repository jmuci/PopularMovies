package com.example.jmucientes.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.jmucientes.popularmovies.db.dao.MovieDAO;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getName();
    private final MovieDAO mMovieDAO;
    private LiveData<List<Movie>> mFavoriteMovies;
    private final AppExecutors mExecutors;

    @Inject
    public MovieRepository(MovieDAO movieDAO, AppExecutors executors) {
        mMovieDAO = movieDAO;
        mExecutors = executors;
        // As it returns LiveData it happens in a background thread by default.
        mFavoriteMovies = mMovieDAO.getAllFavoriteMovies();
    }

    /**
     * Get all movies saved
     * @return a LiveData observable containing all the movies saved as favourites
     */
    public LiveData<List<Movie>> getFavoriteMovies() {
        if (mFavoriteMovies == null || mFavoriteMovies.getValue() == null) {
            Log.w(TAG, "Trying to get pre-fetched favorites list but mFavoriteMovies data is null. ");

        }
        return mFavoriteMovies;
    }

    public void saveMovieToFavorites(final Movie movie) {
        mExecutors.diskIO().execute(() -> mMovieDAO.insertMovie(movie));
    }
}
