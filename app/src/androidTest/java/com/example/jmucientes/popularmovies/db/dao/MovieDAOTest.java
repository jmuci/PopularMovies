package com.example.jmucientes.popularmovies.db.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.jmucientes.popularmovies.db.MovieDataBase;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MovieDAOTest {

    protected MovieDataBase mMovieDataBase;

    @Before
    public void initDb() {
        mMovieDataBase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                MovieDataBase.class
        ).build();
    }

    @After
    public void close(){
        mMovieDataBase.close();
    }

    @Test
    public void testSaveAndGetMovie() throws InterruptedException {

        Movie movie =createMovie(100, "The Godfather: Part II", "\\/bVq65huQ8vHDd1a4Z37QtuyEvpA.jpg");
        mMovieDataBase.movieDAO().insertMovie(movie);

        Movie loadedMovie = LiveDataTestUtil.getValue(mMovieDataBase.movieDAO().getMovieById(100));
        //Movie loadedMovie = mMovieDataBase.movieDAO().getMovieById(ID).getValue();
        assertThat(loadedMovie, notNullValue());
        assertThat(loadedMovie.getTitle(), is("The Godfather: Part II"));
    }

    @Test
    public void testSaveTwoAndGetAllFavorites() throws InterruptedException {

        Movie movie = createMovie(100,"The Godfather: Part II", "\\/bVq65huQ8vHDd1a4Z37QtuyEvpA.jpg");
        mMovieDataBase.movieDAO().insertMovie(movie);

        Movie movie2 = createMovie(200, "The Godfather", "\\/fr265huQ8vHDd1a4Z37QtuykmP0.jpg");
        mMovieDataBase.movieDAO().insertMovie(movie2);

        List<Movie> loadedMovie = LiveDataTestUtil.getValue(mMovieDataBase.movieDAO().getAllFavoriteMovies());
        //List<Movie> loadedMovie = mMovieDataBase.movieDAO().getAllFavoriteMovies().getValue();
        assertThat(loadedMovie.get(0), notNullValue());
        assertThat(loadedMovie.get(0).getTitle(), is("The Godfather: Part II"));

        assertThat(loadedMovie.get(1), notNullValue());
        assertThat(loadedMovie.get(1).getTitle(), is("The Godfather"));
    }

    public static Movie createMovie(int id, String title, String posterPath) {
        String overView = "In the continuing saga of the Corleone crime family, a young Vito Corleone grows up in Sicily and in 1910s New York. In the 1950s, Michael Corleone attempts to expand the family business into Las Vegas, Hollywood and Cuba.\t";
        String backDropPath = "/gLbBRyS7MBrmVUNce91Hmx9vzqI.jpg";
        List<Integer> genreIds = new ArrayList<>();
        genreIds.add(23);
        return new Movie("8.5", backDropPath, id, title, overView, "en",
                "1974-12-20","5125", posterPath);
    }

}