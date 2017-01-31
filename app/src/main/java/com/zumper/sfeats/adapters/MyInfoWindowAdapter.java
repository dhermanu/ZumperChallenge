package com.zumper.sfeats.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.zumper.sfeats.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dhermanu on 1/30/17.
 */

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    public MyInfoWindowAdapter(Activity myActivity) {
        this.myContentsView = myActivity.getLayoutInflater().inflate(R.layout.marker_window, null);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView restaurantName = (TextView) myContentsView.findViewById(R.id.restaurant_name);
        TextView restaurantRating = (TextView) myContentsView.findViewById(R.id.restaurant_rating);

        String data = marker.getSnippet();
        List<String> items = Arrays.asList(data.split("\\s*,\\s*"));

        restaurantName.setText(items.get(0));
        if(!items.get(1).equals("NaN")){
            final String rating = "Rating: " + items.get(1);
            restaurantRating.setText(rating);
        }


        return myContentsView;
    }
}
