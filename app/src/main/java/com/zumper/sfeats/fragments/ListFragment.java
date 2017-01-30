package com.zumper.sfeats.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zumper.sfeats.R;
import com.zumper.sfeats.adapters.RestaurantListAdapter;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;
import com.zumper.sfeats.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ListFragment extends Fragment {

    private GooglePlacesAPI googlePlacesAPI;
    private ArrayList<Restaurant> restaurantListSaved = null;
    private RestaurantListAdapter restaurantListAdapter;
    private RecyclerView recyclerViewRestaurants;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerViewRestaurants = (RecyclerView) rootView.findViewById(R.id.recycler_restaurant_list);
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateList();

        return rootView;
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
                    ArrayList<Restaurant> restaurants = getParseJson(response.body().string());
                    restaurantListAdapter = new RestaurantListAdapter(restaurants, getContext());
                    recyclerViewRestaurants.setAdapter(restaurantListAdapter);

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
