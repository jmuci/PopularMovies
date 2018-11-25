package com.example.jmucientes.popularmovies.presenters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.VideoTrailer;
import com.example.jmucientes.popularmovies.util.NetworkUtils;
import com.example.jmucientes.popularmovies.util.VideoTrailerJsonParser;
import com.example.jmucientes.popularmovies.view.MovieDetailsViewBinder;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailsPresenter {

    private WeakReference<MovieDetailsViewBinder> mDetailsViewBinderWR;
    private final String TAG = MovieDetailsPresenter.class.getName();

    public MovieDetailsPresenter(MovieDetailsViewBinder viewBinder) {
        mDetailsViewBinderWR = new WeakReference<>(viewBinder);
    }

    public void requestTrailersForMovie(int id) {
        Uri requestUri = NetworkUtils.buildRequestUriForTrailersForMovie(id);
        executeBackgroundNetworkRequest(requestUri);
    }

    private void executeBackgroundNetworkRequest(Uri requestUri) {

        Single<String> mResponseSingle = makeMoviesRequestSingleObvservable(requestUri);

        mResponseSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called. Succeeded collecting the videos");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called. Error : " + e.getMessage();
                        Log.e(TAG, msg);
                    }

                    @Override
                    public void onNext(String jsonString) {
                        Log.d(TAG, "onNext() called.");
                        List<VideoTrailer> trailersList = null;
                        try {
                             trailersList = VideoTrailerJsonParser.parseTrailersListJsonResponse(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String msg = "JSON parsing error! Error : " + e.toString();
                            Log.e(TAG, msg);
                        }

                        if (trailersList != null && trailersList.size() > 0) {
                            mDetailsViewBinderWR.get().updateAdapterContent(trailersList);
                        } else {
                            Log.e(TAG, "View binder is null! The activity was destroyed before the results arrive?");
                        }
                    }
                });
    }

    /**
     *
     * @param requestUri
     * @return
     */

    private Single<String> makeMoviesRequestSingleObvservable(@NonNull final Uri requestUri) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    return NetworkUtils.makeRequest(requestUri);
                } catch (IOException e) {
                    String msg = "IOException. Failed to fetch movies list. Error: " + e.getMessage();
                    Log.e(TAG, msg);
                    return null;
                }
            }
        });
    }
}
