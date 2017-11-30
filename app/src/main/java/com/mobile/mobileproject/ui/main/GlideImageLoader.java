package com.mobile.mobileproject.ui.main;

import android.content.Context;
import android.widget.ImageView;

import com.mobile.imageloader.glide.GlideApp;
import com.mobile.mobileproject.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by alvinzhang on 2017/7/25.
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
//        Glide.with(context).load(path).into(imageView);
        GlideApp.with(context)
                .load(path)
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
