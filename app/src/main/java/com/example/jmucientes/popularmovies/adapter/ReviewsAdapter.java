package com.example.jmucientes.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jmucientes.popularmovies.R;
import com.example.jmucientes.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewsAdapter.class.getName();
    private List<Review> mDataSet;
    private Context mContext;

    public ReviewsAdapter() {
        mDataSet = new ArrayList<>();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mAuthorTV;
        TextView mContentTV;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTV = itemView.findViewById(R.id.review_author);
            mContentTV = itemView.findViewById(R.id.review_content);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View trailerView = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_view_review_item, parent, false);
        return new ReviewViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.mAuthorTV.setText(mDataSet.get(position).getAuthor());
        holder.mContentTV.setText(mDataSet.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void updateDataSet(@NonNull List<Review> trailers) {
        Log.d(TAG, "Updating Dataset");
        mDataSet = trailers;
        notifyDataSetChanged();
    }
}
