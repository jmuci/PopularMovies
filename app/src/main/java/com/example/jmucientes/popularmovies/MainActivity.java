package com.example.jmucientes.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Main entry point class for the PopularMovies App
 */
    // TODO(1) Add dependencies: Picasso and Butterknife
    // TODO(2) Make basic app structure

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
