package com.example.jmucientes.popularmovies.viewmodel;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.MovieApp;
import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

@ApplicationScope
public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getName();
    private final MovieRepository mMovieRepository;
    private LiveData<List<Movie>> mFavoriteMovies;
    private MutableLiveData<Boolean> mIsFavorite;

    @Inject
    public MovieViewModel(@NonNull MovieApp application, MovieRepository movieRepository) {
        super(application);
        mMovieRepository = movieRepository;
        Log.d(TAG, "Instantiated MovieViewModel. Fetching movie list");
        mFavoriteMovies = mMovieRepository.getFavoriteMovies();
        mIsFavorite = new MutableLiveData<>();
        mIsFavorite.setValue(false);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public void saveMovieToFavorites(Movie movie) {
        mMovieRepository.saveMovieToFavorites(movie);
        mIsFavorite.setValue(true);
    }

    public void removeMovieFromFavorites(int id) {
        mMovieRepository.deleteMovieFromFavorites(id);
        mIsFavorite.setValue(false);
    }

    public MutableLiveData<Boolean> getIsFavoriteForCurrentMovie(int id) {
        if (isSavedToFavorites(id) || mIsFavorite.getValue()) {
             mIsFavorite.setValue(true);
            Log.d(TAG, "getIsFavoriteForCurrentMovie() -> true");
        } else {
            mIsFavorite.setValue(false);
            Log.d(TAG, "getIsFavoriteForCurrentMovie() -> false");

        }
        return mIsFavorite;
    }

    public boolean isSavedToFavorites(int id) {
        if (mFavoriteMovies.getValue() != null) {
            for (Movie movie : mFavoriteMovies.getValue()) {
                if (movie.getId() == id) {
                    Log.d(TAG, "Found movie in collection! Id: " + id);
                    return true;
                }
            }
        } else {
            Log.w(TAG, "FavMovieList is Null");
        }
        return false;
    }

}
