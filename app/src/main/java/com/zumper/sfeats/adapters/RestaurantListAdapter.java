package com.zumper.sfeats.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zumper.sfeats.R;
import com.zumper.sfeats.activities.DetailActivity;
import com.zumper.sfeats.activities.MainActivity;
import com.zumper.sfeats.interfaces.ItemClickListener;
import com.zumper.sfeats.models.Restaurant;

import java.util.ArrayList;

/**
 * Created by dhermanu on 1/30/17.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView restaurantImage;
        public TextView restaurantRating, restaurantName;
        public ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            restaurantImage = (ImageView) itemView.findViewById(R.id.restaurant_image);
            restaurantRating = (TextView) itemView.findViewById(R.id.restaurant_rating);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getPosition(), false);
        }
    }

    public ArrayList<Restaurant> mRestaurants;
    public Context mContext;
    public RestaurantListAdapter(ArrayList<Restaurant> mRestaurants, Context mContext){
        this.mRestaurants = mRestaurants;
        this.mContext = mContext;
    }

    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the custon layout
        View restaurantView = inflater.inflate(R.layout.fragment_list, parent, false);

        //return a holder instance
        ViewHolder viewHolder = new ViewHolder(restaurantView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantListAdapter.ViewHolder holder, int position) {
        final Restaurant restaurant = mRestaurants.get(position);

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantRating.setText(restaurant.getRating());
        ImageLoader(restaurant.getPhotoReference(), holder.restaurantImage);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isClickLong) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_DATA, restaurant.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void ImageLoader(String reference, ImageView restaurantImage){
        Picasso.with(mContext)
                .load(reference)
                .fit()
                .centerCrop()
                .into(restaurantImage);
    }

}
