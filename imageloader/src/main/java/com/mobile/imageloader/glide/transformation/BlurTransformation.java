package com.mobile.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.FloatRange;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by pxy on 2017/11/30.
 */

public class BlurTransformation extends BitmapTransformation {

    private static final String ID = "org.ligboy.glide.BlurTransformation";

    public static final float DEFAULT_RADIUS = 25.0f;
    public static final float MAX_RADIUS = 25.0f;
    private static final float DEFAULT_SAMPLING = 1.0f;

    private Context mContext;
    private float mSampling = DEFAULT_SAMPLING;
    private float mRadius;
    private int mColor;

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes(CHARSET));
    }

    public static class Builder {

        private Context mContext;
        private float mRadius = DEFAULT_RADIUS;
        private int mColor = Color.TRANSPARENT;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public float getRadius() {
            return mRadius;
        }

        public Builder setRadius(float radius) {
            mRadius = radius;
            return this;
        }

        public int getColor() {
            return mColor;
        }

        public Builder setColor(int color) {
            mColor = color;
            return this;
        }

        public BlurTransformation build() {
            return new BlurTransformation(mContext, mRadius, mColor);
        }

    }

    /**
     *
     * @param context Context
     * @param radius The blur's radius.
     * @param color The color filter for blurring.
     */
    public BlurTransformation(Context context, @FloatRange(from = 0.0f) float radius, int color) {
        super(context);
        mContext = context;
        if (radius > MAX_RADIUS) {
            mSampling = radius / 25.0f;
            mRadius = MAX_RADIUS;
        } else {
            mRadius = radius;
        }
        mColor = color;
    }
    /**
     *
     * @param context Context
     * @param radius The blur's radius.
     */
    public BlurTransformation(Context context, @FloatRange(from = 0.0f) float radius) {
        this(context, radius, Color.TRANSPARENT);
    }

    public BlurTransformation(Context context) {
        this(context, DEFAULT_RADIUS);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        boolean needScaled = mSampling == DEFAULT_SAMPLING;
        int originWidth = toTransform.getWidth();
        int originHeight = toTransform.getHeight();
        int width, height;
        if (needScaled) {
            width = originWidth;
            height = originHeight;
        } else {
            width = (int) (originWidth / mSampling);
            height = (int) (originHeight / mSampling);
        }
        //find a re-use bitmap
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        if (mSampling != DEFAULT_SAMPLING) {
            canvas.scale(1 / mSampling, 1 / mSampling);
        }
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        PorterDuffColorFilter filter =
                new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.drawBitmap(toTransform, 0, 0, paint);
// TIPS: Glide will take care of returning our original Bitmap to the BitmapPool for us,
// we needn't to recycle it.
//        toTransform.recycle();  <--- Just for tips. by Ligboy

        RenderScript rs = RenderScript.create(mContext);
        Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        blur.setInput(input);
        blur.setRadius(mRadius);
        blur.forEach(output);
        output.copyTo(bitmap);

        rs.destroy();

        if (needScaled) {
            return bitmap;
        } else {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, originWidth, originHeight, true);
            bitmap.recycle();
            return scaled;
        }
    }

    /**
     * A method to get a unique identifier for this particular transformation that can be used as part of a cache key.
     * The fully qualified class name for this class is appropriate if written out, but getClass().getName() is not
     * because the name may be changed by proguard.
     * <p/>
     * <p>
     * If this transformation does not affect the data that will be stored in cache, returning an empty string here
     * is acceptable.
     * </p>
     *
     * @return A string that uniquely identifies this transformation.
     */
    public String getId() {
        StringBuilder sb = new StringBuilder(ID);
        sb
                .append('-').append(mRadius)
                .append('-').append(mColor);
        return sb.toString();
    }
}
