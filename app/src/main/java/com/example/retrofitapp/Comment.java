package com.example.retrofitapp;

import com.google.gson.annotations.SerializedName;

public class Comment {

    int postId;

    int id;

    String title;

    @SerializedName("body")
    String text;

    String email;

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getEmail() {
        return email;
    }
}
