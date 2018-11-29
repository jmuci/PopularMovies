package com.example.jmucientes.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.jmucientes.popularmovies.di.DaggerMovieAppComponent;
import com.example.jmucientes.popularmovies.di.MovieAppComponent;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MoviesViewModel extends ViewModel {
    private static final String TAG = MoviesViewModel.class.getName();

    private MutableLiveData<List<Movie>> mMovieList;

    final MoviesRepository mMoviesRepository;

    @Inject
    public MoviesViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public LiveData<List<Movie>> getTopMovieList() {
        if (mMovieList == null) {
            mMovieList = new MutableLiveData<>();
            getTopRatedMovies();
        }
        return mMovieList;
    }

    public void getTopRatedMovies() {
        mMoviesRepository.getTopRatedMovieList(mMovieList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }


}
