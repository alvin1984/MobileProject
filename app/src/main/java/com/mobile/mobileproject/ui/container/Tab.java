package com.mobile.mobileproject.ui.container;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.base.util.DensityUtils;
import com.mobile.mobileproject.R;


/**
 * Created by alvinzhang on 2017/8/7.
 */

public class Tab extends FrameLayout {

    private ImageView picTV;

    private TextView textTV;

    private TextView redTV;

    public Tab(@NonNull Context context) {
        this(context,null);
    }

    public Tab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.fragment_tabs_item,this,true);
        picTV = findViewById(R.id.iv_tab_pic);
        textTV = findViewById(R.id.tv_tab_text);
        redTV = findViewById(R.id.tv_red);
    }

    public Tab setTabImage(int res){
        picTV.setVisibility(VISIBLE);
        picTV.setImageResource(res);
        return this;
    }

    public Tab setTabText(String text){
        textTV.setText(text);
        return this;
    }

    public Tab setTabTextColor(String color){
        textTV.setTextColor(Color.parseColor(color));
        return this;
    }

    public Tab setTabTextColor(int color){
        textTV.setTextColor(color);
        return this;
    }

    public Tab setBackgroundResources(int res){
        super.setBackgroundResource(res);
        return this;
    }

    public Tab showRedPoint(boolean isShow){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtils.dp2px(getContext(),10),DensityUtils.dp2px(getContext(),10));
        lp.addRule(RelativeLayout.RIGHT_OF,R.id.iv_tab_pic);
        lp.leftMargin = DensityUtils.dp2px(getContext(),-5);
        redTV.setLayoutParams(lp);
        redTV.setText("");
        if (isShow){
            redTV.setVisibility(VISIBLE);
        }else{
            redTV.setVisibility(INVISIBLE);
        }
        return this;
    }


    public Tab showRedPointText(String number){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,DensityUtils.dp2px(getContext(),20));
        lp.addRule(RelativeLayout.RIGHT_OF,R.id.iv_tab_pic);
        lp.leftMargin = DensityUtils.dp2px(getContext(),-5);
        redTV.setLayoutParams(lp);
        redTV.setText(number);
        redTV.setVisibility(VISIBLE);
        return this;
    }


}
