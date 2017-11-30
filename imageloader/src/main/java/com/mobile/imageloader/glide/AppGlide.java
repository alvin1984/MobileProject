package com.mobile.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.mobile.imageloader.glide.transformation.BlurTransformation;

import java.util.ArrayList;

/**
 * Created by pxy on 2017/11/30.
 */

public class AppGlide {

    private static GlideRequests mGlideRequests;
    private GlideRequest<Drawable> mGlideRequest;
    private ArrayList<Transformation> mTransformations;

    private AppGlide(){
        mTransformations = new ArrayList<>();
    }

    public static AppGlide with(Context context){
        mGlideRequests = GlideApp.with(context);
        return new AppGlide();
    }

    public AppGlide load(Object url){
        mGlideRequest = mGlideRequests.load(url);
        return this;
    }

    public AppGlide circleCrop(){
        mTransformations.add(new CircleCrop());
        return this;
    }

    public AppGlide blur(Context context){
        mTransformations.add(new BlurTransformation(context));
        return this;
    }

    public AppGlide placeholder(@DrawableRes int resId){
        mGlideRequest.placeholder(resId);
        return this;
    }

    public com.bumptech.glide.request.target.Target into(ImageView target){
        MultiTransformation multiTransformation = new MultiTransformation(mTransformations);
        mGlideRequest.transforms(multiTransformation);
        return mGlideRequest.into(target);
    }
}
