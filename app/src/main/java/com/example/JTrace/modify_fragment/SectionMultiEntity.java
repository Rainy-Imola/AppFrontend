package com.example.JTrace.modify_fragment;

import com.example.JTrace.model.Hobby;

public class SectionMultiEntity<T> {
private String header;
private Boolean isHeader;
private Hobby hobby;
    public SectionMultiEntity(boolean isHeader, String header) {
        this.header = header;
        this.isHeader = isHeader;
    }

    public SectionMultiEntity(Hobby video) {
        this.hobby = video;
    }
}
