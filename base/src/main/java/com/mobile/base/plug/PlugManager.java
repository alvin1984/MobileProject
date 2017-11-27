package com.mobile.base.plug;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * 插件管理者
 * Created by alvinzhang on 2017/7/20.
 */
public class PlugManager {

    public static String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "plugs" + File.separator + "app-debug.apk";

    private static PlugManager sInstance;

    private Context mContext;

    private String mNativeLibDir = null;

    private String dexOutputPath;

    private HashMap<String,PlugPackage> plugPackages;

    private PlugManager(Context context) {
        mContext = context.getApplicationContext();
        mNativeLibDir = mContext.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
        plugPackages = new HashMap<>();
    }


    public static PlugManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PlugManager.class) {
                if (sInstance == null) {
                    sInstance = new PlugManager(context);
                }
            }
        }
        return sInstance;
    }

    public void release(){
        if (plugPackages != null && !plugPackages.isEmpty()){
            plugPackages.clear();
        }
    }

    public synchronized PlugPackage loadPlug(String dexPath){
        if (plugPackages.containsKey(dexPath)){
            return plugPackages.get(dexPath);
        }
        PlugPackage plugPackage = new PlugPackage();
        DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
        AssetManager assetManager = createAssetManager(dexPath);
        Resources resources = createResources(assetManager);
        plugPackage.mClassLoader = dexClassLoader;
        plugPackage.mPlugAssetManager = assetManager;
        plugPackage.mPlugResources = resources;

        plugPackage.mTheme = resources.newTheme();
        plugPackage.mTheme.setTo(mContext.getTheme());

        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = info.applicationInfo;
        plugPackage.packageName = appInfo.packageName;
        plugPackages.put(dexPath,plugPackage);

        return plugPackage;
    }

    private DexClassLoader createDexClassLoader(String dexPath) {

        File dexOutputDir = mContext.getCacheDir();
        dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, mNativeLibDir,mContext.getClassLoader());
        return loader;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }


}
