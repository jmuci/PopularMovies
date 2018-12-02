package com.example.jmucientes.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.db.MovieDAO;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.util.AppExecutors;
import com.example.jmucientes.popularmovies.util.MovieJsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getName();
    public static final int MILIS_IN_ONE_SEC = 1000;
    public static final int SECS_IN_MIN = 60;
    private static final long FRESH_TIME_OUT = 5 * SECS_IN_MIN * MILIS_IN_ONE_SEC;

    private final MoviesWebService mMoviesWebService;
    private final MovieListInMemoryCache mMovieListCacheTopRated;
    private final MovieListInMemoryCache mMovieListCacheMostPopular;
    private final MovieDAO mMovieDAO;
    private final AppExecutors mExecutors;

    @Inject
    public MoviesRepository(MoviesWebService moviesWebService,
                            @Named("topRatedCache") MovieListInMemoryCache movieListCacheTopRated,
                            @Named("mostPopularCache") MovieListInMemoryCache movieListCacheMostPopular,
                            MovieDAO movieDAO,
                            AppExecutors executors) {
        mMoviesWebService = moviesWebService;
        mMovieListCacheTopRated = movieListCacheTopRated;
        mMovieListCacheMostPopular = movieListCacheMostPopular;
        mMovieDAO = movieDAO;
        mExecutors = executors;
    }

    public void getTopRatedMovieList(MutableLiveData<List<Movie>> movieList) {
        Log.d(TAG, "getTopRatedMovieList() called.");
        getTopRatedMovieListFromEndpoint(movieList);
    }

    public void getPopularMovieList(MutableLiveData<List<Movie>> movieList) {
        Log.d(TAG, "getTopRatedMovieList() called.");
        getTopRatedMovieListFromEndpoint(movieList);
    }

    private void refreshMovieList(MutableLiveData<List<Movie>> movieList) {
        mExecutors.networkIO().execute(() -> {
            Log.d(TAG, "refreshMovies() called.");
            //boolean hasRecentList = hasRecentList(FRESH_TIME_OUT);
            //TODO this never hits the DB, how to ensure DB data is fresh?
            List<Movie> latestMovieList = mMovieListCacheTopRated.getMoviesList();
            if (latestMovieList == null) {
                Log.d(TAG, "No Cache, calling network.");
                getTopRatedMovieListFromEndpoint(movieList);
            } else {
                Log.d(TAG, "Calling db. mMovieDAO.getAllFavoriteMovies() and setting value");
                movieList.setValue(mMovieDAO.getAllFavoriteMovies().getValue());
            }
        });
    }

    private void getTopRatedMovieListFromEndpoint(MutableLiveData<List<Movie>> movieList) {
        Log.d(TAG, "Calling endpoint");
        List<Movie> latestMovieList = mMovieListCacheTopRated.getMoviesList();
        if (latestMovieList != null) { // Cached content
            movieList.setValue(latestMovieList);
        } else { // Network request
            Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "1");
            Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "2");
            Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "3");
            executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3, movieList);
        }
    }

    private void getPopularMovieListFromEndpoint(MutableLiveData<List<Movie>> movieList) {
        List<Movie> latestMovieList = mMovieListCacheMostPopular.getMoviesList();
        if (latestMovieList != null) { // Cached content
            Log.d(TAG, "Returning cached content");
            movieList.setValue(latestMovieList);
        } else { // Network request
            Log.d(TAG, "Calling endpoint");
            Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "1");
            Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "2");
            Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "3");
            executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3, movieList);
        }
    }

    private void executeBackgroundNetworkRequest(Uri requestUri, Uri requestUri2, Uri requestUri3, MutableLiveData<List<Movie>> data) {
        Single<String> requestSingle = makeMoviesRequestSingleObvservable(requestUri);
        Single<String> requestSingle2 = makeMoviesRequestSingleObvservable(requestUri2);
        Single<String> requestSingle3 = makeMoviesRequestSingleObvservable(requestUri3);
        List<Movie> mergedMovieList = new ArrayList<>(60);

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
                        mMovieListCacheTopRated.updateInMemoryCache(mergedMovieList);
                        data.setValue(mergedMovieList);
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
                        //TODO Error handling?
                        if (movieList == null || movieList.size() == 0) {
                            Log.w(TAG, "The movie list was empty or null.");
                            return;
                        }
                        Log.d(TAG, "Caching and updating LiveData. MovieList .siz() : " + movieList.size() );
                        mergedMovieList.addAll(movieList);
                        //mMovieListCacheTopRated.updateInMemoryCache(movieList);

                        //data.setValue(movieList);
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