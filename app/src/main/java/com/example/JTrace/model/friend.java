package com.example.JTrace.model;

import com.google.gson.annotations.SerializedName;

public class friend {
    @SerializedName("id")
    private int id = 0;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("status")
    private int status = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public friend(int id, String name, String image, int status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
    }
}
