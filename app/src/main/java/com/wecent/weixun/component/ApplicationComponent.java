package com.wecent.weixun.component;

import android.content.Context;

import com.wecent.weixun.WXApplication;
import com.wecent.weixun.module.ApplicationModule;
import com.wecent.weixun.module.HttpModule;
import com.wecent.weixun.network.JanDanApiManager;
import com.wecent.weixun.network.WeiXunApiManager;

import dagger.Component;

/**
 * desc:
 * author: wecent
 * date: 2018/9/2
 */
@Component(modules = {ApplicationModule.class, HttpModule.class})
public interface ApplicationComponent {

    WXApplication getApplication();

    Context getContext();

    WeiXunApiManager getWeiXunApi();

    JanDanApiManager getJanDanApi();

}
