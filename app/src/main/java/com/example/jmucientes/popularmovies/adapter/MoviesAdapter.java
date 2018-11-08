package com.example.jmucientes.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jmucientes.popularmovies.MoviesDetailsActivity;
import com.example.jmucientes.popularmovies.R;
import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.Network;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * Adapter class with all the movies that are retrieved from the Movie DB
 * for a given endpoint.
 * The DataSet will be preserved when the screen Rotates.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public static final String MOVIE_KEY = "movie_key";
    private static final String TAG = MoviesAdapter.class.getName();
    private List<Movie> mDataSet;
    private Context mContext;

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            openMovieDetailsActivity(getAdapterPosition());
        }
        private void openMovieDetailsActivity(int adapterPosition) {

            final Class destinationActivity = MoviesDetailsActivity.class;
            Intent intent = new Intent(mContext, destinationActivity);
            intent.putExtra(MOVIE_KEY, mDataSet.get(adapterPosition));
            mContext.startActivity(intent);
        }


    }

    @Inject
    public MoviesAdapter(List <Movie> dataSet) {
        mDataSet = dataSet;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View imageView = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_view_item, parent, false);
        return new MovieViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Uri fullImageUri = Network.getFullyQualifiedImageUri(mDataSet.get(position).getImageUri());
        Picasso.with(mContext)
                .load(fullImageUri)
                .placeholder(R.drawable.baseline_cloud_download_black_36)
                .error(R.drawable.baseline_error_black_36)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void updateDataSet(List<Movie> movies) {
        mDataSet.clear();
        mDataSet = movies;
        notifyDataSetChanged();
    }

    public void appendItemsToDataSet(List<Movie> movies) {
        mDataSet.addAll(movies);
        Log.d(TAG, "appendItemsToDataSet() called. New dataSet size: " + mDataSet.size());
        notifyDataSetChanged();
    }

    public void clearDataSetWithoutNotifyDataSetChanged() {
        mDataSet.clear();
    }

    public List<Movie> getDataSet() {
        return mDataSet;
    }

}
