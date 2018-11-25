package com.example.jmucientes.popularmovies.presenters;

import com.example.jmucientes.popularmovies.MoviesDetailsActivity;
import com.example.jmucientes.popularmovies.view.MovieDetailsViewBinder;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsPresenter {

    private MovieDetailsViewBinder mDetailsViewBinder;

    public MovieDetailsPresenter(MoviesDetailsActivity viewBinder) {
        mDetailsViewBinder = viewBinder;
    }

    public void requestTrailersForMovie(int id) {
        List<String> mockData = new ArrayList<>();
        mockData.add("https://www.youtube.com/watch?v=zSWdZVtXT7E");
        mockData.add("https://www.youtube.com/watch?v=oJzwEVbFwMQ");
        mockData.add("https://www.youtube.com/watch?v=zSWdZVtXT7E");

        mDetailsViewBinder.updateAdapterContent(mockData);
    }
}
