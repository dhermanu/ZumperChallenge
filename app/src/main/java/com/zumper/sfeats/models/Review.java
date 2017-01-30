package com.zumper.sfeats.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dhermanu on 1/30/17.
 */

public class Review {
    private String authorName;
    private int rating;
    private String text;

    public Review(JSONObject detailJSON) throws JSONException {
        final String JSON_AUTHOR_NAME = "author_name";
        final String JSON_RATING = "rating";
        final String JSON_TEXT = "text";

        this.authorName = detailJSON.getString(JSON_AUTHOR_NAME);
        this.rating = detailJSON.getInt(JSON_RATING);
        this.text = detailJSON.getString(JSON_TEXT);
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



}
