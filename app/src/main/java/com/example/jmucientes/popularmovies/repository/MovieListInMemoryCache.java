package com.example.jmucientes.popularmovies.repository;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListInMemoryCache {

    private List<Movie> mCachedMovieList;

    private static final String TAG  = MovieListInMemoryCache.class.getName();
    private static final int MILIS_IN_SECOND = 1000;
    private static final int SECS_IN_MINUTE = 60;
    private static final int FIVE_MIN_TTL = 5 * SECS_IN_MINUTE * MILIS_IN_SECOND;
    private long mRefreshTime;

    public MovieListInMemoryCache() {
        mCachedMovieList = new ArrayList<>();
    }

    @Nullable
    public List<Movie> getMoviesList() {
        if (mCachedMovieList.size() > 0 && isDataStillFresh()) {
            Log.d(TAG, "Hit Cache with cached content. Returning Movie list size: " + mCachedMovieList.size());
            return mCachedMovieList;
        }
        Log.d(TAG, "Hit Cache with NO cached content. Returning null: ");
        return null;
    }

    private boolean isDataStillFresh() {
        return (System.currentTimeMillis() - mRefreshTime) < FIVE_MIN_TTL;
    }

    public void updateInMemoryCache(List<Movie> cachedMovieList) {
        mCachedMovieList = cachedMovieList;
        mRefreshTime = System.currentTimeMillis();
    }
}
