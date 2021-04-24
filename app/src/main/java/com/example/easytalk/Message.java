package com.example.easytalk;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {
    @SerializedName("usr_name")
    private String usr_name;
    @SerializedName("content")
    private String content;
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("updatedAt")
    private Date updatedAt;
    public Message(String content){
        setContent(content);
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setCreatedAt(Date createdat) {
        this.createdAt = createdat;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAtt(Date updatedat) {
        this.updatedAt = updatedat;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }

}

