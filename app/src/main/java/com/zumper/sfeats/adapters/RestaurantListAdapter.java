package com.zumper.sfeats.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.net.MalformedURLException;
import java.net.URL;
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

        Log.v("TEST_YES", restaurant.getName());
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantRating.setText(Double.toString(restaurant.getRating()));
        try {
            ImageLoader(restaurant.getPhotoReference(), holder.restaurantImage);
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

    public void ImageLoader(String reference, ImageView restaurantImage) throws MalformedURLException {

        //String test = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk";
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?";
        final String WIDTH_PARAM = "maxwidth";
        final String REFERENCE_PARAM = "photoreference";
        final String KEY_PARAM = "key";
        final String api_key = "AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(WIDTH_PARAM, "400")
                .appendQueryParameter(REFERENCE_PARAM, reference)
                .appendQueryParameter(KEY_PARAM, api_key)
                .build();
        URL url = new URL(builtUri.toString());


        Picasso.with(mContext)
                .load(url.toString())
                .fit()
                .centerCrop()
                .into(restaurantImage);
    }

}
