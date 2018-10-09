package com.example.jmucientes.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.Network;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main entry point class for the PopularMovies App
 */

    // DONE Design detailed view
    // DONE Load movie info into detailed view
    // DONE Add menu item
    // DONE Sort the movies
    // TODO (Opt) Introduce Dagger
    // TODO (Opt) Load more films on Scroll
    // TODO (Opt) Unit Tests
    // TODO Screen rotation, save adapter and do not request again
    // TODO Screen rotation: user grid with 3 or 4 columns
    // TODO Run linter
    // TODO Clean Arch


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
        mAdapter = new MoviesAdapter(mMovieList);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.open_order_most_pop) {
            if (!mMainActivityPresenter.getCurrentSortOption().equals(Network.MOST_POPULAR_END_POINT)) {
                mAdapter.clearDataSetWithoutNotifyDataSetChanged();
                mMainActivityPresenter.requestMostPopularMoviesFromTheMovieDB();
            }
            return true;
        }

        if (id == R.id.order_top_movies) {
            if (!mMainActivityPresenter.getCurrentSortOption().equals(Network.TOP_RATED_END_POINT)) {
                mAdapter.clearDataSetWithoutNotifyDataSetChanged();
                mMainActivityPresenter.requestTopRatedMoviesFromTheMovieDB();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * View Binder Implementation
     * @param movies
     */

    @Override
    public void updateAdapterContent(List<Movie> movies) {
        updateAdapterContent(movies, false);
    }

    @Override
    public void updateAdapterContent(List<Movie> movies, boolean appendItemsToDataSet) {
        if (movies != null && movies.size() > 0) {
            if (appendItemsToDataSet) {
                mAdapter.appendItemsToDataSet(movies);
            } else {
                mAdapter.updateDataSet(movies);
            }
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
