package com.zumper.sfeats.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zumper.sfeats.BuildConfig;
import com.zumper.sfeats.R;
import com.zumper.sfeats.Utils;
import com.zumper.sfeats.activities.DetailActivity;
import com.zumper.sfeats.activities.MainActivity;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;
import com.zumper.sfeats.models.Restaurant;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dhermanu on 1/29/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback{
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

    public void updateList(){
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
        final String COORD_PARAM = "37.77,-122.41";
        final String TYPE_PARAM = "restaurant";
        final String DISTANCE_PARAM = "distance";


        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        googlePlacesAPI = retrofit.create(GooglePlacesAPI.class);
        Call<ResponseBody> restaurantList = googlePlacesAPI.getRestaurantList(
                COORD_PARAM,
                TYPE_PARAM,
                DISTANCE_PARAM,
                BuildConfig.PLACES_API_KEY);

        restaurantList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    restaurantListSaved = Utils.getParsedRestaurant(response.body().string());
                    Utils.updateMap(getActivity(), restaurantListSaved, googleMap);
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

}
