package com.wecent.weixun.component;

import android.content.Context;

import com.wecent.weixun.WXApplication;
import com.wecent.weixun.module.ApplicationModule;
import com.wecent.weixun.module.HttpModule;
import com.wecent.weixun.network.JanDanApi;
import com.wecent.weixun.network.NewsApi;
import com.wecent.weixun.network.WeiXunApi;

import dagger.Component;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/2 .
 */
@Component(modules = {ApplicationModule.class,HttpModule.class})
public interface ApplicationComponent {

    WXApplication getApplication();

    NewsApi getNetEaseApi();

    JanDanApi getJanDanApi();

    WeiXunApi getWeiXunApi();

    Context getContext();

}
