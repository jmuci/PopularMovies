package com.example.jmucientes.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.repository.MoviesRepository;

import java.util.List;

import javax.inject.Inject;


public class MoviesDetailsViewModel extends ViewModel {

    private static final String TAG = MoviesDetailsViewModel.class.getName();
    private final MoviesRepository mMoviesRepository;

    // Create a LiveData with a String
    private MutableLiveData<Boolean> mIsMovieFavorite;
    private MutableLiveData<List<Movie>> mFavoriteMovieList;

    @Inject
    public MoviesDetailsViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;

        //mFavoriteMovieList = mMoviesRepository.getFavoriteMoviesList();
        //mFavoriteMovieList = new MutableLiveData<>();
        //mMoviesRepository.getFavoriteMoviesList(mFavoriteMovieList);
        //mMoviesRepository.getFavoriteMoviesListAsync(mFavoriteMovieList);

        //mFavoriteMovieList = new MutableLiveData<>();
        //mIsMovieFavorite = isMovieSavedToFavorites(id);
    }

    public MutableLiveData<Boolean> isMovieFavorite(int id) {
        if (mIsMovieFavorite == null) {
            mIsMovieFavorite = new MutableLiveData<>();
            mIsMovieFavorite.setValue(isMovieSavedToFavorites(id));
        }
        return mIsMovieFavorite;

/*        if (mIsMovieFavorite == null) {
            mIsMovieFavorite = new MutableLiveData<>();
            mMoviesRepository.isMovieFavoriteAsync(id, mIsMovieFavorite);
        }*/
    }

    public void updateMovieFavoriteStatus(Movie movie) {
        //mMoviesRepository.updateMovieFavoriteStatus(movie, mIsMovieFavorite);
        if (isCachedFavoriteMoviesListReady()) {
            Log.d(TAG, "FavLocalCache isReady at updateMovieFavoriteStatus() ");
            if (isMovieSavedToFavorites(movie.getId())) { // Movie was saved as favourite
                mMoviesRepository.deleteMovieFromFavorites(movie.getId());
                mIsMovieFavorite.setValue(false);
                Log.d(TAG, "Deleted from favs movie:  " + movie.getTitle());
            } else {
                mMoviesRepository.saveMovieToFavorites(movie);
                mIsMovieFavorite.setValue(true);
                Log.d(TAG, "Added to favs movie:  " + movie.getTitle());
            }
        } else {
            Log.w(TAG, "FavLocalCache is NOT ready at updateMovieFavoriteStatus(). Did nothing.");
        }
        //TODO We might need to refresh the content of the FavMovieLies
    }

    private boolean isMovieSavedToFavorites(int movieId){
        //if (isCachedFavoriteMoviesListReady()) {
            MutableLiveData<List<Movie>> favoriteMovieList = mMoviesRepository.getCachedFavoriteMovieList();
            //for (Movie movie : mFavoriteMovieList.getValue()) {
            for (Movie movie : favoriteMovieList.getValue()) {
                if (movie.getId() == movieId) {
                    Log.d(TAG, "Movie is in favs collection ");
                    mIsMovieFavorite.setValue(true);
                    return true;
                }
            }
        //}
        Log.d(TAG, "Movie is NOT in favs collection ");
        mIsMovieFavorite.setValue(false);
        return false;
    }

    private boolean isCachedFavoriteMoviesListReady() {
        //return mFavoriteMovieList != null && mFavoriteMovieList.getValue() != null;
        MutableLiveData<List<Movie>> favoriteMovieList = mMoviesRepository.getCachedFavoriteMovieList();
        return favoriteMovieList != null && favoriteMovieList.getValue() != null && favoriteMovieList.getValue().size() >0;
    }
}
