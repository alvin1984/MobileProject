package com.mobile.imageloader.glide;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.mobile.imageloader.glide.transformation.BlurTransformation;
import com.mobile.imageloader.glide.transformation.RoundedCornersTransformation;

import java.util.ArrayList;

/**
 * Created by pxy on 2017/11/30.
 */

public class AppGlide {

    private GlideRequests mGlideRequests;
    private GlideRequest<?> mGlideRequest;

    private ArrayList<Transformation> mTransformations;

    private AppGlide(Context context) {
        mTransformations = new ArrayList<>();
        mGlideRequests = GlideApp.with(context);
    }

    public static AppGlide with(Context context) {
//        if (mGlideRequests == null){
//            synchronized (AppGlide.class){
//                if (mGlideRequests == null){
//                    mGlideRequests = GlideApp.with(context);
//                }
//            }
//        }
        return new AppGlide(context);
    }

    public AppGlide asBitmap() {
        mGlideRequest = mGlideRequests.asBitmap();
        return this;
    }

    public AppGlide asGif() {
        mGlideRequest = mGlideRequests.asGif();
        return this;
    }

    public AppGlide asDrawable() {
        mGlideRequest = mGlideRequests.asDrawable();
        return this;
    }

    public AppGlide asFile() {
        mGlideRequest = mGlideRequests.asFile();
        return this;
    }


    public AppGlide load(Object url) {
        if (mGlideRequest == null) {
            mGlideRequest = mGlideRequests.load(url);
        } else {
            mGlideRequest.load(url);
        }
        return this;
    }

    public AppGlide circleCrop() {
        mTransformations.add(new CircleCrop());
        return this;
    }

    public AppGlide blur(Context context) {
        mTransformations.add(new BlurTransformation(context));
        return this;
    }

    public AppGlide roundedCorners(RoundedCornersTransformation.CornerType cornerType, int radius) {
        mTransformations.add(new RoundedCornersTransformation(radius, 0, cornerType));
        return this;
    }

    public AppGlide roundedCorners(int radius) {
        mTransformations.add(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL));
        return this;
    }

    public AppGlide centerCrop(){
        mTransformations.add(new CenterCrop());
        return this;
    }

    public AppGlide placeholder(@DrawableRes int resId) {
        mGlideRequest.placeholder(resId);
        return this;
    }

    public com.bumptech.glide.request.target.Target into(ImageView target) {
        if (mTransformations.size() > 0){
            MultiTransformation multiTransformation = new MultiTransformation(mTransformations);
            mGlideRequest.transforms(multiTransformation);
        }
        return mGlideRequest.into(target);
    }

    public Target into(@NonNull SimpleTarget target) {
        if (mTransformations.size() > 0){
            MultiTransformation multiTransformation = new MultiTransformation(mTransformations);
            mGlideRequest.transforms(multiTransformation);
        }
        return mGlideRequest.into(target);
    }


}
