package com.example.jmucientes.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmucientes.popularmovies.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    public static final String MOVIE_KEY = "trailer_key";

    private static final String TAG = TrailersAdapter.class.getName();
    private List<String> mDataSet;
    private Context mContext;

    public TrailersAdapter() {
        mDataSet = new ArrayList<>();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;
        ImageButton mPlayButton;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.trailer_title);
            mPlayButton = itemView.findViewById(R.id.play_button);
            mPlayButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Will open youtube", Toast.LENGTH_SHORT).show();
            playTrailerFromYouTube(getAdapterPosition());
        }

        private void playTrailerFromYouTube(int adapterPosition) {
            final Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDataSet.get(adapterPosition)));
            mContext.startActivity(playVideoIntent);
        }
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View trailerView = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_view_trailer_item, parent, false);
        return new TrailerViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.mTextView.setText(String.format(Locale.getDefault(), "Trailer %d", position + 1));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void updateDataSet(@NonNull List<String> trailers) {
        mDataSet = trailers;
    }
}
