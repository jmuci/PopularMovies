package com.example.jmucientes.popularmovies;

import android.content.res.Configuration;
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

import com.example.jmucientes.popularmovies.adapter.MoviesAdapter;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.presenters.MainActivityPresenter;
import com.example.jmucientes.popularmovies.util.Network;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

// TODO (Opt) Introduce Dagger
// TODO (Opt) Load more films on Scroll https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
// TODO (Opt) Unit Tests
/**
 * Main entry point class for the PopularMovies App.
 * In this class mainly the updating of views is dealt with.
 */
public class MainActivity extends AppCompatActivity implements MainActivityViewBinder {

    public static final int NUM_COLUMS_PORTRAIT = 2;
    public static final int NUM_COLUMS_LANDSCAPE = 4;
    private static final String TAG = MainActivity.class.getName();
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.error_dialog_view) View mErrorView;
    @BindView(R.id.error_msg_tv) TextView mErrorMessageTv;

    @Inject
    MoviesAdapter mAdapter;
    @Inject
    MainActivityPresenter mMainActivityPresenter;
    //List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //mMainActivityPresenter = new MainActivityPresenter(this);
        setUpRecyclerView();

        // If we had saved configuration, restore the dataSet without hitting the backend.
        List<Movie> retainedDataSet = (List<Movie>) getLastCustomNonConfigurationInstance();
        if (retainedDataSet != null && retainedDataSet.size() > 0) {
            mAdapter.updateDataSet(retainedDataSet);
        } else {
            mMainActivityPresenter.requestTopRatedMoviesFromTheMovieDB();
        }

    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        if (isPortraitScreenConfiguration()) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_COLUMS_PORTRAIT));
        } else { // Landscape configuration user more columns.
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_COLUMS_LANDSCAPE));
        }
        //Initialize DataSet Empty Until the results come.
        //mMovieList = new ArrayList<>(0);
        //mAdapter = new MoviesAdapter(mMovieList);

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

        // To optimize, we only send a request if the user clicks on a different sort mode.
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
     * ------------------------------
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
            mErrorMessageTv.setText(R.string.offline_error_msg);
        } else {
            mErrorMessageTv.setText(msg);
        }
    }

    @Override
    public List<Movie> onRetainCustomNonConfigurationInstance() {
        // We save the whole dataSet in case of a config change to minimize the amount of backend requests.
        return mAdapter.getDataSet();
    }

    private boolean isPortraitScreenConfiguration() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
