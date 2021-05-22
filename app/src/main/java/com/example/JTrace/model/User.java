package com.example.JTrace.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("user_hobby")
    private List<String> user_hobby;
    @SerializedName("user_constellation")
    private String user_constellation;
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("user_avatar")
    private String user_avatar;

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public User(String user_name) {
        this.user_name = user_name;
        this.user_hobby = new ArrayList<>();
        this.user_constellation = "";
        this.user_avatar = "";
    }
    public User(){
        this.user_name = "";
        this.user_hobby = new ArrayList<>();
        this.user_constellation = "";
        this.user_id=0;
        this.user_avatar = "";
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
