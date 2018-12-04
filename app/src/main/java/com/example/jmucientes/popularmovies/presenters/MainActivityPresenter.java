package com.example.jmucientes.popularmovies.presenters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.util.MovieJsonParser;
import com.example.jmucientes.popularmovies.view.MainActivity;
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
    public static final String SHOW_FAVORITES = "show_favorites_order";
    public static final String SHOW_TOP_RATED = "show_top_rated_order";
    public static final String SHOW_MOST_POPULAR = "show_most_pop_order";
    //TODO Move to string resources
    public static final String TOP_RATED_MOVIES_TITLE = "Top Rated Movies";
    public static final String POPULAR_MOVIES_NOW_TITLE = "Popular Movies Now";
    public static final String MY_FAVORITE_MOVIES_TITLE = "My Favorite Movies";

    private WeakReference<MainActivityViewBinder> mViewBinderWR;
    private String mCurrentSortOption;

    private MoviesWebService mMoviesWebService;

    public MainActivityPresenter(@NonNull MainActivityViewBinder viewBinder, MoviesWebService moviesWebService) {
        mViewBinderWR = new WeakReference<>(checkNotNull(viewBinder));
        mCurrentSortOption = SHOW_TOP_RATED;
        mMoviesWebService = moviesWebService;
    }

    /**
     * Executes a request to the Top Rated end point.
     */
    public void requestTopRatedMoviesFromTheMovieDB() {
        mCurrentSortOption = SHOW_TOP_RATED;
        Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "1");
        Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT,"2");
        Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT,"3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3);
        mViewBinderWR.get().setToolBarTitle(TOP_RATED_MOVIES_TITLE);
    }

    /**
     * Executes a request to the most popular end point.
     */
    public void requestMostPopularMoviesFromTheMovieDB() {
        mCurrentSortOption = SHOW_MOST_POPULAR;
        Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "1");
        Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT,"2");
        Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3);
        mViewBinderWR.get().setToolBarTitle(POPULAR_MOVIES_NOW_TITLE);
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
                    return mMoviesWebService.makeRequest(requestUri);
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

    public void setCurrentSortOptionBasedOnTitle(String currentSortOptionBasedOnTitle) {
        switch (currentSortOptionBasedOnTitle) {
            case MY_FAVORITE_MOVIES_TITLE:
                mCurrentSortOption = SHOW_FAVORITES;
                break;
            case TOP_RATED_MOVIES_TITLE:
                mCurrentSortOption = TOP_RATED_MOVIES_TITLE;
                break;
            case POPULAR_MOVIES_NOW_TITLE:
                mCurrentSortOption = POPULAR_MOVIES_NOW_TITLE;
                break;
        }
    }

    public void setCurrentSortOption(String currentSortOption) {
        mCurrentSortOption = currentSortOption;
    }
}
