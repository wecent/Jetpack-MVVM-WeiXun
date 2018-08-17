package com.wecent.weixun;


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
 * author: Will .
 * date: 2017/9/2 .
 */
public class WXApplication extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;

    private static WXApplication mWXApplication;

    public static int width = 0;

    public static int height = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        mWXApplication = this;
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

}
