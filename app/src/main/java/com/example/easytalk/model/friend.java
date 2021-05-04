package com.example.easytalk.model;

import com.google.gson.annotations.SerializedName;

public class friend {
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private int image;

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

    public friend(String name,int image){
            this.name = name;
            this.image = image;
    }
}
