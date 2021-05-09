package com.example.easytalk.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("user_hobby")
    private List<String> user_hobby;
    @SerializedName("user_constellation")
    private String user_constellation;


    public User(String user_name) {
        this.user_name = user_name;
        this.user_hobby = null;
        this.user_constellation = null;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getUser_hobby() {
        return user_hobby;
    }

    public void setUser_hobby(List<String> user_hobby) {
        this.user_hobby = user_hobby;
    }

    public String getUser_constellation() {
        return user_constellation;
    }

    public void setUser_constellation(String user_constellation) {
        this.user_constellation = user_constellation;
    }
}
