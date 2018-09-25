package com.wecent.weixun;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerApplicationComponent;
import com.wecent.weixun.module.ApplicationModule;
import com.wecent.weixun.module.HttpModule;
import com.wecent.weixun.utils.ContextUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/2 .
 */
public class WXApplication extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;

    private static WXApplication mWXApplication;

    private static Context mContext;//上下文

    private static Thread mMainThread;//主线程

    private static long mMainThreadId;//主线程id

    private static Looper mMainLooper;//循环队列

    private static Handler mHandler;//主线程Handler

    public static int width = 0;

    public static int height = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mWXApplication = this;
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
        BGASwipeBackManager.getInstance().init(this);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        LitePal.initialize(this);
        width = ContextUtils.getSreenWidth(WXApplication.getContext());
        height = ContextUtils.getSreenHeight(WXApplication.getContext());
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

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    /**
     * 重启当前应用
     */
    public static void restartApp() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

}
