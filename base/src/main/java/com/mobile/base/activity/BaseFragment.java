package com.mobile.base.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.base.R;
import com.mobile.base.util.PermissionHelper;
import com.mobile.base.widget.dialog.LoadingDialog;

import static android.R.id.message;

public abstract class  BaseFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private ActionEventInterface mEventInterface;
    protected View titleBarLayout;
    private FrameLayout containerLayout;
    protected PermissionHelper mPermissionHelper;
    private LoadingDialog mProgressDialog;
    private boolean isHidden;

    /**
     * 用于保存startFragmentForResult方法后requestCode的数据
     */
    public int requestCode = -1;
    /**
     * 用于保存startFragmentForResult方法后resultCode的数据
     */
    public int resultCode = -1;
    /**
     * 用于保存startFragmentForResult方法后data的数据
     */
    public Intent intentData;
    /**
     * 是否是需要算处理onActivityForResult
     */
    public boolean isHandleForResult;

    public boolean isPlug;

    public String plugPackageName;
    protected TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BaseActivity.historyFragmentStack.put(this.getClass().getSimpleName(), this);
        //此baseFramgent的根布局，其子类的布局将添加到此布局的containerLayout当中
        View rootView;
        if (isPlug){
            ((BaseActivity)getActivity()).isPlug = true;
            int layoutId = getResources().getIdentifier("base_library_base_layout","layout",plugPackageName);
            rootView = inflater.inflate(layoutId, container, false);
            titleBarLayout = rootView.findViewById(getResources().getIdentifier("base_lib_title_bar_layout","id",plugPackageName));
            containerLayout = (FrameLayout) rootView.findViewById(getResources().getIdentifier("base_lib_container_layout","id",plugPackageName));
        }else{
            ((BaseActivity)getActivity()).isPlug = false;
            rootView = inflater.inflate(R.layout.base_library_base_layout, container, false);
            titleBarLayout = rootView.findViewById(R.id.base_lib_title_bar_layout);
            containerLayout = (FrameLayout) rootView.findViewById(R.id.base_lib_container_layout);
        }
        rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                if (getActivity() != null){
                    ((BaseActivity)getActivity()).isPlug = isPlug;
                }
                return true;
            }
        });
        title = (TextView) rootView.findViewById(R.id.base_lib_title_bar_title_text);
        setFitsSystemStatusBar();
        // 左边按钮默认为后退
        setTitleBarLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPermissionHelper = new PermissionHelper(getActivity());
        //子类的布局，将添加到根布局中当
        View contentView = createView(inflater, container, savedInstanceState);
        if (contentView != null) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            containerLayout.addView(contentView, lp);
        }
        if (getActivity() instanceof BaseActivity) {
            setEventInterface((BaseActivity) getActivity());
        }
        return rootView;
    }

    public void setTitleBarBackground(int color) {
        titleBarLayout.setBackgroundColor(color);
    }

    /**
     * 如果需要设置沉浸式状态栏，可以重写这个方法在里面进行设置
     */
    protected void setFitsSystemStatusBar() {
//        StatusBarUtil.setColor(getActivity(), Color.BLACK, 0);
    }

    protected void showTitleBar(boolean isShow) {
        if (isShow) {
            titleBarLayout.setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (!isHidden) {
            onPauseFragment();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isPlug) {
//            ((BaseActivity) getActivity()).isPlug = true;
//        }else{
//            ((BaseActivity) getActivity()).isPlug = false;
//        }
        if (!isHidden) {
            onResumeFragment();
            Log.d(TAG, "======>" + BaseActivity.historyFragmentStack.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isHidden) {
            onStopFragment();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isHidden) {
            onStartFragment();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyFragment();
        BaseActivity.historyFragmentStack.remove(this.getClass().getSimpleName());
        Log.d(TAG, "======>" + BaseActivity.historyFragmentStack.toString());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            onPauseFragment();
            onStopFragment();
        } else {
            onStartFragment();
            onResumeFragment();
        }
        Log.d("MyFragment", getClass().getSimpleName() + "---------onHinddenChanged");
    }

    protected void setMidelImgShow(boolean flag) {
        ImageView showImg = (ImageView) titleBarLayout.findViewById(R.id.base_lib_title_bar_middle_img);
        showImg.setVisibility(flag ? View.VISIBLE : View.GONE);
        ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_title_text)).setVisibility(flag ? View.INVISIBLE : View.VISIBLE);
    }

    protected void setTitle(String text) {
        ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_title_text)).setText(text);
    }

    protected void setTitle(int textId) {
        if (isPlug){
            ((TextView) titleBarLayout.findViewById(getResources().getIdentifier("base_lib_title_bar_title_text","id",plugPackageName))).setText(textId);
        }else{
            ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_title_text)).setText(textId);
        }
    }

    protected void showTitleBarUnderline(boolean showUnderline) {
        titleBarLayout.findViewById(R.id.base_lib_title_bar_underline).setVisibility(showUnderline ? View.VISIBLE : View.GONE);
    }

    protected void setTitleBarLeftBtnImage(int imgId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText("");
        Drawable drawable = ContextCompat.getDrawable(getActivity(), imgId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnImage(Bitmap bitmap) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText("");
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void showTitleBarLeftBtn(boolean show) {
        if (show) {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setVisibility(View.GONE);
        }
    }

    protected void showTitleBarRightBtn(boolean show){
        if (show) {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_right_panel).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_right_panel).setVisibility(View.GONE);
        }
    }

    protected void setTitleBarLeftBtnText(int textId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText(textId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    protected void setTitleBarLeftBtnText(String text) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText(text);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    protected void setTitleBarLeftBtn(int textId, int imgId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText(textId);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), imgId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnListener(View.OnClickListener listener) {
        if (isPlug){
            titleBarLayout.findViewById(getResources().getIdentifier("base_lib_title_bar_left_text","id",plugPackageName)).setOnClickListener(listener);
        }else{
            titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setOnClickListener(listener);
        }

    }

    protected void setTitleBarRightBtnListener(View.OnClickListener listener) {
        titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text).setOnClickListener(listener);
    }

    protected void setTitleBarRightBtnImage(int imgId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText("");
        Drawable drawable = ContextCompat.getDrawable(getActivity(), imgId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        rightTV.setVisibility(View.VISIBLE);
    }

    protected void setTitleBarRightBtnImage(Bitmap bitmap) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText("");
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        rightTV.setVisibility(View.VISIBLE);
    }

    protected void setTitleBarRightBtnText(int textId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText(textId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rightTV.setVisibility(View.VISIBLE);
    }

    protected void setTitleBarRightBtn(int textId, int imgId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText(textId);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), imgId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        rightTV.setVisibility(View.VISIBLE);
    }

    protected void setTitleBarRightBtnText(String text) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText(text);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rightTV.setVisibility(View.VISIBLE);
    }

    protected void showRedDot(boolean isShow) {
        if (isShow) {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_right_red_dot).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_right_red_dot).setVisibility(View.GONE);
        }
    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void onStartFragment();

    public abstract void onResumeFragment();

    public abstract void onPauseFragment();

    public abstract void onStopFragment();

    public abstract void onDestroyFragment();


    public void setEventInterface(ActionEventInterface eventInterface) {
        mEventInterface = eventInterface;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void finish() {
        mEventInterface.onEvent(ActionMode.FINISH, this, null, null, -1, -2, null);
    }


    public void startFragment(Class<? extends BaseFragment> clazz, Bundle args) {
        mEventInterface.onEvent(ActionMode.START_ACTIVITY, this, clazz, args, -1, -2, null);
    }

    public void startFragmentBySingleTask(Class<? extends BaseFragment> clazz, Bundle args) {
        mEventInterface.onEvent(ActionMode.START_ACTIVITY_FRO_SINGLE_TASK, this, clazz, args, -1, -2, null);
    }

    public void startFragmentAndFinish(Class<? extends BaseFragment> clazz, Bundle args) {
        mEventInterface.onEvent(ActionMode.START_ACTIVITY_AND_FINISH, this, clazz, args, -1, -2, null);
    }

    public void startFragmentForResult(Class<? extends BaseFragment> clazz, Bundle args, int requestCode) {
        mEventInterface.onEvent(ActionMode.START_ACTIVITY_FOR_RESULT, this, clazz, args, requestCode, -2, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    protected void setResult(int resultCode, Intent data) {
        mEventInterface.onEvent(ActionMode.SET_RESULT, this, null, null, -1, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput() {
        if (getContext() != null) {
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void showProgress() {
        if (getActivity().isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {

            mProgressDialog = new LoadingDialog(getActivity(), getString(R.string.base_library_waiting));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(message);
        mProgressDialog.hideMsg();
        mProgressDialog.show();
    }

    protected void showProgress(String message) {
        if (getActivity().isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {

            mProgressDialog = new LoadingDialog(getActivity(), message);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(message);
        mProgressDialog.show();
    }

    protected void showProgress(@StringRes int resId) {
        if (getActivity().isFinishing()) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(getActivity(), getString(resId));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(getString(resId));
        mProgressDialog.show();
    }

    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
