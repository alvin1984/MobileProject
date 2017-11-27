package com.mobile.base.widget.commonbackground;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * 支持XML配置的通用背景Button
 */
public class CommonBackgroundButton extends AppCompatButton {

    public CommonBackgroundButton(Context context) {
        this(context, null);
    }

    public CommonBackgroundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonBackgroundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CommonBackgroundFactory.fromXml(this, attrs);
    }
}
