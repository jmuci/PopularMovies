package com.example.jmucientes.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.jmucientes.popularmovies.model.Movie;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDAO {

    @Insert(onConflict = REPLACE)
    void saveMovieToDB(Movie movie);

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllFavoriteMovies();

}
