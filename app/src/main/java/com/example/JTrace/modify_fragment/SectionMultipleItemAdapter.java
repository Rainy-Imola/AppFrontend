package com.example.JTrace.modify_fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.JTrace.R;
import com.example.JTrace.model.Hobby;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SectionMultipleItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {
    public static final int ITEM_FIRST_LEVEL = 1;

    public static final int ITEM_SECOND_LEVEL = 2;

    public SectionMultipleItemAdapter(List<MultiItemEntity> data){
        super(data);
        addItemType(ITEM_FIRST_LEVEL,R.layout.first_hobby_recycle);
        addItemType(ITEM_SECOND_LEVEL,R.layout.second_hobby_recyle);
    }



    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MultiItemEntity multiItemEntity) {
        switch (baseViewHolder.getItemViewType()){
            case ITEM_FIRST_LEVEL:
                TagBean tagBean = (TagBean) multiItemEntity;
                baseViewHolder.setText(R.id.textView8,tagBean.getTag_style());
                break;
            case ITEM_SECOND_LEVEL:
                HobbyBean hobbyBean = (HobbyBean) multiItemEntity;
                baseViewHolder.setText(R.id.textView8,hobbyBean.getStyle_tag());
                break;
        }

    }
}
