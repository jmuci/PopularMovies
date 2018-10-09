package com.example.jmucientes.popularmovies.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Network {
    private static final String HTTP_SCHEME = "http";
    private static final String API_THEMOVIEDB_AUTHORITY = "api.themoviedb.org";
    private static final String MOVIE_PATH = "movie";
    public static final String TOP_RATED_END_POINT = "top_rated";
    public static final String MOST_POPULAR_END_POINT = "popular";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PERSONAL_API_KEY = "0c6313bbf2f22242126a23d1d43f83cd";
    private static final String TAG = Network.class.getName();
    // TODO This should be an ENUM to guarentee a valid size
    public static final String IMAGE_SIZE_W_185 = "w185";
    public static final String IMAGE_SIZE_W_342 = "w342";
    public static final String IMAGE_SIZE_W_500 = "w500";
    private static final String PATH = "t/p";
    private static final String IMAGES_AUTH = "image.tmdb.org";
    private static final String VERSION_SEGMENT = "3";
    public static final String PAGE_KEY = "page";
    public static final String DEFAULT_TO_FIRST_PAGE = "1";

    @NonNull
    public static Uri buildRequestUriForMoviesWithEndPoint(String endPoint) {
        return buildRequestUriForMoviesWithEndPoint(endPoint, DEFAULT_TO_FIRST_PAGE);
    }

    @NonNull
    public static Uri buildRequestUriForMoviesWithEndPoint(String endPoint, String page){
        //Uri http://api.themoviedb.org/3/movie/top_rated?api_key=0c6313bbf2f22242126a23d1d43f83cd
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme(HTTP_SCHEME)
                .authority(API_THEMOVIEDB_AUTHORITY)
                .appendPath(VERSION_SEGMENT)
                .appendPath(MOVIE_PATH)
                .appendPath(endPoint)
                .appendQueryParameter(API_KEY_PARAM, PERSONAL_API_KEY)
                .appendQueryParameter(PAGE_KEY, page)
                .build();
    }

    /**
     * The base URL will look like: http://image.tmdb.org/t/p/.
     * Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
     * And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”
     * @param imagePath
     * @return
     */
    public static Uri getFullyQualifiedImageUri(String imagePath) {
        return getFullyQualifiedImageUri(imagePath, IMAGE_SIZE_W_185);
    }

    public static Uri getFullyQualifiedImageUri(String imagePath, String imageSize) {
        if (imagePath.length() > 0 && '/' == (imagePath.charAt(0))) { //Remove extra slash
            imagePath = imagePath.substring(1, imagePath.length());
        }
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme(HTTP_SCHEME)
                .authority(IMAGES_AUTH)
                .path(PATH)
                .appendPath(imageSize)
                .appendPath(imagePath)
                .build();
    }

        @Nullable
    public static String makeRequest(Uri requestUri) throws IOException {
        final OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(requestUri.toString()).build();
        Response response =  httpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().string();
        } else {
            Log.e(TAG, "Something went wrong and the request failed. Http code: " + response.code());
            return null;
        }
    }
}
