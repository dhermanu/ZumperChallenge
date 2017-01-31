package com.zumper.sfeats.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zumper.sfeats.R;
import com.zumper.sfeats.Utils;
import com.zumper.sfeats.activities.DetailActivity;
import com.zumper.sfeats.activities.MainActivity;
import com.zumper.sfeats.interfaces.ItemClickListener;
import com.zumper.sfeats.models.Restaurant;

import java.net.MalformedURLException;
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
            itemView.setOnClickListener(this);
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
        View restaurantView = inflater.inflate(R.layout.list_item_restaurant, parent, false);

        //return a holder instance
        ViewHolder viewHolder = new ViewHolder(restaurantView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantListAdapter.ViewHolder holder, int position) {
        final Restaurant restaurant = mRestaurants.get(position);
        final Double rating = restaurant.getRating();
        holder.restaurantName.setText(restaurant.getName());

        if(!rating.isNaN())
           holder.restaurantRating.setText(rating.toString());
        try {
            Utils.ImageLoader(restaurant.getPhotoReference(), holder.restaurantImage, mContext);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isClickLong) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_DATA, restaurant.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mRestaurants != null)
            return mRestaurants.size();
        return 0;
    }
}
