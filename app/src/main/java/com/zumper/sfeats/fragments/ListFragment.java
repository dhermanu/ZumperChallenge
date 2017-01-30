package com.zumper.sfeats.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;
import com.zumper.sfeats.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dhermanu on 1/29/17.
 */

public class ListFragment extends Fragment {

    private GooglePlacesAPI googlePlacesAPI;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        return rootView;
    }

    public void updateList(){
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        googlePlacesAPI = retrofit.create(GooglePlacesAPI.class);
        Call<ResponseBody> restaurantList = googlePlacesAPI.getRestaurantList("37.77,-122.41", "restaurant", "distance", "AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk");

        restaurantList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
