package com.example.jmucientes.popularmovies.model;

public class Movie {
    private String mImageUri;

    public Movie(String imageUri) {
        mImageUri = imageUri;
    }

    public String getImageUri() {
        return mImageUri;
    }
}
