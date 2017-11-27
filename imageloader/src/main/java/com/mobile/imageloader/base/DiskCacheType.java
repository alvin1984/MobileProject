package com.eascs.imageloader.base;

/**
 * Set of available caching strategies for media.
 * Copy from  Glide DiskCacheStrategy
 */
public enum DiskCacheType {
    /**
     * Caches with both {@link #SOURCE} and {@link #RESULT}.
     */
    ALL(true, true),
    /**
     * Saves no data to cache.
     */
    NONE(false, false),
    /**
     * Saves just the original data to cache.
     */
    SOURCE(true, false),
    /**
     * Saves the media item after all transformations to cache.
     */
    RESULT(false, true);

    private final boolean cacheSource;
    private final boolean cacheResult;

    DiskCacheType(boolean cacheSource, boolean cacheResult) {
        this.cacheSource = cacheSource;
        this.cacheResult = cacheResult;
    }

    /**
     * Returns true if this request should cache the original unmodified data.
     */
    public boolean cacheSource() {
        return cacheSource;
    }

    /**
     * Returns true if this request should cache the final transformed result.
     */
    public boolean cacheResult() {
        return cacheResult;
    }
}