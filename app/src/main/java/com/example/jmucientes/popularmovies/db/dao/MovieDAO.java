package com.example.jmucientes.popularmovies.db.dao;

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
    long saveMovieToDB(Movie movie);

    @Query("SELECT * FROM Movie WHERE id = :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("DELETE FROM Movie WHERE id = :movieId")
    void deleteMoveFromDB(int movieId);
}
