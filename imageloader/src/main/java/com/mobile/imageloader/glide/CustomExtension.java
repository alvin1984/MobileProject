package com.mobile.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.imageloader.glide.transformation.BlurTransformation;
import com.mobile.imageloader.glide.transformation.RoundedCornersTransformation;

/**
 * Created by pxy on 2017/11/29.
 *
 */

@GlideExtension
public class CustomExtension {

    private CustomExtension(){}

    @GlideOption
    public static void blur(RequestOptions options, Context context){
        options.transform(new BlurTransformation(context,25));
    }

    @GlideOption
    public static void roundedCorners(RequestOptions options){
        options.transforms(new RoundedCornersTransformation(15,0));

    }

    @GlideOption
    public static void roundedCorners(RequestOptions options,
                                      RoundedCornersTransformation.CornerType cornerType,
                                      int radius,int margin){
        options.transforms(new RoundedCornersTransformation(radius,margin,cornerType));

    }

}
