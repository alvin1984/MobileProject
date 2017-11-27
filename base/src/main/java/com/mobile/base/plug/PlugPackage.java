package com.mobile.base.plug;

import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * 插件实体
 */
public class PlugPackage {

    public DexClassLoader mClassLoader;

    public Resources mPlugResources;

    public AssetManager mPlugAssetManager;

    public Resources.Theme mTheme;

    public String packageName;
}
