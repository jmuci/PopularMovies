package com.example.jmucientes.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Represents a movie Object with most relevant fields.
 * NOTE that implements Serializable, but it should actually implement Parcelable
 * for performance reasons when Bundling a Movie object to pass it to the DetailsActivity.
 */
@Entity(tableName = "Movie")
public class Movie implements Serializable {

    public Movie(String vote_average, String backdrop_path, int id, String title, String overview, String original_language, String release_date, String vote_count, String poster_path) {
        this.vote_average = vote_average;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.original_language = original_language;
        this.release_date = release_date;
        this.vote_count = vote_count;
        this.poster_path = poster_path;
    }

    @PrimaryKey
    @NonNull
    private int id;

    public String getImageUri() {
        return poster_path;
    }
    private String vote_average;

    private String backdrop_path;


    private String title;

    private String overview;

    private String original_language;

    private String release_date;

    private String vote_count;

    private String poster_path;

    public String getVote_average ()
    {
        return vote_average;
    }

    public void setVote_average (String vote_average)
    {
        this.vote_average = vote_average;
    }

    public String getBackdrop_path ()
    {
        return backdrop_path;
    }

    public void setBackdrop_path (String backdrop_path)
    {
        this.backdrop_path = backdrop_path;
    }

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getOverview ()
    {
        return overview;
    }

    public void setOverview (String overview)
    {
        this.overview = overview;
    }

    public String getOriginal_language ()
    {
        return original_language;
    }

    public void setOriginal_language (String original_language)
    {
        this.original_language = original_language;
    }

    public String getRelease_date ()
    {
        return release_date;
    }

    public void setRelease_date (String release_date)
    {
        this.release_date = release_date;
    }

    public String getVote_count ()
    {
        return vote_count;
    }

    public void setVote_count (String vote_count)
    {
        this.vote_count = vote_count;
    }

    public String getPoster_path ()
    {
        return poster_path;
    }

    public void setPoster_path (String poster_path)
    {
        this.poster_path = poster_path;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vote_average = "+vote_average+", backdrop_path = "+backdrop_path+", id = "+id+", title = "+title+", overview = "+overview+", original_language = "+original_language+", release_date = "+release_date+", vote_count = "+vote_count+", poster_path = "+poster_path+"]";
    }

    public boolean notEmpty() {
        return !TextUtils.isEmpty(title) && id != 0 && !TextUtils.isEmpty(poster_path) &&
                !TextUtils.isEmpty(overview) && !TextUtils.isEmpty(vote_average);
    }
}
