package com.zumper.sfeats.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dhermanu on 1/29/17.
 */

public class Restaurant {
    private String id;
    private String name;
    private double rating;
    private String photoReference;
    private double latitude;
    private double longitude;

    public Restaurant(){

    }

    public Restaurant(JSONObject restaurant) throws JSONException {
        final String JSON_ID = "place_id";
        final String JSON_NAME = "name";
        final String JSON_RATING = "rating";
        final String JSON_PHOTO = "photos";
        final String JSON_PHOTO_REFERENCE = "photo_reference";
        final String JSON_GEOMETRY = "geometry";
        final String JSON_LOCATION = "location";
        final String JSON_LATITUDE = "lat";
        final String JSON_LONGITUDE = "lng";

        JSONArray photos = restaurant
                .optJSONArray(JSON_PHOTO);

        JSONObject location = restaurant
                .getJSONObject(JSON_GEOMETRY)
                .getJSONObject(JSON_LOCATION);

        this.id = restaurant.getString(JSON_ID);
        this.name = restaurant.getString(JSON_NAME);
        this.latitude = location.getDouble(JSON_LATITUDE);
        this.longitude = location.getDouble(JSON_LONGITUDE);
        this.rating = restaurant.optDouble(JSON_RATING);

        if(photos != null )
            this.photoReference = photos
                    .getJSONObject(0)
                    .getString(JSON_PHOTO_REFERENCE);


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



}
