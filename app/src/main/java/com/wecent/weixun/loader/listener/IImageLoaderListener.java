package com.wecent.weixun.loader.listener;

import android.widget.ImageView;

/**
 * desc: 监听图片下载进度
 * author: wecent
 * date: 2018/9/27
 */
public interface IImageLoaderListener {

    //监听图片下载错误
    void onLoadingFailed(String url, ImageView target, Exception exception);
   //监听图片加载成功
    void onLoadingComplete(String url, ImageView target);

}
