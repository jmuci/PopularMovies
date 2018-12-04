package com.example.jmucientes.popularmovies.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewJsonParser {
    private static final String TAG = ReviewJsonParser.class.getName();

    public static List<Review> parseReviewListJsonResponse(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            Log.w(TAG, "Attempted to parse empty JSON response. ");
            return null;
        }
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        List<Review> reviewList = new ArrayList<>(resultsArray.length());
        for (int i = 0; i < resultsArray.length(); i++) {
            reviewList.add(parseReviewJson(resultsArray.getJSONObject(i)));
        }
        return reviewList;
    }


    private static Review parseReviewJson(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString("id");
        String content = jsonObject.getString("content");
        String author = jsonObject.getString("author");
        String url = jsonObject.getString("url");

        return new Review(id, content, author, url);
    }
}
