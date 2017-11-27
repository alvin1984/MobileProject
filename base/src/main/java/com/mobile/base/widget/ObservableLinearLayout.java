package com.mobile.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 可以监听键盘弹出或者隐藏的线性布局
 */
public class ObservableLinearLayout extends LinearLayout {

    private LayoutChangeListener mLayoutChangeListener;

    public ObservableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (oldH != 0) {
            if (h > oldH) {
                if (mLayoutChangeListener != null) {
                    mLayoutChangeListener.onKeyboardHind();
                }
            } else {
                if (mLayoutChangeListener != null) {
                    mLayoutChangeListener.onKeyboardShow();
                }
            }
        }
    }

    public void setLayoutChangeListener(LayoutChangeListener mLayoutChangeListener) {
        this.mLayoutChangeListener = mLayoutChangeListener;
    }

    public interface LayoutChangeListener {
        void onKeyboardShow();

        void onKeyboardHind();
    }

}
