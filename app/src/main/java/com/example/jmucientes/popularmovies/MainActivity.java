package com.example.jmucientes.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main entry point class for the PopularMovies App
 */

    // TODO Design detailed view
    // TODO Load movie info into detailed view
    // TODO Sort the movies
    // TODO (Opt) Introduce Dagger
    // TODO (Opt) Load more films
    // TODO (Opt) Unit Tests


public class MainActivity extends AppCompatActivity implements MainActivityViewBinder{

    public static final int NUM_COLUMS = 2;
    private static final String TAG = MainActivity.class.getName();
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.error_dialog_view) View mErrorView;
    @BindView(R.id.error_msg_tv) TextView mErrorMessageTv;
    private MoviesAdapter mAdapter;
    MainActivityPresenter mMainActivityPresenter;
    List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMainActivityPresenter = new MainActivityPresenter(this);
        mMainActivityPresenter.requestTopRatedMoviesFromTheMovieDB();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_COLUMS));

        //Initialize DataSet Empty Until the results come.
        mMovieList = new ArrayList<>(0);
        mMovieList = getPlaceHolderMovieList();
        mAdapter = new MoviesAdapter(mMovieList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Movie> getPlaceHolderMovieList() {
        List<Movie> movieList = new ArrayList<>(6);
        for (int i = 0; i < 4; i++) {
            movieList.add(new Movie("", "", 0, "", "", "", "", "", ""));
        }
        return movieList;
    }

    @Override
    public void updateAdapterContent(List<Movie> movies) {
        if (movies != null && movies.size() > 0) {
            mAdapter.updateDataSet(movies);
        } else {
            Log.e(TAG, "Error trying to update adapter with response movies");
        }

    }

    @Override
    public void showErrorMessage(String msg) {
        mErrorView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(msg)) {
            mErrorMessageTv.setText("Are you offline?");
        } else {
            mErrorMessageTv.setText(msg);
        }
    }
}
