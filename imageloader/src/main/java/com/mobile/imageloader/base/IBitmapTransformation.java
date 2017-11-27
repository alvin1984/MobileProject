package com.eascs.imageloader.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


public interface IBitmapTransformation {
    public Bitmap transform(Bitmap source, ImageView imageView);

    public Context getContext();
}
