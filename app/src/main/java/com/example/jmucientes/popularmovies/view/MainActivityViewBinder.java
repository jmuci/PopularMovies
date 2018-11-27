package com.example.jmucientes.popularmovies.view;

import android.content.Context;

import com.example.jmucientes.popularmovies.model.Movie;

import java.util.List;

public interface MainActivityViewBinder {
    void updateAdapterContent(List<Movie> movies);

    void updateAdapterContent(List<Movie> movies, boolean appendItemsToDataSet);

    void showErrorMessage(String msg);

    Context getContext();

    void setToolBarTitle(String title);
}
