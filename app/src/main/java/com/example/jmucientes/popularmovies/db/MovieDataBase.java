package com.example.jmucientes.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.jmucientes.popularmovies.model.Movie;

@Database(entities = {Movie.class}, exportSchema = false, version = 1)
public abstract class MovieDataBase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
}
