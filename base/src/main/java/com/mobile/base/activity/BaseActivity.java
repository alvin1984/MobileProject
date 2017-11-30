package com.mobile.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.base.R;
import com.mobile.base.plug.PlugPackage;
import com.mobile.base.util.PermissionHelper;
import com.mobile.base.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.R.id.message;

/**
 * Created by alvinzhang on 2017/7/20.
 */
public class BaseActivity extends AppCompatActivity implements ActionEventInterface {

    /**
     * 标题栏容器
     */
    private View titleBarLayout;
    /**
     * 内容容器
     */
    private FrameLayout containerLayout;

    public LoadingDialog mProgressDialog;

    public PermissionHelper mPermissionHelper;

    /**
     * 此处用于处理那些需要在activity已经启动后(onResume方法调用后)再执行的操作
     */
    private Runnable delayRun;

    /**
     * 标记setContentView是否是fragment。
     * 由于使用者可能会设置一个普通的view，所以在处理返回时，如果在此activity启动过BaseFragment其他界面时，
     * 会出现一次按返回，结束一个fragment的同时也结束了当前的activity。所以增加这个标记位来处理这种情况。
     */
    private boolean isFirstViewNotFragment;

    /**
     * 用于保存用户已经启动的fragment任务栈
     */
    private LinkedList<String> fragmentTags = new LinkedList<>();

    /**
     * 用于保存历史打开过的所有fragment界面，全局的
     */
    public static ArrayMap<String, BaseFragment> historyFragmentStack = new ArrayMap<>();
    private final static String SAVE_KEY = "SAVE_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enterAnimation();
        super.onCreate(savedInstanceState);

        //设置此activity的根布局，子类的布局将添加到此根布局内
        super.setContentView(R.layout.base_library_base_layout);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setFitsSystemStatusBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mPermissionHelper = new PermissionHelper(this);
        initView();
        ActivityManager.getInstance().addActivity(this);
        if (savedInstanceState != null) {
            String targetFragmentTag = savedInstanceState.getString(SAVE_KEY);
            FragmentManager fm = getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            FragmentTransaction ft = fm.beginTransaction();
            if (!TextUtils.isEmpty(targetFragmentTag)) {
                for (Fragment fragment : fragments) {
                    if (fragment != null) {
                        if (targetFragmentTag.equals(fragment.getClass().getSimpleName())) {
                            ft.show(fragment);
                        } else {
                            ft.hide(fragment);
                        }
                    }
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    public PermissionHelper getPermissionHelper() {
        return mPermissionHelper;
    }

    /**
     * 如果需要设置沉浸式状态栏，可以重写这个方法在里面进行设置
     */
    protected void setFitsSystemStatusBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (delayRun != null) {
            delayRun.run();
            delayRun = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (getSupportFragmentManager().getFragments() != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }
            }
            fragmentTransaction.commitAllowingStateLoss();

        }
        fragmentTags.clear();

        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();

    }

    public void setContentView(int resId) {
        isFirstViewNotFragment = true;
        View v = getLayoutInflater().inflate(resId, null, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        containerLayout.addView(v, lp);
    }

    public void setContentView(View view) {
        isFirstViewNotFragment = true;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        containerLayout.addView(view, lp);
    }

    public void setContentView(BaseFragment fragment) {
        //如果是插件，则设置相应的信息
        initPlugConfig(fragment.getClass(), fragment);

//        fragment.isPlug = isPlug;
//        fragment.plugPackageName = plugPackageName;
        fragment.setEventInterface(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.base_lib_container_layout, fragment, fragment.getClass().getSimpleName());
        ft.commit();

        fragmentTags.add(fragment.getClass().getSimpleName());

    }

    private void initPlugConfig(Class clazz, BaseFragment fragment) {
        String[] clazzPackageNameCells = clazz.getPackage().getName().split("\\.");
        String clazzPackageName = clazzPackageNameCells[0] + "." + clazzPackageNameCells[1] + "." + clazzPackageNameCells[2];

        if (!this.getClass().getPackage().getName().contains(clazzPackageName)
                && !this.getClass().getPackage().getName().contains("com.mobile.base")
                && !this.getClass().getPackage().getName().contains("com.mobile.mobileproject")) {
            fragment.isPlug = true;
            fragment.plugPackageName = clazz.getPackage().getName();
        }
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.base_lib_title_bar_layout);
        containerLayout = (FrameLayout) findViewById(R.id.base_lib_container_layout);
        titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showTitleBar(boolean isShow) {
        if (isShow) {
            titleBarLayout.setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.setVisibility(View.GONE);
        }
    }

    protected void showTitleBarUnderline(boolean showUnderline) {
        titleBarLayout.findViewById(R.id.base_lib_title_bar_underline).setVisibility(showUnderline ? View.VISIBLE : View.GONE);
    }

    public void showTitleBarLeftBtn(boolean show) {
        if (show) {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setVisibility(View.GONE);
        }
    }

    protected void setTitle(String text) {
        ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_title_text)).setText(text);
    }

    public void setTitle(int textId) {
        ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_title_text)).setText(textId);
    }

    protected void setTitleBarLeftBtnImage(int imgId) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText("");
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnImage(Bitmap bitmap) {
        TextView leftTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        leftTV.setText("");
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
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
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        leftTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarLeftBtnListener(View.OnClickListener listener) {
        titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text).setOnClickListener(listener);
    }

    protected void setTitleBarRightBtnListener(View.OnClickListener listener) {
        titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text).setOnClickListener(listener);
    }

    protected void setTitleBarRightBtnImage(int imgId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText("");
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarRightBtnImage(Bitmap bitmap) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText("");
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarRightBtnText(int textId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText(textId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    protected void setTitleBarRightBtn(int textId, int imgId) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_left_text));
        rightTV.setText(textId);
        Drawable drawable = ContextCompat.getDrawable(this, imgId);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    protected void setTitleBarRightBtnText(String text) {
        TextView rightTV = ((TextView) titleBarLayout.findViewById(R.id.base_lib_title_bar_right_text));
        rightTV.setText(text);
        rightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }


    protected void showRedDot(boolean isShow) {
        if (isShow) {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_right_red_dot).setVisibility(View.VISIBLE);
        } else {
            titleBarLayout.findViewById(R.id.base_lib_title_bar_right_red_dot).setVisibility(View.GONE);
        }
    }

    public View getTitleBar() {
        return titleBarLayout;
    }

    public void startFragment(Class<? extends BaseFragment> clazz, Bundle args) {
        startFragment(clazz, args, false);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!fragmentTags.isEmpty()) {
            ArrayList<String> saveData = new ArrayList<>();
            for (String temp : fragmentTags) {
                saveData.add(temp);
            }
            outState.putStringArrayList("FragmentTags", saveData);
            outState.putString(SAVE_KEY, fragmentTags.getLast());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> data = savedInstanceState.getStringArrayList("FragmentTags");
        if (data != null && !data.isEmpty()) {
            for (String temp : data) {
                fragmentTags.add(temp);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        exitAnimation();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        enterAnimation();
    }

    public void startFragment(Class<? extends BaseFragment> clazz, Bundle args, boolean putStack) {
        try {

            final BaseFragment fragment = clazz.newInstance();

            //如果是插件，则设置相应的信息
            initPlugConfig(clazz, fragment);
//            if (!clazz.getPackage().getName().equals(this.getClass().getPackage().getName())){
//                fragment.isPlug = true;
//                fragment.plugPackageName = clazz.getPackage().getName();
//            }

            fragment.setEventInterface(this);
            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (!fragmentTags.isEmpty()) {
                String preFragmentTag = fragmentTags.getLast();
                final BaseFragment preFragment = (BaseFragment) fragmentManager.findFragmentByTag(preFragmentTag);
                if (preFragment != null) {
                    ft.hide(preFragment);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (preFragment != null) {
                            if (preFragment.isPlug) {
                                int anim = getResources().getIdentifier("base_library_slide_left_out", "anim", preFragment.plugPackageName);
                                Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, anim);
                                preFragment.getView().startAnimation(leftOutAnim);
                            } else {
                                Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_left_out);
                                preFragment.getView().startAnimation(leftOutAnim);
                            }
                        }
                    }
                });
            }
            ft.add(R.id.base_lib_container_layout, fragment, clazz.getSimpleName());
            fragmentTags.add(clazz.getSimpleName());
            if (putStack) {
                ft.addToBackStack(null);//将当前的fragment压入栈中，以便将返回或者用户回退界面时能返回上一个页面
            }
            ft.commit();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (fragment != null && fragment.getView() != null) {
                        if (fragment.isPlug) {
                            int anim = getResources().getIdentifier("base_library_slide_right_in", "anim", fragment.plugPackageName);
                            Animation rightInAnim = AnimationUtils.loadAnimation(BaseActivity.this, anim);
                            fragment.getView().startAnimation(rightInAnim);
                        } else {
                            Animation rightInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_in);
                            fragment.getView().startAnimation(rightInAnim);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MyHandler handler = new MyHandler();

    private static class MyHandler extends Handler {

    }

    protected void startFragmentForResult(BaseFragment startForResultFragment, Class<? extends BaseFragment> clazz, Bundle args, int requestCode, boolean putStack) {
        startForResultFragment.isHandleForResult = true;
        startForResultFragment.requestCode = requestCode;
        startFragment(clazz, args, putStack);
    }

    private void hindFragments() {

    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }


    public boolean isPlug;
    public String plugPackageName;
    public PlugPackage mPlugPackage;

    @Override
    public void onEvent(ActionMode mode, final BaseFragment currentFragment, final Class<? extends BaseFragment> clazz, final Bundle args, int requestCode, int resultCode, Intent data) {
        switch (mode) {
            case START_ACTIVITY_FOR_RESULT:
                startFragmentForResult(currentFragment, clazz, args, requestCode, true);
                break;
            case FINISH:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                int count = fragmentManager.getBackStackEntryCount();
                if (count > 0) {
                    fragmentTags.pollLast();
                    if (!fragmentTags.isEmpty()) {
                        String preFragmentTag = fragmentTags.getLast();
                        final BaseFragment preFragment = (BaseFragment) fragmentManager.findFragmentByTag(preFragmentTag);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (preFragment != null && preFragment.getView() != null){
                                    Animation leftInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_left_in);
                                    preFragment.getView().startAnimation(leftInAnim);
                                }

                                if (currentFragment != null && currentFragment.getView() != null){
                                    Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
                                    currentFragment.getView().startAnimation(rightOutAnim);
                                }

//                                if (preFragment != null && preFragment.getView() != null){
//                                    if (preFragment.isPlug){
//                                        int anim = getResources().getIdentifier("base_library_slide_left_in","anim",preFragment.plugPackageName);
//                                        Animation leftInAnim = AnimationUtils.loadAnimation(BaseActivity.this,anim);
//                                        preFragment.getView().startAnimation(leftInAnim);
//                                    }else{
//                                        Animation leftInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_left_in);
//                                        preFragment.getView().startAnimation(leftInAnim);
//                                    }
//                                }
//
//                                if (currentFragment != null && currentFragment.getView() != null){
//                                    if (currentFragment.isPlug){
//                                        int anim = getResources().getIdentifier("base_library_slide_right_out","anim",currentFragment.plugPackageName);
//                                        Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, anim);
//                                        currentFragment.getView().startAnimation(rightOutAnim);
//                                    }else{
//                                        Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
//                                        currentFragment.getView().startAnimation(rightOutAnim);
//                                    }
//                                }
                            }
                        });

                        isPlug = preFragment.isPlug;
                        plugPackageName = preFragment.plugPackageName;
                        fragmentTransaction.show(preFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        if (preFragment.isHandleForResult) {
                            preFragment.onActivityResult(preFragment.requestCode, currentFragment.resultCode, currentFragment.intentData);
                        }
                        fragmentManager.popBackStack();//返回上一fragment


                    } else {
                        if (!isFirstViewNotFragment) {
                            finish();
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (currentFragment != null && currentFragment.getView() != null) {
                                        Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
                                        currentFragment.getView().startAnimation(rightOutAnim);
                                    }

                                }
                            });
                            fragmentManager.popBackStack();
                        }
                    }


                } else {
                    if (!isFirstViewNotFragment) {
                        finish();
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (currentFragment != null && currentFragment.getView() != null) {
                                    Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
                                    currentFragment.getView().startAnimation(rightOutAnim);
                                }

                            }
                        });
                        fragmentManager.popBackStack();
                        fragmentManager.beginTransaction().remove(currentFragment).commitAllowingStateLoss();
                        fragmentTags.pollLast();
                    }
                }
                break;
            case START_ACTIVITY:
                startFragment(clazz, args, true);
                break;
            case SET_RESULT:
                currentFragment.resultCode = resultCode;
                currentFragment.intentData = data;
                break;
            case START_ACTIVITY_AND_FINISH:
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentFragment != null && currentFragment.getView() != null) {
                            Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_left_out);
                            currentFragment.getView().startAnimation(leftOutAnim);
                        }

                    }
                });
                ft.remove(currentFragment);
                ft.commitAllowingStateLoss();
                fragmentTags.pollLast();
                fm.popBackStack();
                startFragment(clazz, args, true);
                break;
            case START_ACTIVITY_FRO_SINGLE_TASK:
                int index = fragmentTags.indexOf(clazz.getSimpleName());
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                if (index != -1) {//此处先判断当前的activity里是否已经加载过此fragment
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (currentFragment != null && currentFragment.getView() != null) {
                                Animation leftOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_left_out);
                                currentFragment.getView().startAnimation(leftOutAnim);
                            }
                        }
                    });
                    int fragmentCount = fm2.getBackStackEntryCount();
                    for (int i = fragmentCount; i > index; i--) {
                        fm2.popBackStack();
                        fragmentTags.pollLast();
                    }
                    final String topFragmentTag = fragmentTags.getLast();
                    final BaseFragment topFragment = (BaseFragment) fm2.findFragmentByTag(topFragmentTag);
                    ft2.show(topFragment);
                    ft2.commitAllowingStateLoss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (topFragment != null && topFragment.getView() != null) {
                                Animation rightInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_in);
                                topFragment.getView().startAnimation(rightInAnim);
                            }
                        }
                    });
                } else {
                    if (historyFragmentStack.containsKey(clazz.getSimpleName())) {//判断是否历史上其他activity已经加载过此fragment
                        final BaseFragment baseFragment = historyFragmentStack.get(clazz.getSimpleName());
                        BaseActivity baseActivity = (BaseActivity) baseFragment.getActivity();
                        baseActivity.delayRun = new Runnable() {
                            @Override
                            public void run() {
                                baseFragment.startFragmentBySingleTask(clazz, args);
                            }
                        };
                        startActivity(new Intent(this, baseActivity.getClass()));
                        if (currentFragment != null && currentFragment.getActivity() != null && !currentFragment.getActivity().isFinishing()) {
                            BaseActivity currentActivity = (BaseActivity) currentFragment.getActivity();
                            currentActivity.finish();
                        }

                    } else {
                        startFragment(clazz, args, true);
                    }

                }


                break;
        }
    }

    public void startFragmentBySingleTask(Class<? extends BaseFragment> clazz, Bundle args) {
        onEvent(ActionMode.START_ACTIVITY_FRO_SINGLE_TASK, null, clazz, args, -1, -2, null);
    }

    protected void removeAllFragment() {
        fragmentTags.clear();
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * 进入动画
     */
    protected void enterAnimation() {
        overridePendingTransition(R.anim.base_library_slide_right_in, R.anim.base_library_slide_left_out);
    }

    /**
     * 退出动画
     */
    protected void exitAnimation() {
        overridePendingTransition(R.anim.base_library_slide_left_in, R.anim.base_library_slide_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {

                String curFragmentTag = fragmentTags.getLast();
                final BaseFragment curFragment = (BaseFragment) fragmentManager.findFragmentByTag(curFragmentTag);
                boolean consume = curFragment.onKeyDown(keyCode, event);
                if (!consume) {/**如果fragment自己消化了返回按钮事件，则不做处理*/
                    fragmentTags.pollLast();

                    if (!fragmentTags.isEmpty()) {
                        String preFragmentTag = fragmentTags.getLast();
                        final BaseFragment preFragment = (BaseFragment) fragmentManager.findFragmentByTag(preFragmentTag);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (preFragment != null && preFragment.getView() != null) {
                                    Animation leftInAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_left_in);
                                    preFragment.getView().startAnimation(leftInAnim);
                                }

                                if (curFragment != null && curFragment.getView() != null) {
                                    Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
                                    curFragment.getView().startAnimation(rightOutAnim);
                                }

                            }
                        });
                        isPlug = preFragment.isPlug;
                        plugPackageName = preFragment.plugPackageName;

                        ft.show(preFragment);
                        ft.commitAllowingStateLoss();
                        fragmentManager.popBackStack();
                        return true;
                    } else {
                        if (!isFirstViewNotFragment) {
                            return super.onKeyDown(keyCode, event);
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (curFragment != null && curFragment.getView() != null) {
                                        Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
                                        curFragment.getView().startAnimation(rightOutAnim);
                                    }

                                }
                            });
                            fragmentManager.popBackStack();
                            fragmentManager.beginTransaction().remove(curFragment).commitAllowingStateLoss();
                            fragmentTags.pollLast();
                            return true;
                        }

                    }

                } else {
                    return true;
                }

            } else {
                //此处解决当fragment通过setContentView加载进来时，由于没有放入到fragmentManager的栈中，需要特殊处理
                if (!fragmentTags.isEmpty()) {
                    String curFragmentTag = fragmentTags.getLast();
                    final BaseFragment curFragment = (BaseFragment) fragmentManager.findFragmentByTag(curFragmentTag);
                    boolean consume = curFragment.onKeyDown(keyCode, event);
                    if (consume) {
                        return true;
                    }else if (isFirstViewNotFragment){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (curFragment != null && curFragment.getView() != null) {
                                    Animation rightOutAnim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.base_library_slide_right_out);
                                    curFragment.getView().startAnimation(rightOutAnim);
                                }

                            }
                        });
                        fragmentManager.popBackStack();
                        fragmentManager.beginTransaction().remove(curFragment).commitAllowingStateLoss();
                        fragmentTags.pollLast();
                        return true;
                    }
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * 强制开关软键盘
     */
    public void toggleSoftInputFoced() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void showProgress() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {

            mProgressDialog = new LoadingDialog(this, getString(R.string.base_library_waiting));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(message);
        mProgressDialog.hideMsg();
        mProgressDialog.show();
    }

    protected void showProgress(String message) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {

            mProgressDialog = new LoadingDialog(this, message);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.setMsg(message);
        mProgressDialog.show();
    }

    protected void showProgress(@StringRes int resId) {
        if (isFinishing()) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(this, getString(resId));
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

    @Override
    public Resources getResources() {
        return mPlugPackage != null && isPlug ? mPlugPackage.mPlugResources : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mPlugPackage != null && isPlug ? mPlugPackage.mPlugAssetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPlugPackage != null && isPlug ? mPlugPackage.mClassLoader : super.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        return mPlugPackage != null && isPlug ? mPlugPackage.mTheme : super.getTheme();
    }
}
