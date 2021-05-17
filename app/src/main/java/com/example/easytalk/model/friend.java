package com.example.easytalk.model;

import com.google.gson.annotations.SerializedName;

public class friend {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private int image;
    @SerializedName("status")
    private int status=0;

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

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public friend(int id, String name, int image, int status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
    }
}
