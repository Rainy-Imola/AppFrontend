package com.example.easytalk.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.easytalk.R;

public class TitleLayout extends LinearLayout {
    private ImageView iv_backward;
    private TextView tv_title, tv_forward;
    private addClickListener maddClickListener;

    public void setSaveClick(addClickListener mmaddClickListener ) {
        maddClickListener = mmaddClickListener;
    }

    public  interface addClickListener{

        void onItemClick(View view);

    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout bar_title = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.title_widget, this);
        iv_backward = (ImageView) bar_title.findViewById(R.id.iv_backward);
        tv_title = (TextView) bar_title.findViewById(R.id.tv_title);
        tv_forward = (TextView) bar_title.findViewById(R.id.tv_forward);

        //设置监听器
        //如果点击back则结束活动
        iv_backward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment).navigateUp();
            }
        });
        tv_forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maddClickListener!=null){
                    maddClickListener.onItemClick(v);
                }
            }
        });
    }
}