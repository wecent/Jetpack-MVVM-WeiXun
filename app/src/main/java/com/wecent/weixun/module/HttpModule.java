package com.wecent.weixun.module;

import com.wecent.weixun.WXApplication;
import com.wecent.weixun.network.ApiConstants;
import com.wecent.weixun.network.JanDanApiManager;
import com.wecent.weixun.network.JanDanApiService;
import com.wecent.weixun.network.RetrofitConfig;
import com.wecent.weixun.network.WeiXunApiManager;
import com.wecent.weixun.network.WeiXunApiService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * desc:
 * author: wecent
 * date: 2018/9/3
 */
@Module
public class HttpModule {

    @Provides
    OkHttpClient.Builder provideOkHttpClient() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(WXApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        return new OkHttpClient().newBuilder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

//    @Provides
//    Retrofit.Builder provideBuilder(OkHttpClient okHttpClient) {
//        return new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(okHttpClient);
//    }

    @Provides
    WeiXunApiManager provideWeiXunApis(OkHttpClient.Builder builder) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return WeiXunApiManager.getInstance(retrofitBuilder
                .baseUrl(ApiConstants.mWeiXunApi)
                .build().create(WeiXunApiService.class));
    }

    @Provides
    JanDanApiManager provideJanDanApis(OkHttpClient.Builder builder) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return JanDanApiManager.getInstance(retrofitBuilder
                .baseUrl(ApiConstants.mJanDanApi)
                .build().create(JanDanApiService.class));
    }

}
