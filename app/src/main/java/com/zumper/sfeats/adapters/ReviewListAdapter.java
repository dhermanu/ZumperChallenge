package com.zumper.sfeats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zumper.sfeats.R;
import com.zumper.sfeats.models.Review;

import java.util.ArrayList;

/**
 * Created by dhermanu on 1/30/17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView authorName, text, rating;

        public ViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView) itemView.findViewById(R.id.author_name);
            text = (TextView) itemView.findViewById(R.id.text);
            rating = (TextView) itemView.findViewById(R.id.rating);
        }
    }

    public ArrayList<Review> mReviews;
    public Context mContext;
    public ReviewListAdapter(ArrayList<Review> mReviews, Context mContext){
        this.mReviews = mReviews;
        this.mContext = mContext;
    }

    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the custon layout
        View reviewView = inflater.inflate(R.layout.list_item_review, parent, false);

        //return a holder instance
        ViewHolder viewHolder = new ViewHolder(reviewView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewListAdapter.ViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        holder.authorName.setText(review.getAuthorName());
        holder.text.setText(review.getText());
        //holder.rating.setText(review.getRating());
    }

    @Override
    public int getItemCount() {
        if(mReviews != null)
            return mReviews.size();
        return 0;
    }

}

