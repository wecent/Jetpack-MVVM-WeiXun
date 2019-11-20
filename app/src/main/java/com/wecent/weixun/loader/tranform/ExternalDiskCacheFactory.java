package com.wecent.weixun.loader.tranform;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.engine.cache.DiskCache;

/**
 * desc: 磁盘缓存
 *      Glide 使用 DiskLruCacheWrapper 作为默认的 磁盘缓存 。
 *      DiskLruCacheWrapper 是一个使用 LRU 算法的固定大小的磁盘缓存。默认磁盘大小为 250 MB ，
 *      位置是在应用的 缓存文件夹 中的一个 特定目录 。
 *      假如应用程序展示的媒体内容是公开的（从无授权机制的网站上加载，或搜索引擎等），
 *      那么应用可以将这个缓存位置改到外部存储：
 * author: wecent
 * date: 2018/9/27
 */
class ExternalDiskCacheFactory implements DiskCache.Factory {
    public ExternalDiskCacheFactory(Context context) {
    }

    @Nullable
    @Override
    public DiskCache build() {
        return null;
    }
}
