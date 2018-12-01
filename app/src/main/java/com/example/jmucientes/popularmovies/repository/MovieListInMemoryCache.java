package com.example.jmucientes.popularmovies.repository;

import android.support.annotation.Nullable;

import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MovieListInMemoryCache {

    private List<Movie> mCachedMovieList;

    private static final int MILIS_IN_SECOND = 1000;
    private static final int SECS_IN_MINUTE = 60;
    private int FIVE_MIN_TTL = 5 * SECS_IN_MINUTE * MILIS_IN_SECOND;
    private long mRefreshTime;

    public MovieListInMemoryCache() {
        mCachedMovieList = new ArrayList<>();
    }

    @Nullable
    public List<Movie> getTopRatedMoviesList() {
        if (mCachedMovieList.size() > 0 && isDataStillFresh()) {
            return mCachedMovieList;
        }
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
