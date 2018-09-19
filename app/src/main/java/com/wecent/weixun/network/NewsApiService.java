package com.wecent.weixun.network;

import com.wecent.weixun.model.NewsArticleBean;
import com.wecent.weixun.model.NewsCmppVideoBean;
import com.wecent.weixun.model.NewsDetail;
import com.wecent.weixun.model.NewsImagesBean;
import com.wecent.weixun.model.VideoChannelBean;
import com.wecent.weixun.model.VideoDetailBean;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.NewsResponse;
import com.wecent.weixun.model.response.ResultResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * desc:
 * author: wecent .
 * date: 2017/9/2 .
 */
public interface NewsApiService {

    String GET_ARTICLE_LIST = "api/news/feed/v62/?refer=1&count=20&loc_mode=4&device_id=34960436458&iid=13136511752";
    String GET_COMMENT_LIST = "article/v2/tab_comments/";

    /**
     * 获取新闻列表
     *
     * @param category 频道
     * @return
     */
    @GET(GET_ARTICLE_LIST)
    Observable<NewsResponse> getNewsList(@Query("category") String category,
                                         @Query("min_behot_time") long lastTime,
                                         @Query("last_refresh_sub_entrance_interval") long currentTime);

    /**
     * 获取新闻详情
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
    Observable<CommentResponse> getComment(@Query("group_id") String groupId,
                                           @Query("item_id") String itemId,
                                           @Query("offset") String offset,
                                           @Query("count") String count);

//    @GET("ClientNews")
//    Observable<List<NewsDetail>> getNewsDetail(@Query("id") String id,
//                                               @Query("action") String action,
//                                               @Query("pullNum") String pullNum,
//                                               @Query("uid") String uid,
//                                               @Query("devid") String devid,
//                                               @Query("proid") String proid,
//                                               @Query("vt") String vt,
//                                               @Query("publishid") String publishid,
//                                               @Query("screen") String screen,
//                                               @Query("os") String os,
//                                               @Query("df") String df,
//                                               @Query("nw") String nw);

    @GET("ClientNews")
    Observable<List<NewsDetail>> getNewsDetail(@Query("id") String id,
                                               @Query("action") String action,
                                               @Query("pullNum") int pullNum
    );

    @GET("api_vampire_article_detail")
    Observable<NewsArticleBean> getNewsArticleWithSub(@Query("aid") String aid);

    @GET
    Observable<NewsArticleBean> getNewsArticleWithCmpp(@Url String url,
                                                       @Query("aid") String aid);

    @GET
    Observable<NewsImagesBean> getNewsImagesWithCmpp(@Url String url,
                                                     @Query("aid") String aid);

    @GET("NewRelativeVideoList")
    Observable<NewsCmppVideoBean> getNewsVideoWithCmpp(@Url String url,
                                                       @Query("guid") String guid);

    @GET("ifengvideoList")
    Observable<List<VideoChannelBean>> getVideoChannel(@Query("page") int page);

    @GET("ifengvideoList")
    Observable<List<VideoDetailBean>> getVideoDetail(@Query("page") int page,
                                               @Query("listtype") String listtype,
                                               @Query("typeid") String typeid);


}
