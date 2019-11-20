package com.wecent.weixun.module;

import android.content.Context;

import com.wecent.weixun.WXApplication;

import dagger.Module;
import dagger.Provides;

/**
 * desc:
 * author: wecent
 * date: 2018/9/3
 */
@Module
public class ApplicationModule {

    private Context mContext;

    public ApplicationModule(Context context) {
        this.mContext = context;
    }

    @Provides
    WXApplication provideApplication() {
        return (WXApplication) mContext.getApplicationContext();
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
