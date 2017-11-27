package com.eascs.imageloader.base;

import android.graphics.drawable.Drawable;

public interface ImageLoaderListener {

    void onLoadingFailed(Exception e);

    void onLoadingComplete(Drawable resource);
}
