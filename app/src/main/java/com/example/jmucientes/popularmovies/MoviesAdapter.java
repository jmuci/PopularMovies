package com.example.jmucientes.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jmucientes.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Movie[] mDataSet;
    private Context mContext;

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
        }
    }

    public MoviesAdapter(Movie[] dataSet) {
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
        Picasso.with(mContext)
                .load(mDataSet[position].getImageUri())
                .placeholder(R.color.lightGray)
                .error(R.color.colorAccent)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
