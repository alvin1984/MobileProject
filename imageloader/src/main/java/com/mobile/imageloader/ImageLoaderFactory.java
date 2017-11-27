package com.eascs.imageloader;

import com.eascs.imageloader.base.ImageLoader;
import com.eascs.imageloader.glide.GlideImageLoader;

public class ImageLoaderFactory {

    public static ImageLoader getImageLoader() {
        return new GlideImageLoader();
    }
}
