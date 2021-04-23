package com.example.easytalk.model;

import com.google.gson.annotations.SerializedName;

public class message {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
}
