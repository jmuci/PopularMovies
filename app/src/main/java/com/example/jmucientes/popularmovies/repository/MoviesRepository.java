package com.example.jmucientes.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmucientes.popularmovies.db.MovieDAO;
import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.network.MoviesWebService;
import com.example.jmucientes.popularmovies.util.AppExecutors;
import com.example.jmucientes.popularmovies.util.MovieJsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ApplicationScope
public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getName();
    public static final int MILIS_IN_ONE_SEC = 1000;
    public static final int SECS_IN_MIN = 60;
    private static final long FRESH_TIME_OUT = 5 * SECS_IN_MIN * MILIS_IN_ONE_SEC;

    private final MoviesWebService mMoviesWebService;
    private final MovieListInMemoryCache mMovieListCacheTopRated;
    private final MovieListInMemoryCache mMovieListCacheMostPopular;
    private final MovieDAO mMovieDAO;
    private final AppExecutors mExecutors;

    private MutableLiveData<List<Movie>> mCachedMovieList;

    @Inject
    public MoviesRepository(MoviesWebService moviesWebService,
                            @Named("topRatedCache") MovieListInMemoryCache movieListCacheTopRated,
                            @Named("mostPopularCache") MovieListInMemoryCache movieListCacheMostPopular,
                            MovieDAO movieDAO,
                            AppExecutors executors) {
        mMoviesWebService = moviesWebService;
        mMovieListCacheTopRated = movieListCacheTopRated;
        mMovieListCacheMostPopular = movieListCacheMostPopular;
        mMovieDAO = movieDAO;
        mExecutors = executors;
        mCachedMovieList = new MutableLiveData<>();
        getFavoriteMoviesListNew();
    }

    public void getTopRatedMovieList(MutableLiveData<List<Movie>> movieList) {
        Log.d(TAG, "getTopRatedMovieList() called.");
        getTopRatedMovieListFromEndpoint(movieList);
    }

    public void getPopularMovieList(MutableLiveData<List<Movie>> movieList) {
        Log.d(TAG, "getTopRatedMovieList() called.");
        getPopularMovieListFromEndpoint(movieList);
    }

    private void refreshMovieList(MutableLiveData<List<Movie>> movieList) {
        mExecutors.networkIO().execute(() -> {
            Log.d(TAG, "refreshMovies() called.");
            //boolean hasRecentList = hasRecentList(FRESH_TIME_OUT);
            //TODO this never hits the DB, how to ensure DB data is fresh?
            List<Movie> latestMovieList = mMovieListCacheTopRated.getMoviesList();
            if (latestMovieList == null) {
                Log.d(TAG, "No Cache, calling network.");
                getTopRatedMovieListFromEndpoint(movieList);
            } else {
                Log.d(TAG, "Calling db. mMovieDAO.getAllFavoriteMovies() and setting value");
                movieList.setValue(mMovieDAO.getAllFavoriteMovies().getValue());
            }
        });
    }

    private void getTopRatedMovieListFromEndpoint(MutableLiveData<List<Movie>> movieList) {
        Log.d(TAG, "Calling endpoint");
        //List<Movie> latestMovieList = mMovieListCacheTopRated.getMoviesList();
/*        if (latestMovieList != null) { // Cached content
            movieList.setValue(latestMovieList);
        } else { // Network request*/
        Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "1");
        Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "2");
        Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.TOP_RATED_END_POINT, "3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3, movieList);

    }

    private void getPopularMovieListFromEndpoint(MutableLiveData<List<Movie>> movieList) {
        List<Movie> latestMovieList = mMovieListCacheMostPopular.getMoviesList();
/*        if (latestMovieList != null) { // Cached content
            Log.d(TAG, "Returning cached content");
            movieList.setValue(latestMovieList);
        } else { // Network request*/
        Log.d(TAG, "Calling endpoint");
        Uri requestUri = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "1");
        Uri requestUri2 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "2");
        Uri requestUri3 = mMoviesWebService.buildRequestUriForMoviesWithEndPoint(MoviesWebService.MOST_POPULAR_END_POINT, "3");
        executeBackgroundNetworkRequest(requestUri, requestUri2, requestUri3, movieList);

    }

    private void executeBackgroundNetworkRequest(Uri requestUri, Uri requestUri2, Uri requestUri3, MutableLiveData<List<Movie>> data) {
        Single<String> requestSingle = makeMoviesRequestSingleObvservable(requestUri);
        Single<String> requestSingle2 = makeMoviesRequestSingleObvservable(requestUri2);
        Single<String> requestSingle3 = makeMoviesRequestSingleObvservable(requestUri3);
        List<Movie> mergedMovieList = new ArrayList<>(60);

        // Request the 3 first pages and merge the response.
        Single.merge(
                requestSingle,
                requestSingle2,
                requestSingle3)
                .subscribeOn(Schedulers.io()) // Run on the background.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called.");
                        mMovieListCacheTopRated.updateInMemoryCache(mergedMovieList);
                        data.setValue(mergedMovieList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called. Error : " + e.getMessage();
                        Log.e(TAG, msg);
                        //mViewBinderWR.get().showErrorMessage(msg);
                    }

                    @Override
                    public void onNext(String jsonString) {
                        Log.d(TAG, "onNext() called.");
                        List<Movie> movieList = null;
                        try {
                            movieList = MovieJsonParser.parseMovieListJsonResponse(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String msg = "JSON parsing error! Error : " + e.toString();
                            Log.e(TAG, msg);
                        }
                        //TODO Error handling?
                        if (movieList == null || movieList.size() == 0) {
                            Log.w(TAG, "The movie list was empty or null.");
                            return;
                        }
                        Log.d(TAG, "Adding movies to mergedlist. MergedList .siz() : " + mergedMovieList.size());
                        mergedMovieList.addAll(movieList);
                        //mMovieListCacheTopRated.updateInMemoryCache(movieList);

                        //data.setValue(movieList);
                    }
                });
    }

    private Single<String> makeMoviesRequestSingleObvservable(@NonNull final Uri requestUri) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    return mMoviesWebService.makeRequest(requestUri);
                } catch (IOException e) {
                    String msg = "IOException. Failed to fetch movies list. Error: " + e.getMessage();
                    Log.e(TAG, msg);
                    //mViewBinderWR.get().showErrorMessage(msg);
                    return null;
                }
            }
        });
    }

    public void deleteMovieFromFavorites(int id) {
        mExecutors.diskIO().execute(() -> mMovieDAO.deleteMoveFromDB(id));
    }

    public void saveMovieToFavorites(Movie movie) {
        mExecutors.diskIO().execute(() -> mMovieDAO.saveMovieToDB(movie));
    }

    public void updateMovieFavoriteStatus(Movie movie, MutableLiveData<Boolean> isMovieFavorite) {
/*        mExecutors.diskIO().execute(() -> {
            if (mMovieDAO.getMovieById(movie.getId()).getValue() != null) {
                Log.d(TAG, "Deleting movie from favs DB ");
                mMovieDAO.deleteMoveFromDB(movie.getId());
            } else {
                Log.d(TAG, "Adding movie to favs DB ");
                mMovieDAO.saveMovieToDB(movie);
            }
        });*/

        Single.just(mMovieDAO.getMovieById(movie.getId()).getValue())
                .subscribeOn(Schedulers.io())
                .doOnEach((foundMovie) -> { // Update DB accordingly on the io thread.
                    if (foundMovie.getValue() != null) {
                        Log.d(TAG, "Deleting movie from favs DB ");
                        mMovieDAO.deleteMoveFromDB(movie.getId());
                    } else {
                        Log.d(TAG, "Adding movie to favs DB ");
                        mMovieDAO.saveMovieToDB(movie);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called updtaintFavoriteStatus Error : " + e.getMessage();
                        Log.e(TAG, msg);
                    }

                    @Override
                    public void onNext(Movie movie) {
                        Log.d(TAG, "Setting isFavoriteValue to MovieID: " + movie);
                        isMovieFavorite.setValue(movie != null);
                    }
                });

    }

    public LiveData<Movie> fetchMovieBy(int id) {
        return mMovieDAO.getMovieById(id);
    }

    public void fetchMovieAsync(int id, MutableLiveData<Movie> movieMutableLiveData) {
        mExecutors.diskIO().execute(() -> movieMutableLiveData.setValue(mMovieDAO.getMovieById(id).getValue()));
    }

    public MutableLiveData<List<Movie>> getFavoriteMoviesList() {
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(mMovieDAO.getAllFavoriteMovies().getValue());
        return mutableLiveData;
    }

    public void getFavoriteMoviesListAsync(MutableLiveData<List<Movie>> movieList) {
        mExecutors.diskIO().execute(()-> {
            movieList.postValue(mMovieDAO.getAllFavoriteMovies().getValue());
            Log.d(TAG, "Executed getAllFavoriteMovies() in Thread: " + Thread.currentThread().getName());
        });
    }

    @NonNull
    public MutableLiveData<List<Movie>> getCachedFavoriteMovieList() {
        if (mCachedMovieList == null || mCachedMovieList.getValue() == null ) {
            Log.e(TAG, "Failed to getCachedFavoriteMovieList(). The Cached Live Data is null");
            MutableLiveData<List<Movie>> emptyModel = new MutableLiveData<>();
            emptyModel.setValue(new ArrayList<>());
            return emptyModel;
        }
        return mCachedMovieList;
    }

    private void getFavoriteMoviesListNew() {
        mExecutors.diskIO().execute(()-> {
            mCachedMovieList.postValue(mMovieDAO.getAllFavoriteMovies().getValue());
            Log.d(TAG, "Executed getAllFavoriteMovies() in Thread: " + Thread.currentThread().getName());
            Log.d(TAG, "Content of mCachedMoviesList:  " + mCachedMovieList.getValue());
        });
    }

    public void isMovieFavoriteAsync(int id, MutableLiveData<Boolean> isMovieFavorite) {
        //mExecutors.diskIO().execute(() -> isMovieFavoriteAsync.setValue(mMovieDAO.getMovieById(id).getValue() != null));
        Single.just(mMovieDAO.getMovieById(id).getValue() != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called checking isMovieFav. Error : " + e.getMessage();
                        Log.e(TAG, msg);
                    }

                    @Override
                    public void onNext(Boolean isFavo) {
                        isMovieFavorite.setValue(isFavo);
                    }
                });
    }

    public void getFavoriteMoviesList(MutableLiveData<List<Movie>> favoriteMoviesList) {
        //mExecutors.diskIO().execute(() -> favoriteMoviesList.setValue(mMovieDAO.getAllFavoriteMovies().getValue()));
        Single.just(mMovieDAO.getAllFavoriteMovies().getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg = "onError() called checking isMovieFav. Error : " + e.getMessage();
                        Log.e(TAG, msg);
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        favoriteMoviesList.setValue(movies);
                    }
                });
    }
}