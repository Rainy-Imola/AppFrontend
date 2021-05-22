package com.example.easytalk.model;

import java.io.Serializable;

public class NewFriendMsg implements Serializable{
    private String from_author_name;
    private String to_author_name;
    private String reqMsg;
    private int Status;

    public String getFrom_author_name(){
        return from_author_name;
    }
    public void setFrom_author_name(String name){
        from_author_name=name;
    }

    public String getTo_author_name(){
        return to_author_name;
    }
    public void setTo_author_name(String name){
        to_author_name=name;
    }

    public String getReqMsg(){
        return reqMsg;
    }
    public void setReqMsg(String msg){
        reqMsg=msg;
    }

    public int getStatus(){return Status;}
    public void setStatus(int status){
        Status=status;
    }

    public NewFriendMsg(String from_name,String to_name,String msg,int s){
        setFrom_author_name(from_name);
        setTo_author_name(to_name);
        setReqMsg(msg);
        setStatus(s);
    }
}
