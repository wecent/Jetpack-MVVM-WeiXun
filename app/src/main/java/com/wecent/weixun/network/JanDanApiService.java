package com.wecent.weixun.network;

import com.wecent.weixun.model.entity.BelleEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * desc:
 * author: wecent
 * date: 2018/9/27
 */
public interface JanDanApiService {

    @GET
    Observable<BelleEntity> getDetailData(@Url String url,
                                          @Query("oxwlxojflwblxbsapi") String oxwlxojflwblxbsapi,
                                          @Query("page") int page
    );

}

