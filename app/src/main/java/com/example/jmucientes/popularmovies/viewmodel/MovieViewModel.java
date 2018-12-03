package com.example.jmucientes.popularmovies.viewmodel;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.MovieApp;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;


public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getName();
    private final MovieRepository mMovieRepository;
    private LiveData<List<Movie>> mFavoriteMovies;

    @Inject
    public MovieViewModel(@NonNull MovieApp application, MovieRepository movieRepository) {
        super(application);
        mMovieRepository = movieRepository;
        Log.d(TAG, "Instantiated MovieViewModel. Fetching movie list");
        mFavoriteMovies = mMovieRepository.getFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public void saveMovieToFavorites(Movie movie) {
        mMovieRepository.saveMovieToFavorites(movie);
    }
}
