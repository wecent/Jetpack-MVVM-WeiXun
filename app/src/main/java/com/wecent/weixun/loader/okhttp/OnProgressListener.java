package com.wecent.weixun.loader.okhttp;

import com.bumptech.glide.load.engine.GlideException;

/**
 * desc: 注意：需要在监听返回键的时候，取消请求
 * author: wecent
 * date: 2018/9/27
 */
public interface OnProgressListener {
    /**
     *
      * @param imageUrl 图片地址
     * @param bytesRead 下载了多少字节
     * @param totalBytes 总共的大小
     * @param isDone 是否完成
     * @param exception 异常
     */
    void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception);
}
