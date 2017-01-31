package com.zumper.sfeats.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zumper.sfeats.BuildConfig;
import com.zumper.sfeats.R;
import com.zumper.sfeats.Utils;
import com.zumper.sfeats.adapters.ReviewListAdapter;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;
import com.zumper.sfeats.models.RestaurantDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private GooglePlacesAPI googlePlacesAPI;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView formattedAddress, formattedPhoneNumber, rating;
    private ImageView bannerImage;
    private ReviewListAdapter reviewListAdapter;
    private RecyclerView recyclerViewReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        formattedAddress = (TextView) findViewById(R.id.formatted_address);
        formattedPhoneNumber = (TextView) findViewById(R.id.formatted_phone_number);
        rating = (TextView) findViewById(R.id.rating);
        bannerImage = (ImageView) findViewById(R.id.profile_image);

        recyclerViewReviews = (RecyclerView) findViewById(R.id.recycler_review_list);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        final String placeID = getIntent().getStringExtra(MainActivity.EXTRA_DATA);
        getDetails(placeID);
    }


    public void getDetails(String placeID){

        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        googlePlacesAPI = retrofit.create(GooglePlacesAPI.class);
        Call<ResponseBody> restaurantDetail = googlePlacesAPI.getRestaurantDetail(
                placeID,
                BuildConfig.PLACES_API_KEY);

        restaurantDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    updateViews(response.body().string());

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

    public void updateViews(String jsonString) throws JSONException, MalformedURLException {
        JSONObject responseObject  = new JSONObject(jsonString);
        JSONObject result = responseObject.getJSONObject("result");
        RestaurantDetail restaurantDetail = new RestaurantDetail(result);

        collapsingToolbarLayout.setTitle(restaurantDetail.getName());
        formattedAddress.setText(restaurantDetail.getFormattedAddress());
        formattedPhoneNumber.setText(restaurantDetail.getFormattedPhoneNumber());
        rating.setText(restaurantDetail.getRating());
        Utils.ImageLoader(restaurantDetail.getPhotoReference(), bannerImage, this);
        reviewListAdapter = new ReviewListAdapter(restaurantDetail.getReviews(), this);
        recyclerViewReviews.setAdapter(reviewListAdapter);
    }
}
