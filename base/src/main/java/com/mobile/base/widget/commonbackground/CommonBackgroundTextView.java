package com.mobile.base.widget.commonbackground;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 支持XML配置的通用背景TextView
 */
public class CommonBackgroundTextView extends AppCompatTextView {

    public CommonBackgroundTextView(Context context) {
        this(context, null);
    }

    public CommonBackgroundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonBackgroundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CommonBackgroundFactory.fromXml(this, attrs);
    }
}
