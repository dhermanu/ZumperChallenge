package com.zumper.sfeats.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhermanu on 1/30/17.
 */

public class RestaurantDetail {
    private String id;
    private String name;
    private String formattedAddress;
    private String formattedPhoneNumber;
    private String photoReference;
    private String rating;
    private ArrayList<Review> reviews;

    public RestaurantDetail(JSONObject detail) throws JSONException {
        final String JSON_NAME = "name";
        final String JSON_FORMATTED_ADDRESS = "formatted_address";
        final String JSON_FORMATTED_PHONE_NUMBER = "formatted_phone_number";
        final String JSON_PHOTO_REFERENCE = "photo_reference";
        final String JSON_PHOTO = "photos";
        final String JSON_RATING = "rating";
        final String JSON_REVIEWS = "reviews";

        JSONArray photos = detail.optJSONArray(JSON_PHOTO);
        JSONArray reviews = detail.optJSONArray(JSON_REVIEWS);
        this.name = detail.getString(JSON_NAME);
        this.formattedAddress = detail.getString(JSON_FORMATTED_ADDRESS);
        this.formattedPhoneNumber = detail.optString(JSON_FORMATTED_PHONE_NUMBER);
        this.rating = detail.optString(JSON_RATING);

        if(photos != null )
            this.photoReference = photos.getJSONObject(0).getString(JSON_PHOTO_REFERENCE);

        if(reviews != null)
            addReviews(detail.optJSONArray(JSON_REVIEWS));
    }

    private void addReviews(JSONArray reviewsJSON) throws JSONException {
        reviews = new ArrayList<Review>();
        for(int i = 0; i <reviewsJSON.length();i++){
            JSONObject getRestaurant = reviewsJSON.getJSONObject(i);
            Review review = new Review(getRestaurant);
            reviews.add(review);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formatted_address) {
        this.formattedAddress = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formatted_phone_number) {
        this.formattedPhoneNumber = formatted_phone_number;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }


}
