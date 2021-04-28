package com.example.easytalk.model;

import com.google.gson.annotations.SerializedName;

public class message {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    public void setAuthor(String author1){author=author1;}
    public String getAuthor(){return author;}

    public void setContent(String content1){content=content1;}
    public String getContent(){return content;}

    public message(String author1,String content1){
        setAuthor(author1);
        setContent(content1);
    }
}
