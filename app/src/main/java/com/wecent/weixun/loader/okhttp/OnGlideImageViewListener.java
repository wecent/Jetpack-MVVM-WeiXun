package com.wecent.weixun.loader.okhttp;

import com.bumptech.glide.load.engine.GlideException;

/**
 * desc: 注意：需要在监听返回键的时候，取消请求
 * author: wecent
 * date: 2018/9/27
 */
public interface OnGlideImageViewListener {
    /**
     *
     * @param percent 下载进度的百分比，不关心，大小
     * @param isDone 是否完成
     * @param exception 异常
     */
    void onProgress(int percent, boolean isDone, GlideException exception);
}
