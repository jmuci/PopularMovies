package com.example.jmucientes.popularmovies.presenters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.MainActivity;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.JsonUtils;
import com.example.jmucientes.popularmovies.util.Network;
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

public class MainActivityPresenter {
    private static final String TAG  = MainActivity.class.getName();

    private WeakReference<MainActivityViewBinder> mViewBinderWR;
    private String mCurrentSortOption;

    public MainActivityPresenter(MainActivityViewBinder viewBinder) {
        mViewBinderWR = new WeakReference<>(viewBinder);
    }

    public void requestTopRatedMoviesFromTheMovieDB() {
        mCurrentSortOption = Network.TOP_RATED_END_POINT;
        Uri requestUri = Network.buildRequestUriForMoviesWithEndPoint(Network.TOP_RATED_END_POINT, "1");
        Uri requestUri2 = Network.buildRequestUriForMoviesWithEndPoint(Network.TOP_RATED_END_POINT,"2");
        Uri requestUri3 = Network.buildRequestUriForMoviesWithEndPoint(Network.TOP_RATED_END_POINT,"3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3);
    }

    public void requestMostPopularMoviesFromTheMovieDB() {
        mCurrentSortOption = Network.MOST_POPULAR_END_POINT;
        Uri requestUri = Network.buildRequestUriForMoviesWithEndPoint(Network.MOST_POPULAR_END_POINT, "1");
        Uri requestUri2 = Network.buildRequestUriForMoviesWithEndPoint(Network.MOST_POPULAR_END_POINT,"2");
        Uri requestUri3 = Network.buildRequestUriForMoviesWithEndPoint(Network.MOST_POPULAR_END_POINT, "3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3);
    }

    private void executeBackgroundNetworkRequest(Uri requestUri, Uri requestUri2, Uri requestUri3) {
        Single<String> requestSingle = makeMoviesRequestSingleObvservable(requestUri);
        Single<String> requestSingle2 = makeMoviesRequestSingleObvservable(requestUri2);
        Single<String> requestSingle3 = makeMoviesRequestSingleObvservable(requestUri3);
        Single.merge(
                requestSingle,
                requestSingle2,
                requestSingle3)
                .subscribeOn(Schedulers.io())
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
                            movieList = JsonUtils.parseJsonResponse(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String msg = "JSON parsing error! Error : " + e.toString();
                            Log.e(TAG, msg);
                            mViewBinderWR.get().showErrorMessage(msg);
                        }
                        if (movieList != null && movieList.size() > 0) {
                            mViewBinderWR.get().updateAdapterContent(movieList, true);
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
                    return Network.makeRequest(requestUri);
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
