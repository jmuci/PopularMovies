package com.example.jmucientes.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jmucientes.popularmovies.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main entry point class for the PopularMovies App
 */
    // TODO(1) Add dependencies: Picasso and Butterknife
    // TODO(2) Make basic app structure

public class MainActivity extends AppCompatActivity {

    public static final int NUM_COLUMS = 2;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_COLUMS));

        mAdapter = new MoviesAdapter(generateFakeMoviesSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    //TODO Replace with real data
    private Movie[] generateFakeMoviesSet() {
        String uri = "https://images-na.ssl-images-amazon.com/images/I/81xTx-LxAPL._SL1500_.jpg";
        String uri2 = "https://is3-ssl.mzstatic.com/image/thumb/Video128/v4/16/ec/1d/16ec1d49-bd88-f23c-393d-0a850c0ca1c9/contsched.rwjqgfdx.lsr/268x0w.jpg";
        String uri3 = "https://images-na.ssl-images-amazon.com/images/I/51DT17Z5E7L._SY445_.jpg";
        return new Movie[]{
                new Movie(uri),
                new Movie(uri2),
                new Movie(uri3),
                new Movie(uri),
                new Movie(uri2),
                new Movie(uri3),
                new Movie(uri),
                new Movie(uri2),
                new Movie(uri3),
                new Movie(uri),
                new Movie(uri2),
                new Movie(uri3)
        };
    }
}
