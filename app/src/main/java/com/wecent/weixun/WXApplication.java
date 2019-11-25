package com.wecent.weixun;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerApplicationComponent;
import com.wecent.weixun.module.ApplicationModule;
import com.wecent.weixun.module.HttpModule;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.Locale;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;

/**
 * desc:
 * author: wecent
 * date: 2018/9/2
 */
public class WXApplication extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;

    private static WXApplication mWXApplication;

    private static Context mContext;//上下文

    @Override
    public void onCreate() {
        super.onCreate();
        mWXApplication = this;
        mContext = getApplicationContext();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        LitePal.initialize(this);

        bindAutoSize();
    }

    private void bindAutoSize() {
        AutoSizeConfig.getInstance()
                .setCustomFragment(true)
                //屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                });
    }

    public static WXApplication getInstance() {
        return mWXApplication;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }
}
