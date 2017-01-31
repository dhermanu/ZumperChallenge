package com.zumper.sfeats;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;
import com.zumper.sfeats.adapters.MyInfoWindowAdapter;
import com.zumper.sfeats.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dhermanu on 1/30/17.
 */

public class Utils {

    public static ArrayList<Restaurant> getParsedRestaurant(String jsonString) throws JSONException {
        final String RESULT = "results";
        JSONObject responseObject  = new JSONObject(jsonString);
        JSONArray restaurantListJSON = responseObject.getJSONArray(RESULT);

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for(int i = 0; i <restaurantListJSON.length();i++){
            JSONObject getRestaurant = restaurantListJSON.getJSONObject(i);

            Restaurant restaurant = new Restaurant(getRestaurant);
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    public static void updateMap(Activity myActivity, ArrayList<Restaurant> restaurants, GoogleMap googleMap){
        IconGenerator iconFactory = new IconGenerator(myActivity);
        iconFactory.setStyle(IconGenerator.STYLE_RED);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int i = 1;
        for (Restaurant restaurant : restaurants) {
            addIcon(iconFactory,
                    Integer.toString(i),
                    new LatLng(restaurant.getLatitude(), restaurant.getLongitude()),
                    restaurant, googleMap);
            builder.include(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()));
            i++;
        }

        final LatLngBounds bounds = builder.build();
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);


        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(myActivity));
        googleMap.moveCamera(cu);
    }

    private static void addIcon(IconGenerator iconFactory, CharSequence text,
                         LatLng position, Restaurant restaurant, GoogleMap googleMap)
    {
        String data = dataBuilder(restaurant);
        MarkerOptions markerOptions = new
                MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
                .position(position)
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
                .title(restaurant.getId())
                .snippet(data);

        googleMap.addMarker(markerOptions);
    }

    public static String dataBuilder(Restaurant restaurant){
        StringBuilder sb = new StringBuilder();
        String commaSeparate = ",";

        sb.append(restaurant.getName());
        sb.append(commaSeparate);
        sb.append(restaurant.getRating());
        sb.append(commaSeparate);
        return new String(sb);
    }

    public static void ImageLoader(String reference, ImageView restaurantImage, Context mContext)
            throws MalformedURLException {

        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?";
        final String WIDTH_PARAM = "maxwidth";
        final String MAX_WIDTH = "400";
        final String REFERENCE_PARAM = "photoreference";
        final String KEY_PARAM = "key";
        final String API_KEY = BuildConfig.PLACES_API_KEY;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(WIDTH_PARAM, MAX_WIDTH)
                .appendQueryParameter(REFERENCE_PARAM, reference)
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();
        URL url = new URL(builtUri.toString());


        Picasso.with(mContext)
                .load(url.toString())
                .fit()
                .centerCrop()
                .into(restaurantImage);
    }


}
