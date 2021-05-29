package com.example.JTrace.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// List<NewFriendMsg>
public class NewFriendMsgs implements Serializable {
    private List<NewFriendMsg> msgs = new ArrayList<>();

    public void setMsgs(List<NewFriendMsg> msgs) {
        this.msgs = msgs;
    }

    public List<NewFriendMsg> getMsgs() {
        return msgs;
    }

    public void NewFriendMsg(@Nullable List<NewFriendMsg> msgs) {
        setMsgs(msgs);
    }

    public void clearMsgs() {
        msgs.clear();
    }

    public void addMsg(NewFriendMsg msg) {
        msgs.add(msg);
    }

    public NewFriendMsg getMsgByIndex(int i) {
        if (i < 0 || i >= msgs.size()) {
            return null;
        } else {
            return msgs.get(i);
        }
    }
}
