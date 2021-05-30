package com.example.JTrace.model;

import com.google.gson.annotations.SerializedName;
import com.jidcoo.android.widget.commentview.model.CommentEnable;
import com.jidcoo.android.widget.commentview.model.ReplyEnable;

import java.util.Date;
import java.util.List;

public class comment extends CommentEnable {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private String date;
    @SerializedName("msg_id")
    private String msg_id;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setMsg_id(String id) {
        this.msg_id = id;
    }

    public String getMsg_id() {
        return this.msg_id;
    }

    public comment(String author, String content, String msg_id, String date) {
        setAuthor(author);
        setContent(content);
        setMsg_id(msg_id);
        setDate(date);
    }

    @Override
    public <T extends ReplyEnable> List<T> getReplies() {
        return null;
    }
}
