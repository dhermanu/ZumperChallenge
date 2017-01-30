package com.zumper.sfeats.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.ui.IconGenerator;
import com.zumper.sfeats.R;
import com.zumper.sfeats.activities.DetailActivity;
import com.zumper.sfeats.activities.MainActivity;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;
import com.zumper.sfeats.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dhermanu on 1/29/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener{
    private GoogleMap googleMap;
    private GooglePlacesAPI googlePlacesAPI;
    private ArrayList<Restaurant> restaurantListSaved = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        updateList();


        return rootView;

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng SanFrancisco = new LatLng(37.77, -122.41);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SanFrancisco, 15));

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent =  new Intent(getContext(), DetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_DATA, marker.getTitle());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            this.myContentsView = getActivity().getLayoutInflater().inflate(R.layout.marker_window, null);

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
            if(items.get(1) != "NaN")
               restaurantRating.setText(items.get(1));


            return myContentsView;
        }
    }



    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position, Restaurant restaurant)
    {
        String data = dataBuilder(restaurant);
        MarkerOptions markerOptions = new
                MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
                .position(position)
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
                .title("1")
                .snippet(data);

        googleMap.addMarker(markerOptions);
    }

    public String dataBuilder(Restaurant restaurant){
        StringBuilder sb = new StringBuilder();
        String commaSeparate = ",";

        sb.append(restaurant.getName());
        sb.append(commaSeparate);
        sb.append(restaurant.getRating());
        sb.append(commaSeparate);
        return new String(sb);
    }








    public void updateList(){
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        googlePlacesAPI = retrofit.create(GooglePlacesAPI.class);
        Call<ResponseBody> restaurantList = googlePlacesAPI.getRestaurantList(
                "37.77,-122.41",
                "restaurant",
                "distance",
                "AIzaSyB-");
        Log.v("TEST",restaurantList.request().url().toString());


        restaurantList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    restaurantListSaved = getParseJson(response.body().string());

                    IconGenerator iconFactory = new IconGenerator(getActivity());
                    iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    int i = 1;
                    for (Restaurant restaurant : restaurantListSaved) {
                        addIcon(iconFactory,
                                Integer.toString(i),
                                new LatLng(restaurant.getLatitude(), restaurant.getLongitude()),
                                restaurant);
                        builder.include(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()));
                        i++;
                    }

                    final LatLngBounds bounds = builder.build();
                    final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);

                    googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            googleMap.animateCamera(cu);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public ArrayList<Restaurant> getParseJson(String jsonString) throws JSONException{
        JSONObject responseObject  = new JSONObject(jsonString);
        JSONArray restaurantList = responseObject.getJSONArray("results");

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for(int i = 0; i <restaurantList.length();i++){
            JSONObject getRestaurant = restaurantList.getJSONObject(i);

            Restaurant restaurant = new Restaurant(getRestaurant);
            restaurants.add(restaurant);
        }
        return restaurants;
    }
}
