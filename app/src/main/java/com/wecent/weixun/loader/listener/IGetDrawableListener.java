package com.wecent.weixun.loader.listener;

import android.graphics.drawable.Drawable;

/**
 * desc: 设置此接口是为了业务需要，一般不需要关心网络请求回来的drawable，但是业务需要切换的地方的话，需要拿到网络请求回来的drawable
 * author: wecent
 * date: 2018/9/27
 */
public interface IGetDrawableListener {
    void onDrawable(Drawable drawable);
}
