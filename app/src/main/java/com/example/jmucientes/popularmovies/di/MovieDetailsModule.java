package com.example.jmucientes.popularmovies.di;

import com.example.jmucientes.popularmovies.MoviesDetailsActivity;
import com.example.jmucientes.popularmovies.adapter.ReviewsAdapter;
import com.example.jmucientes.popularmovies.adapter.TrailersAdapter;
import com.example.jmucientes.popularmovies.di.scopes.ActivityScope;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.presenters.MovieDetailsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieDetailsModule {

    @Provides
    @ActivityScope
    static MovieDetailsPresenter providesMovieDetailsPresenter(MoviesDetailsActivity viewBinder, MoviesWebService moviesWebService) {
        return new MovieDetailsPresenter(viewBinder, moviesWebService);
    }

    @Provides
    @ActivityScope
    static TrailersAdapter providesTrailersAdapter() {
        return new TrailersAdapter();
    }

    @Provides
    @ActivityScope
    static ReviewsAdapter providesReviewsAdapter() {
        return new ReviewsAdapter();
    }
}
