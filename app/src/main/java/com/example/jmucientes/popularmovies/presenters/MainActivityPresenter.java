package com.example.jmucientes.popularmovies.presenters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.MainActivity;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.MovieJsonParser;
import com.example.jmucientes.popularmovies.util.NetworkUtils;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * This class will take care of generating and executing the network requests to the Movie DB
 * using RX Java libraries.
 * Once the the results are obtained, the Suscriber's onNext() will be called, the String response
 * will be parsed and set as the Adapter's dataSet.
 */
public class MainActivityPresenter {
    private static final String TAG  = MainActivity.class.getName();

    private WeakReference<MainActivityViewBinder> mViewBinderWR;
    private String mCurrentSortOption;

    public MainActivityPresenter(@NonNull MainActivityViewBinder viewBinder) {
        mViewBinderWR = new WeakReference<>(checkNotNull(viewBinder));
    }

    /**
     * Executes a request to the Top Rated end point.
     */
    public void requestTopRatedMoviesFromTheMovieDB() {
        mCurrentSortOption = NetworkUtils.TOP_RATED_END_POINT;
        Uri requestUri = NetworkUtils.buildRequestUriForMoviesWithEndPoint(NetworkUtils.TOP_RATED_END_POINT, "1");
        Uri requestUri2 = NetworkUtils.buildRequestUriForMoviesWithEndPoint(NetworkUtils.TOP_RATED_END_POINT,"2");
        Uri requestUri3 = NetworkUtils.buildRequestUriForMoviesWithEndPoint(NetworkUtils.TOP_RATED_END_POINT,"3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3);
    }

    /**
     * Executes a request to the most popular end point.
     */
    public void requestMostPopularMoviesFromTheMovieDB() {
        mCurrentSortOption = NetworkUtils.MOST_POPULAR_END_POINT;
        Uri requestUri = NetworkUtils.buildRequestUriForMoviesWithEndPoint(NetworkUtils.MOST_POPULAR_END_POINT, "1");
        Uri requestUri2 = NetworkUtils.buildRequestUriForMoviesWithEndPoint(NetworkUtils.MOST_POPULAR_END_POINT,"2");
        Uri requestUri3 = NetworkUtils.buildRequestUriForMoviesWithEndPoint(NetworkUtils.MOST_POPULAR_END_POINT, "3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3);
    }

    private void executeBackgroundNetworkRequest(Uri requestUri, Uri requestUri2, Uri requestUri3) {
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
                        mViewBinderWR.get().showErrorMessage(msg);
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
                            mViewBinderWR.get().showErrorMessage(msg);
                        }
                        if (movieList != null && movieList.size() > 0) {
                            if (mViewBinderWR.get() != null) {
                                mViewBinderWR.get().updateAdapterContent(movieList, true);
                            } else {
                                Log.e(TAG, "View binder is null! The activity was destroyed before the results arrive?");
                            }
                        } else {
                            Log.w(TAG, "The movie list was empty.");
                            mViewBinderWR.get().showErrorMessage("Empty movie list.");

                        }
                    }
                });
    }


    private Single<String> makeMoviesRequestSingleObvservable(@NonNull final Uri requestUri) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    return NetworkUtils.makeRequest(requestUri);
                } catch (IOException e) {
                    String msg = "IOException. Failed to fetch movies list. Error: " + e.getMessage();
                    Log.e(TAG, msg);
                    mViewBinderWR.get().showErrorMessage(msg);
                    return null;
                }
            }
        });
    }

    public String getCurrentSortOption() {
        return mCurrentSortOption;
    }

}
