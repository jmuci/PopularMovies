package com.example.jmucientes.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jmucientes.popularmovies.adapter.MoviesAdapter;
import com.example.jmucientes.popularmovies.adapter.ReviewsAdapter;
import com.example.jmucientes.popularmovies.adapter.TrailersAdapter;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.model.Review;
import com.example.jmucientes.popularmovies.model.VideoTrailer;
import com.example.jmucientes.popularmovies.presenters.MovieDetailsPresenter;
import com.example.jmucientes.popularmovies.util.NetworkUtils;
import com.example.jmucientes.popularmovies.view.MovieDetailsViewBinder;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * This class is responsible for showing the details of the Movie.
 * It has a different layout for landscape mode.
 * The layout defines a collapsing toolbar, where the collapsing toolbar is decorated
 * with a backdrop image from the movie.
 */
public class MoviesDetailsActivity extends DaggerAppCompatActivity implements MovieDetailsViewBinder {


    @BindView(R.id.collapsing_toolbar_backdrop)
    ImageView mImageBackdropView;
    @BindView(R.id.poster_view)
    ImageView mPosterView;
    @BindView(R.id.collapsing_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.release_year)
    TextView mReleaseYearTv;
    @BindView(R.id.rating)
    TextView mRating;
    @BindView(R.id.synopsis_tv)
    TextView mSynopsisTv;
    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.scroll)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView mTrailersRV;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView mReviewsRV;
    @BindView(R.id.reviews_section_header)
    TextView mReviewsTextView;
    @BindView(R.id.fab)
    FloatingActionButton mFavoriteButton;


    Movie mMovie;

    @Inject
    ReviewsAdapter mReviewsAdapter;
    @Inject
    TrailersAdapter mTrailersAdapter;
    @Inject
    MovieDetailsPresenter mMovieDetailsPresenter;

    private boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        if (startingIntent != null && startingIntent.getSerializableExtra(MoviesAdapter.MOVIE_KEY) != null) {
            mMovie = (Movie) startingIntent.getSerializableExtra(MoviesAdapter.MOVIE_KEY);
            mMovieDetailsPresenter.requestTrailersForMovie(mMovie.getId());
            mMovieDetailsPresenter.requestReviewsForMoview(mMovie.getId());
            populateUI();
            setUpRecyclerView(mTrailersRV, mTrailersAdapter);
            setUpRecyclerView(mReviewsRV, mReviewsAdapter);
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        setUpToolBar();
        appBarLayout.setMinimumHeight(R.dimen.event_entity_appbar_height);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovieDetailsPresenter.updateIsFavoriteStatus(mMovie.getId());
                setIsFavoriteButtonStatus(mMovieDetailsPresenter.isMovieFavorite(mMovie.getId()));
            }
        });
    }

    private void setUpRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
    }

    private void populateUI() {
        if (mMovie.notEmpty()) {
            mToolbar.setTitle(mMovie.getTitle());
            mSynopsisTv.setText(mMovie.getOverview());
            if (mMovie.getRelease_date().length() > 4) {
                mReleaseYearTv.setText(mMovie.getRelease_date().substring(0, 4));
            }
            mRatingBar.setStepSize(0.25f);
            mRatingBar.setRating(Float.valueOf(mMovie.getVote_average()) / 2);
            mRating.setText(mMovie.getVote_average());
            // Set back drop image with Higher Resolution
            Picasso.with(this)
                    .load(NetworkUtils.getFullyQualifiedImageUri(mMovie.getBackdrop_path(), NetworkUtils.IMAGE_SIZE_W_500))
                    .error(R.drawable.baseline_error_black_36)
                    .placeholder(R.drawable.baseline_cloud_download_black_36)
                    .into(mImageBackdropView);

            // Set poster image in Detailed view with Higher Resolution
            Picasso.with(this)
                    .load(NetworkUtils.getFullyQualifiedImageUri(mMovie.getPoster_path(), NetworkUtils.IMAGE_SIZE_W_500))
                    .error(R.drawable.baseline_error_black_36)
                    .placeholder(R.drawable.baseline_cloud_download_black_36)
                    .into(mPosterView);

            // Update FAB Save to Favorites Button State
            setIsFavoriteButtonStatus(mMovieDetailsPresenter.isMovieFavorite(mMovie.getId()));
        }
    }

    private void setIsFavoriteButtonStatus(boolean isFavorite) {
        if (isFavorite) {
            mFavoriteButton.setImageResource(R.drawable.baseline_favorite_black_24dp);
        } else {
            mFavoriteButton.setImageResource(R.drawable.baseline_favorite_white_24);
        }
    }

    @Override
    public void updateTrailersAdapterContent(List<VideoTrailer> trailers) {
        mTrailersAdapter.updateDataSet(trailers);
    }

    @Override
    public void updateReviewsAdapterContent(List<Review> reviews) {
        if (reviews.size() == 0) {
            mReviewsTextView.setVisibility(View.GONE);
        } else {
            mReviewsTextView.setVisibility(View.VISIBLE);
        }
        mReviewsAdapter.updateDataSet(reviews);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
