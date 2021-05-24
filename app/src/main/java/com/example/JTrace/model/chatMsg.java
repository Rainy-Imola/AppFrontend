package com.example.JTrace.model;

public class chatMsg {
    private String senderID;
    private String receiveID;
    private boolean status;
    private int type;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiveID() {
        return receiveID;
    }

    public boolean isStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    private String content;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "chatMsg{" +
                "senderID='" + senderID + '\'' +
                ", receiveID='" + receiveID + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setReceiveID(String receiveID) {
        this.receiveID = receiveID;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public chatMsg(String senderID, String receiveID, int type, String content,String avatar) {
        this.senderID = senderID;
        this.receiveID = receiveID;
        this.type = type;
        this.status = false;
        this.content = content;
        this.avatar = avatar;
    }
}
