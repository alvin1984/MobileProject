package com.mobile.mobileproject.ui.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by alvinzhang on 2017/8/7.
 */

public class TabGroup extends LinearLayout implements View.OnClickListener{

    private OnTabSelectListener onTabSelectListener;

    private View preTab;
    private int prePosition;

    public TabGroup(@NonNull Context context) {
        this(context,null);
    }

    public TabGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    public void addCustomerView(View item){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.weight = 1.0f;
        this.addView(item,lp);
    }

    public void addTab(Tab tab){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.weight = 1.0f;
        tab.setOnClickListener(this);
        this.addView(tab,lp);
        tab.setTag(this.getChildCount() - 1);
    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.onTabSelectListener = onTabSelectListener;
    }

    @Override
    public void onClick(View view) {
        if (onTabSelectListener != null){
            int position = (int) view.getTag();
            if (preTab != null){
                onTabSelectListener.onTabUnselected(preTab,prePosition);
            }
            onTabSelectListener.onTabSelected(view,position);
            preTab = view;
            prePosition = position;
        }
    }

    public void selectedTab(int position){
        View tab = getChildAt(position);
        tab.setTag(position);
        onClick(tab);
    }

    interface OnTabSelectListener{
        void onTabSelected(View tab, int position);
        void onTabUnselected(View tab, int position);
    }




}
