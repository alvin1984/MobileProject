package com.mobile.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mobile.base.R;

public class LoadingDialog extends Dialog{

    private TextView tvMsg;

    private Context mContext;
    private View iv_loading_frame;
    private AnimationDrawable loadingAnim;

    public LoadingDialog(Context context, String strMessage) {
        this(context, R.style.BaseLibraryLoadingDialog, strMessage);
    }

    public LoadingDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        mContext = context;
        this.setContentView(R.layout.base_library_dialog_loading_layout);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        tvMsg = (TextView) this.findViewById(R.id.base_lib_tv_dialog_tips);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        iv_loading_frame = this.findViewById(R.id.iv_loading_frame);
        loadingAnim = (AnimationDrawable) iv_loading_frame.getBackground();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
        tvMsg.setVisibility(View.VISIBLE);
        loadingAnim.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        loadingAnim.stop();
    }

    public void setMsg(String msg){
        tvMsg.setText(msg);
    }

    public void setMsg(int resId){
        tvMsg.setText(mContext.getString(resId));
    }

    public void hideMsg() {
        tvMsg.setVisibility(View.GONE);
    }
}
