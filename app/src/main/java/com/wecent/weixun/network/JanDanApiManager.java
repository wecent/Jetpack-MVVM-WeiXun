package com.wecent.weixun.network;

import android.support.annotation.StringDef;

import com.wecent.weixun.model.entity.BelleEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Observable;

/**
 * desc:
 * author: wecent
 * date: 2018/9/27
 */
public class JanDanApiManager {

    public static final String TYPE_FRESH = "get_recent_posts";
    public static final String TYPE_FRESHARTICLE = "get_post";
    public static final String TYPE_BORED = "jandan.get_pic_comments";
    public static final String TYPE_GIRLS = "jandan.get_ooxx_comments";
    public static final String TYPE_Duan = "jandan.get_duan_comments";

    @StringDef({TYPE_FRESH, TYPE_BORED, TYPE_GIRLS, TYPE_Duan})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    public static JanDanApiManager sInstance;

    private JanDanApiService mService;

    public JanDanApiManager(JanDanApiService janDanApiService) {
        this.mService = janDanApiService;
    }

    public static JanDanApiManager getInstance(JanDanApiService janDanApiService) {
        if (sInstance == null)
            sInstance = new JanDanApiManager(janDanApiService);
        return sInstance;
    }

    /**
     * 获取 无聊图，妹子图，段子列表
     *
     * @param type {@link Type}
     * @param page 页码
     * @return
     */
    public Observable<BelleEntity> getJdDetails(@Type String type, int page) {
        return mService.getDetailData(ApiConstants.mJanDanApi, type, page);
    }

}
