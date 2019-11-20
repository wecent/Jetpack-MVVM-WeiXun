package com.wecent.weixun.network;

import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.NewsResponse;
import com.wecent.weixun.model.response.ResultResponse;
import com.wecent.weixun.model.response.VideoPathResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * desc:
 * author: wecent
 * date: 2018/9/2
 */
public interface WeiXunApiService {

    String GET_ARTICLE_LIST = "api/news/feed/v62/?refer=1&count=20&loc_mode=4&device_id=34960436458&iid=13136511752";
    String GET_COMMENT_LIST = "article/v2/tab_comments/";

    /**
     * 获取新闻列表数据
     *
     * @param category
     * @return
     */
    @GET(GET_ARTICLE_LIST)
    Observable<NewsResponse> getNewsList(@Query("category") String category,
                                         @Query("min_behot_time") long lastTime,
                                         @Query("last_refresh_sub_entrance_interval") long currentTime);

    /**
     * 获取新闻详情数据
     */
    @GET
    Observable<ResultResponse<NewsDetail>> getNewsDetail(@Url String url);

    /**
     * 获取评论列表数据
     *
     * @param groupId
     * @param itemId
     * @param offset
     * @param count
     * @return
     */
    @GET(GET_COMMENT_LIST)
    Observable<CommentResponse> getCommentList(@Query("group_id") String groupId,
                                               @Query("item_id") String itemId,
                                               @Query("offset") String offset,
                                               @Query("count") String count);

    /**
     * 获取视频数据
     *
     * @param link
     * @param r
     * @param s
     * @return
     */
    @Headers({"Content-Type:application/x-www-form-urlencoded; charset=UTF-8",
            "Cookie:PHPSESSIID=334267171504; _ga=GA1.2.646236375.1499951727; _gid=GA1.2.951962968.1507171739; Hm_lvt_e0a6a4397bcb500e807c5228d70253c8=1507174305;Hm_lpvt_e0a6a4397bcb500e807c5228d70253c8=1507174305; _gat=1",
            "Origin:http://toutiao.iiilab.com"})
    @POST("http://service.iiilab.com/video/toutiao")
    Observable<VideoPathResponse> getVideoDetail(@Query("link") String link,
                                                 @Query("r") String r,
                                                 @Query("s") String s);
}
