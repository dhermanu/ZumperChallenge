package com.zumper.sfeats.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.zumper.sfeats.R;
import com.zumper.sfeats.adapters.ReviewListAdapter;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;
import com.zumper.sfeats.models.RestaurantDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
        updateList(placeID);
    }


    public void updateList(String placeID){
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        googlePlacesAPI = retrofit.create(GooglePlacesAPI.class);
        Call<ResponseBody> restaurantDetail = googlePlacesAPI.getRestaurantDetail(
                placeID,
                "AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk");
        Log.v("TEST2",  restaurantDetail.request().url().toString());


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
        ImageLoader(restaurantDetail.getPhotoReference());
        reviewListAdapter = new ReviewListAdapter(restaurantDetail.getReviews(), this);
        recyclerViewReviews.setAdapter(reviewListAdapter);
    }

    public void ImageLoader(String reference) throws MalformedURLException {

        //String test = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk";
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?";
        final String WIDTH_PARAM = "maxwidth";
        final String REFERENCE_PARAM = "photoreference";
        final String KEY_PARAM = "key";
        final String api_key = "AIzaSyB-";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(WIDTH_PARAM, "400")
                .appendQueryParameter(REFERENCE_PARAM, reference)
                .appendQueryParameter(KEY_PARAM, api_key)
                .build();
        URL url = new URL(builtUri.toString());

        Picasso.with(this)
                .load(url.toString())
                .fit()
                .centerCrop()
                .into(bannerImage);
    }
}
