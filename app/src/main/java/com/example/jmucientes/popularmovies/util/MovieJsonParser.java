package com.example.jmucientes.popularmovies.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieJsonParser {
    private static final String TAG = MovieJsonParser.class.getName();

    public static List<Movie> parseMovieListJsonResponse(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            Log.w(TAG, "Attemted to parse empty JSON response. ");
            return null;
        }
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        List<Movie> movieList = new ArrayList<>(resultsArray.length());
        for (int i = 0; i < resultsArray.length(); i++) {
            movieList.add(parseMovieJson(resultsArray.getJSONObject(i)));
        }
        return movieList;
    }

    private static Movie parseMovieJson(JSONObject jsonObject) throws JSONException {

        String voteCount = String.valueOf(jsonObject.getInt("vote_count"));
        int id = jsonObject.getInt("id");
        String voteAverage = String.valueOf(jsonObject.getDouble("vote_average"));
        String title = jsonObject.getString("title");
        String posterPath = jsonObject.getString("poster_path");
        String overview = jsonObject.getString("overview");
        String releaseDate = jsonObject.getString("release_date");
        String backDropPath = jsonObject.getString("backdrop_path");
        String originalLang = jsonObject.getString("original_language");

        return new Movie(
                voteAverage,
                backDropPath,
                id,
                title,
                overview,
                originalLang,
                releaseDate,
                voteCount,
                posterPath
        );
    }
}
