package com.example.jmucientes.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jmucientes.popularmovies.model.Movie;
import com.example.jmucientes.popularmovies.util.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public static final String MOVIE_KEY = "movie_key";
    private static final String TAG = MoviesAdapter.class.getName();
    private List<Movie> mDataSet;
    private Context mContext;

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            openMovieDetailsActivity(getAdapterPosition());
        }

        private void openMovieDetailsActivity(int adapterPosition) {

            final Class destinationActivity = MoviesDetails.class;
            Intent intent = new Intent(mContext, destinationActivity);
            intent.putExtra(MOVIE_KEY, mDataSet.get(adapterPosition));
            mContext.startActivity(intent);
        }

    }
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
                .placeholder(R.drawable.loader)
                .error(R.drawable.broken_img)
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


}
