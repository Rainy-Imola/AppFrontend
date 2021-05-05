package com.example.easytalk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class message implements Serializable {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_w")
    private int imageW;
    @SerializedName("image_h")
    private int imageH;

    public void setAuthor(String author1){author=author1;}
    public String getAuthor(){return author;}

    public void setContent(String content1){content=content1;}
    public String getContent(){return content;}

    public void setCreatedAt(Date date){this.createdAt=date;}
    public Date getCreatedAt(){return this.createdAt;}

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

    public message(String author1,String content1,Date date,String imageUrl){
        setAuthor(author1);
        setContent(content1);
        setCreatedAt(date);
        setImageUrl(imageUrl);
    }
}
