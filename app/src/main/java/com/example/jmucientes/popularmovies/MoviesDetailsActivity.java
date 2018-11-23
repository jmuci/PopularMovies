package com.example.jmucientes.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jmucientes.popularmovies.adapter.MoviesAdapter;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.Network;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * This class is responsible for showing the details of the Movie.
 * It has a different layout for landscape mode.
 * The layout defines a collapsing toolbar, where the collapsing toolbar is decorated
 * with a backdrop image from the movie.
 */
public class MoviesDetailsActivity extends DaggerAppCompatActivity {


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

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        if (startingIntent != null && startingIntent.getSerializableExtra(MoviesAdapter.MOVIE_KEY) != null) {
            mMovie = (Movie) startingIntent.getSerializableExtra(MoviesAdapter.MOVIE_KEY);
            populateUI();
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        setUpToolBar();
        appBarLayout.setMinimumHeight(R.dimen.event_entity_appbar_height);
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
                    .load(Network.getFullyQualifiedImageUri(mMovie.getBackdrop_path(), Network.IMAGE_SIZE_W_500))
                    .error(R.drawable.baseline_error_black_36)
                    .placeholder(R.drawable.baseline_cloud_download_black_36)
                    .into(mImageBackdropView);

            // Set poster image in Detailed view with Higher Resolution
            Picasso.with(this)
                    .load(Network.getFullyQualifiedImageUri(mMovie.getPoster_path(), Network.IMAGE_SIZE_W_500))
                    .error(R.drawable.baseline_error_black_36)
                    .placeholder(R.drawable.baseline_cloud_download_black_36)
                    .into(mPosterView);
        }
    }
}
