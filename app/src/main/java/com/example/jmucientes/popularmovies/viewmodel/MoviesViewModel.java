package com.example.jmucientes.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.repository.MoviesRepository;

import java.util.List;

import javax.inject.Inject;
@ApplicationScope
public class MoviesViewModel extends ViewModel {
    private static final String TAG = MoviesViewModel.class.getName();

    private MutableLiveData<List<Movie>> mTopMovieList;
    private MutableLiveData<List<Movie>> mPopularMovieList;
    //private MutableLiveData<List<Movie>> mFavoriteMoviesList;

    private final MoviesRepository mMoviesRepository;

    @Inject
    public MoviesViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
/*        mFavoriteMoviesList = new MutableLiveData<>();
        Log.d(TAG, "Calling Get Favs");
        mMoviesRepository.getFavoriteMoviesListAsync(mFavoriteMoviesList);*/
    }

    public LiveData<List<Movie>> getTopMovieList() {
        if (mTopMovieList == null || mTopMovieList.getValue() == null) {
            mTopMovieList = new MutableLiveData<>();
            getTopRatedMovies();
        }
        return mTopMovieList;
    }

    private void getTopRatedMovies() {
        mMoviesRepository.getTopRatedMovieList(mTopMovieList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }

    public LiveData<List<Movie>> getMostPopularMovieList() {
        if (mPopularMovieList == null || mPopularMovieList.getValue() == null) {
            mPopularMovieList = new MutableLiveData<>();
            getMostPopularMovies();
        }
        return mPopularMovieList;
    }

    private void getMostPopularMovies() {
        mMoviesRepository.getPopularMovieList(mPopularMovieList);
    }

    public LiveData<List<Movie>> getFavoritesList() {
/*        if (mFavoriteMoviesList == null || mFavoriteMoviesList.getValue() == null) {
            mFavoriteMoviesList = new MutableLiveData<>();
            getFavoriteMovies();
        }*/
        return mMoviesRepository.getCachedFavoriteMovieList();
    }

}
