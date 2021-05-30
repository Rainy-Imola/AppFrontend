package com.example.JTrace.modify_fragment;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class HobbyBean implements MultiItemEntity, Serializable {
    private String hobby;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public String getStyle_tag() {
        return hobby;
    }

    public void setStyle_tag(String style_tag) {
        this.hobby = style_tag;
    }

    public HobbyBean(String style_tag) {
        this.hobby = style_tag;
    }


    @Override
    public int getItemType() {
        return 2;
    }
}
