package com.example.JTrace.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class message implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_w")
    private int imageW;
    @SerializedName("image_h")
    private int imageH;


    @SerializedName("like")
    private int like;


    @SerializedName("accept")
    private int accept;
    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setAuthor(String author1) {
        author = author1;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content1) {
        content = content1;
    }

    public String getContent() {
        return content;
    }

    public void setCreatedAt(String date) {
        this.createdAt = date;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageW(int imageW) {
        this.imageW = imageW;
    }

    public int getImageW() {
        return imageW;
    }

    public void setImageH(int imageH) {
        this.imageH = imageH;
    }

    public int getImageH() {
        return imageH;
    }

    public message(String id, String author1, String content1, String date, String imageUrl) {
        setId(id);
        setAuthor(author1);
        setContent(content1);
        setCreatedAt(date);
        setImageUrl(imageUrl);
    }

    public message() {

    }
}
