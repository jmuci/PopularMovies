package com.example.jmucientes.popularmovies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmucientes.popularmovies.adapter.MoviesAdapter;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.presenters.MovieDetailsPresenter;
import com.example.jmucientes.popularmovies.util.Network;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesDetailsActivity extends AppCompatActivity {


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
    MovieDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);

        mPresenter = new MovieDetailsPresenter(this);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
            Picasso.with(this)
                    .load(Network.getFullyQualifiedImageUri(mMovie.getBackdrop_path(), Network.IMAGE_SIZE_W_500))
                    .error(R.drawable.baseline_error_black_36)
                    .placeholder(R.drawable.baseline_cloud_download_black_36)
                    .into(mImageBackdropView);

            Picasso.with(this)
                    .load(Network.getFullyQualifiedImageUri(mMovie.getPoster_path()))
                    .error(R.drawable.baseline_error_black_36)
                    .placeholder(R.drawable.baseline_cloud_download_black_36)
                    .into(mPosterView);
        }
    }
}
