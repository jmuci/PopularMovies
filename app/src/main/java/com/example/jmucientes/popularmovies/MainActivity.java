package com.example.jmucientes.popularmovies;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.example.jmucientes.popularmovies.model.MoviesViewModel;
import com.example.jmucientes.popularmovies.presenters.MainActivityPresenter;
import com.example.jmucientes.popularmovies.view.MainActivityViewBinder;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import static com.example.jmucientes.popularmovies.presenters.MainActivityPresenter.TOP_RATED_MOVIES_TITLE;

// TODO (Opt) Load more films on Scroll https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
// TODO (Opt) Unit Tests
// TODO (Opt) Use LiveData for all endpoints

/**
 * Main entry point class for the PopularMovies App.
 * In this class mainly the updating of views is dealt with.
 */
public class MainActivity extends DaggerAppCompatActivity implements MainActivityViewBinder {

    public static final int NAM_COLUMN_PORTRAIT = 2;
    public static final int NAM_COLUMN_LANDSCAPE = 4;
    private static final String TAG = MainActivity.class.getName();
    private static final String TOOLBAR_TITLE = "toolbar_title";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_dialog_view)
    View mErrorView;
    @BindView(R.id.error_msg_tv)
    TextView mErrorMessageTv;

    // Dagger 2 Injections
    @Inject
    MoviesAdapter mAdapter;
    @Inject
    MainActivityPresenter mMainActivityPresenter;
    @Inject
    List<Movie> mMovieList; //Initialized Empty //TODO Remove this list.
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MoviesViewModel mMoviesViewModel;
    private String mToolBarTilte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpRecyclerView();
        mMoviesViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MoviesViewModel.class);
        mMoviesViewModel.getTopMovieList().observe(this, movies -> {
            updateAdapterContent(movies, false);
        });

        //TODO This crashes on rotation. Can title use live Data?
        if (savedInstanceState != null) {
            String title = savedInstanceState.getString(TOOLBAR_TITLE);
            setToolBarTitle(title);
            mMainActivityPresenter.setCurrentSortOptionBasedOnTitle(title);
        } else {
            setToolBarTitle(TOP_RATED_MOVIES_TITLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMainActivityPresenter.getCurrentSortOption().equals(MainActivityPresenter.SHOW_FAVORITES)) {
            boolean success = mMainActivityPresenter.showOnlyFavoriteMovies();
            if (!success) {
                //mAdapter.clearDataSetWithoutNotifyDataSetChanged();
                //mMainActivityPresenter.requestTopRatedMoviesFromTheMovieDB();
            }
        }
    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        if (isPortraitScreenConfiguration()) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, NAM_COLUMN_PORTRAIT));
        } else { // Landscape configuration user more columns.
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, NAM_COLUMN_LANDSCAPE));
        }

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
            if (!MainActivityPresenter.SHOW_MOST_POPULAR.equals(mMainActivityPresenter.getCurrentSortOption())) {
                mAdapter.clearDataSetWithoutNotifyDataSetChanged();
                mMainActivityPresenter.requestMostPopularMoviesFromTheMovieDB();
            }
            return true;
        }

        if (id == R.id.order_top_movies) {
            if (!MainActivityPresenter.SHOW_TOP_RATED.equals(mMainActivityPresenter.getCurrentSortOption())) {
                mAdapter.clearDataSetWithoutNotifyDataSetChanged();
                mMainActivityPresenter.requestTopRatedMoviesFromTheMovieDB();
            }
            return true;
        }

        if (id == R.id.order_favorites) {
            if (!MainActivityPresenter.SHOW_FAVORITES.equals(mMainActivityPresenter.getCurrentSortOption())) {
                mMainActivityPresenter.showOnlyFavoriteMovies();
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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setToolBarTitle(String title) {
        mToolBarTilte = title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TOOLBAR_TITLE, mToolBarTilte);
        super.onSaveInstanceState(outState);
    }
}
