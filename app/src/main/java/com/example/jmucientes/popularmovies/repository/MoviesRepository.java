package com.example.jmucientes.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.util.MovieJsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getName();

    final MoviesWebService mMoviesWebService;
    final MovieListInMemoryCache mMovieListCache;

    public MoviesRepository(MoviesWebService moviesWebService, MovieListInMemoryCache movieListCache) {
        mMoviesWebService = moviesWebService;
        mMovieListCache = movieListCache;
    }

    public void getTopRatedMovieList(MutableLiveData<List<Movie>> movieList) {

        List<Movie> cached = mMovieListCache.getTopRatedMoviesList();
        if (cached != null) {
            movieList.setValue(cached);
        }

        Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "1");
        Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT,"2");
        Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT,"3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3, movieList);
    }

    private void executeBackgroundNetworkRequest(Uri requestUri, Uri requestUri2, Uri requestUri3, MutableLiveData<List<Movie>> data) {
        Single<String> requestSingle = makeMoviesRequestSingleObvservable(requestUri);
        Single<String> requestSingle2 = makeMoviesRequestSingleObvservable(requestUri2);
        Single<String> requestSingle3 = makeMoviesRequestSingleObvservable(requestUri3);
        // Request the 3 first pages and merge the response.
        Single.merge(
                requestSingle,
                requestSingle2,
                requestSingle3)
                .subscribeOn(Schedulers.io()) // Run on the background.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called. Error : " + e.getMessage();
                        Log.e(TAG, msg);
                        //mViewBinderWR.get().showErrorMessage(msg);
                    }

                    @Override
                    public void onNext(String jsonString) {
                        Log.d(TAG, "onNext() called.");
                        List<Movie> movieList = null;
                        try {
                            movieList = MovieJsonParser.parseMovieListJsonResponse(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String msg = "JSON parsing error! Error : " + e.toString();
                            Log.e(TAG, msg);
                        }
                        if (movieList != null && movieList.size() > 0) {
/*                            if (mViewBinderWR.get() != null) {
                                mViewBinderWR.get().updateAdapterContent(movieList, true);
                            } else {
                                Log.e(TAG, "View binder is null! The activity was destroyed before the results arrive?");
                            }
                        } else {*/
                            Log.w(TAG, "The movie list was empty.");
                            //mViewBinderWR.get().showErrorMessage("Empty movie list.");

                        }
                        mMovieListCache.updateInMemoryCache(movieList);
                        data.setValue(movieList);
                    }
                });
    }

    private Single<String> makeMoviesRequestSingleObvservable(@NonNull final Uri requestUri) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    return mMoviesWebService.makeRequest(requestUri);
                } catch (IOException e) {
                    String msg = "IOException. Failed to fetch movies list. Error: " + e.getMessage();
                    Log.e(TAG, msg);
                    //mViewBinderWR.get().showErrorMessage(msg);
                    return null;
                }
            }
        });
    }

}