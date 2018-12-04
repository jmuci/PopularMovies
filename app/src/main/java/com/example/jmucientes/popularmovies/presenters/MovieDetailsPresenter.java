package com.example.jmucientes.popularmovies.presenters;

import android.net.Uri;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Review;
import com.example.jmucientes.popularmovies.model.VideoTrailer;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.util.ReviewJsonParser;
import com.example.jmucientes.popularmovies.util.VideoTrailerJsonParser;
import com.example.jmucientes.popularmovies.view.MovieDetailsViewBinder;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

public class MovieDetailsPresenter {

    public static final String FAVORITE_PREFERENCES = "FavoritePreferences";
    public static final String IS_FAV_KEY_PREFIX = "is_fav_";
    public static final String FAV_MOVIE_JSON_KEY_PREFIX = "fav_poster_";
    private WeakReference<MovieDetailsViewBinder> mDetailsViewBinderWR;
    private final String TAG = MovieDetailsPresenter.class.getName();

    private final MoviesWebService mMoviesWebService;

    public MovieDetailsPresenter(MovieDetailsViewBinder viewBinder, MoviesWebService moviesWebService) {
        mDetailsViewBinderWR = new WeakReference<>(viewBinder);
        mMoviesWebService = moviesWebService;
    }

    public void requestTrailersForMovie(int id) {
        Uri requestUri = mMoviesWebService.buildRequestUriForMovieTrailers(id);
        mMoviesWebService.executeBackgroundNetworkRequest(requestUri, new MoviesWebService.GeneralParser() {
            @Override
            public void parseResultsAndUpdateAdapter(String jsonResponse) throws JSONException {
                parseTrailersResultsAndUpdateAdapter(jsonResponse);
            }
        });
    }

    public void requestReviewsForMovie(int id) {
        Uri requestUri = mMoviesWebService.buildRequestUriForMovieReviews(id);
        mMoviesWebService.executeBackgroundNetworkRequest(requestUri, new MoviesWebService.GeneralParser() {
            @Override
            public void parseResultsAndUpdateAdapter(String jsonResponse) throws JSONException {
                parseReviewsResultsAndUpdateAdapter(jsonResponse);
            }
        });
    }

    private void parseTrailersResultsAndUpdateAdapter(String jsonString) throws JSONException {
        List<VideoTrailer> trailersList = null;
        trailersList = VideoTrailerJsonParser.parseTrailersListJsonResponse(jsonString);

        if (trailersList != null && trailersList.size() > 0) {
            mDetailsViewBinderWR.get().updateTrailersAdapterContent(trailersList);
        } else {
            Log.e(TAG, "View binder is null! The activity was destroyed before the results arrive?");
        }
    }

    private void parseReviewsResultsAndUpdateAdapter(String jsonString) throws JSONException {
        List<Review> reviewList = null;
        reviewList = ReviewJsonParser.parseReviewListJsonResponse(jsonString);

        if (reviewList != null && reviewList.size() > 0) {
            mDetailsViewBinderWR.get().updateReviewsAdapterContent(reviewList);
        } else {
            Log.e(TAG, "View binder is null! The activity was destroyed before the results arrive?");
        }
    }

}
