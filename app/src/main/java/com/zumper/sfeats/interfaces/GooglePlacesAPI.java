package com.zumper.sfeats.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dhermanu on 1/29/17.
 */

public interface GooglePlacesAPI {
    @GET("nearbysearch/json")
    Call<ResponseBody> getRestaurantList(@Query("location") String location,
                                         @Query("type") String type,
                                         @Query("rankBy") String rankBy,
                                         @Query("key") String key);

    @GET("details/json")
    Call<ResponseBody> getRestaurantDetail(@Query("placeid") String placeid,
                                          @Query("key") String key);
}
