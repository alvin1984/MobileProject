package com.eascs.imageloader.glide;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.eascs.imageloader.base.IBitmapTransformation;

public class GlideTransform extends BitmapTransformation {

    private IBitmapTransformation transformation;

    public GlideTransform(Context context, IBitmapTransformation transformation) {
        super(context);
        this.transformation = transformation;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        }
        Log.d(getClass().getCanonicalName(), "transform in child thread : " + String.valueOf(Looper.getMainLooper() != Looper.myLooper()));
        return transformation.transform(toTransform, null);
    }

    @Override
    public String getId() {
        return getClass().getName() + transformation.hashCode();
    }
}
