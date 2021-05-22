package com.example.JTrace.modify_fragment;



import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.JTrace.model.Hobby;

public class SectionMultipleItem extends SectionMultiEntity<Hobby> implements MultiItemEntity {

    public static final int TEXT = 1;
    public static final int IMG_TEXT = 2;
    private int itemType;        // 附加字段方便处理所需逻辑
    private boolean isMore;      // 附加字段方便处理所需逻辑
    private Hobby hobby;         // 主体数据实体类型

    // 创建section 数据
    public SectionMultipleItem(boolean isHeader, String header, boolean isMore) {
        super(isHeader, header);
        this.isMore = isMore;
    }

    // 创建主体item数据
    public SectionMultipleItem(int itemType, Hobby video) {
        super(video);
        this.hobby = video;
        this.itemType = itemType;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public Hobby getVideo() {
        return hobby;
    }

    public void setVideo(Hobby video) {
        this.hobby = video;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
