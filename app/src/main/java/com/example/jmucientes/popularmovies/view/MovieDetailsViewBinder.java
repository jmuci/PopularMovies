package com.example.jmucientes.popularmovies.view;

import android.content.Context;

import com.example.jmucientes.popularmovies.model.Review;
import com.example.jmucientes.popularmovies.model.VideoTrailer;

import java.util.List;

public interface MovieDetailsViewBinder {
    void updateTrailersAdapterContent(List<VideoTrailer> trailers);
    void updateReviewsAdapterContent(List<Review> reviews);
    Context getContext();
}
