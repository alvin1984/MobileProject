package com.mobile.mobileproject;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;


/**
 * Created by alvinzhang on 2017/7/19.
 */

public class MobileApplication extends Application {

//    private DaoSession daoSession;

    private static MobileApplication instance;

    private static final String DB_NAME = "vteams.db";

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
        instance = this;
//        AppCan.getInstance().initSync(this.getApplicationContext());
//        AppCan.getInstance().setWidgetSdk(false);

        initTencenWebView();
    }

    private void initTencenWebView(){
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("zhanghx", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.d("zhanghx","=====>");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static MobileApplication getInstance(){
        return instance;
    }

    private void setupDatabase() {
//        //创建数据库shop.db"
//        VteamsDBHelper helper = new VteamsDBHelper(this, DB_NAME, null);
//        //获取可写数据库
//        SQLiteDatabase db = helper.getWritableDatabase();
//        //获取数据库对象
//        DaoMaster daoMaster = new DaoMaster(db);
//        //获取Dao对象管理者
//        daoSession = daoMaster.newSession();
    }

//    public DaoSession getDaoSession() {
//        return daoSession;
//    }


}
