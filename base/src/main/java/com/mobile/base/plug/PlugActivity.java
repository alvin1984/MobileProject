package com.mobile.base.plug;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mobile.base.activity.BaseActivity;
import com.zhenai.proxy.IProxy;

/**
 * 加载插件的activity
 * Created by alvinzhang on 2017/7/20.
 */
public class PlugActivity extends BaseActivity {

    private PlugPackage mPlugPackage;
    private IProxy plugActivity;
    public static final String PLUG_NAME = "plugName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String componentName = getIntent().getStringExtra(PLUG_NAME);
        mPlugPackage = PlugManager.getInstance(this).loadPlug(PlugManager.dexPath);
        try {
            Class<?> clazz = getClassLoader().loadClass(componentName);
            plugActivity = (IProxy) clazz.newInstance();
            plugActivity.setHost(this);
            plugActivity.plugOnCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        Intent intent1 = intent;
        if (mPlugPackage != null){
            ComponentName component = intent.getComponent();
            intent1 = new Intent(this,PlugActivity.class);
            intent1.putExtra(PLUG_NAME,component.getClassName());
        }
        super.startActivity(intent1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (plugActivity != null){
            plugActivity.plugOnResume();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (plugActivity != null){

            plugActivity.plugOnDestroy();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (plugActivity != null){

            plugActivity.plugOnStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (plugActivity != null){

            plugActivity.plugOnPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (plugActivity != null){

            plugActivity.plugOnStop();
        }
    }

    @Override
    public Resources getResources() {
        return mPlugPackage != null ? mPlugPackage.mPlugResources : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mPlugPackage != null ? mPlugPackage.mPlugAssetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPlugPackage != null ? mPlugPackage.mClassLoader : super.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        return mPlugPackage != null ? mPlugPackage.mTheme : super.getTheme();
    }


}
