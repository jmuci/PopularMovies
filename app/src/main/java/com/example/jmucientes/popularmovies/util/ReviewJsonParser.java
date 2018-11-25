package com.example.jmucientes.popularmovies.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.jmucientes.popularmovies.model.VideoTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoTrailerJsonParser {
    private static final String TAG = VideoTrailerJsonParser.class.getName();

    public static List<VideoTrailer> parseTrailersListJsonResponse(String json) throws JSONException {
        if (TextUtils.isEmpty(json)) {
            Log.w(TAG, "Attempted to parse empty JSON response. ");
            return null;
        }
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        List<VideoTrailer> trailerList = new ArrayList<>(resultsArray.length());
        for (int i = 0; i < resultsArray.length(); i++) {
            VideoTrailer trailer = parseTrailerJson(resultsArray.getJSONObject(i));
            if (VideoTrailer.SITE_YOU_TUBE.equals(trailer.getSite())) {
                trailerList.add(trailer);
            }
        }
        return trailerList;    }


    private static VideoTrailer parseTrailerJson(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString("id");
        String key = jsonObject.getString("key");
        String name = jsonObject.getString("name");
        String site = jsonObject.getString("site");

        return new VideoTrailer(id, key, name, site);
    }
}
