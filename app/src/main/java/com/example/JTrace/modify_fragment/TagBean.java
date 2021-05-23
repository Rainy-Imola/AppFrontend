package com.example.JTrace.modify_fragment;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class TagBean implements MultiItemEntity, Serializable {
    public String getTag_style() {
        return tag_style;
    }

    public void setTag_style(String tag_style) {
        this.tag_style = tag_style;
    }

    private  String tag_style;

    public TagBean(String style_tag) {
        this.tag_style = style_tag;
    }


    @Override
    public int getItemType() {
        return 1;
    }

}
