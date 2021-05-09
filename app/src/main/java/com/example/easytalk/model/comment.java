package com.example.easytalk.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class comment {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private Date date;
    @SerializedName("msg_id")
    private String msg_id;

    public void setAuthor(String author){this.author=author;}
    public String getAuthor(){return this.author;}

    public void setContent(String content){this.content=content;}
    public String getContent(){return this.content;}

    public void setDate(Date date){this.date=date;}
    public Date getDate(){return this.date;}

    public void setMsg_id(String id){this.msg_id=id;}
    public String getMsg_id(){return this.msg_id;}

    public comment(String author,String content,String msg_id,Date date){
        setAuthor(author);
        setContent(content);
        setMsg_id(msg_id);
        setDate(date);
    }
}
