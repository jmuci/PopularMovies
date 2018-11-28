package com.example.jmucientes.popularmovies.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jmucientes.popularmovies.BuildConfig;
import com.example.jmucientes.popularmovies.util.ImageUriUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoviesWebService {

    private static final String HTTP_SCHEME = "http";
    private static final String API_THEMOVIEDB_AUTHORITY = "api.themoviedb.org";
    private static final String MOVIE_PATH = "movie";

    private static final String API_KEY_PARAM = "api_key";
    private static final String PERSONAL_API_KEY = BuildConfig.TheMovieApiKey;
    private static final String TAG = ImageUriUtils.class.getName();
    private static final String PATH = "t/p";
    private static final String IMAGES_AUTH = "image.tmdb.org";
    private static final String VERSION_SEGMENT = "3";

    public static final String PAGE_KEY = "page";
    public static final String DEFAULT_TO_FIRST_PAGE = "1";

    public static final String TOP_RATED_END_POINT = "top_rated";
    public static final String MOST_POPULAR_END_POINT = "popular";
    private static final String MOVIE_TRAILERS = "videos";
    private static final String MOVIE_REVIEWS = "reviews";


    /**
     * This method will build a complete URI to hit either the top movies or the popular movies end point.
     * It will default to just requesting the first page.
     * @param endPoint String
     * @return full Uri
     */
    @NonNull
    public Uri buildRequestUriForMoviesWithEndPoint(@NonNull String endPoint) {
        return buildRequestUriForMoviesWithEndPoint(endPoint, DEFAULT_TO_FIRST_PAGE);
    }

    /**
     * This method will build a complete URI to hit either the top movies or the popular movies end point.
     * It will also take the results page as parameter..
     * @param endPoint String
     * @param page String
     * @return full Uri
     */
    @NonNull
    public Uri buildRequestUriForMoviesWithEndPoint(@NonNull String endPoint, @NonNull String page){
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
     * This method will build a complete URI to hit either the movie trailers end point.
     * @param id int
     * @return full Uri
     */
    @NonNull
    public Uri buildRequestUriForMovieTrailers(int id){
        //Uri http://api.themoviedb.org/3/movie/240/videos?api_key=0c6313bbf2f22242126a23d1d43f83cd
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme(HTTP_SCHEME)
                .authority(API_THEMOVIEDB_AUTHORITY)
                .appendPath(VERSION_SEGMENT)
                .appendPath(MOVIE_PATH)
                .appendPath(Integer.toString(id))
                .appendPath(MOVIE_TRAILERS)
                .appendQueryParameter(API_KEY_PARAM, PERSONAL_API_KEY)
                .build();
    }


    /**
     * This method will build a complete URI to hit either the movie reviews end point.
     * @param id int
     * @return full Uri
     */
    @NonNull
    public Uri buildRequestUriForMovieReviews(int id){
        //Uri http://api.themoviedb.org/3/movie/240/reviews?api_key=0c6313bbf2f22242126a23d1d43f83cd
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme(HTTP_SCHEME)
                .authority(API_THEMOVIEDB_AUTHORITY)
                .appendPath(VERSION_SEGMENT)
                .appendPath(MOVIE_PATH)
                .appendPath(Integer.toString(id))
                .appendPath(MOVIE_REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, PERSONAL_API_KEY)
                .build();
    }

    /**
     * Make a network request using the requestUri using OkHttp client.
     * This method shouldn't be run on the Main Thread.
     *
     * If the request fails, it will log an error and return null.
     *
     * @param requestUri String
     * @return response String
     * @throws IOException
     */
    @Nullable
    public String makeRequest(@NonNull Uri requestUri) throws IOException {
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

    public interface GeneralParser {
        void parseResultsAndUpdateAdapter(String jsonResponse) throws JSONException;
    }

    public void executeBackgroundNetworkRequest(Uri requestUri, final MoviesWebService.GeneralParser parser) {

        Single<String> mResponseSingle = makeRequestSingleObvservable(requestUri);

        mResponseSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called. Succeeded collecting the videos");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called. Error : " + e.getMessage();
                        Log.e(TAG, msg);
                    }

                    @Override
                    public void onNext(String jsonString) {
                        Log.d(TAG, "onNext() called.");
                        //parseTrailersResultsAndUpdateAdapter(jsonString);
                        try {
                            parser.parseResultsAndUpdateAdapter(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String msg = "JSON parsing error! Error : " + e.toString();
                            Log.e(TAG, msg);
                        }

                    }
                });
    }

    /**
     *
     * @param requestUri
     * @return
     */

    private Single<String> makeRequestSingleObvservable(@NonNull final Uri requestUri) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    return makeRequest(requestUri);
                } catch (IOException e) {
                    String msg = "IOException. Failed to fetch movies list. Error: " + e.getMessage();
                    Log.e(TAG, msg);
                    return null;
                }
            }
        });
    }


}
