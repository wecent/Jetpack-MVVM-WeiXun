package com.wecent.weixun.network;

import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.NewsResponse;
import com.wecent.weixun.model.response.ResultResponse;
import com.wecent.weixun.model.response.VideoPathResponse;

import io.reactivex.Observable;

/**
 * desc:
 * author: wecent .
 * date: 2017/9/2 .
 */
public class WeiXunApi {

    public static WeiXunApi mInstance;

    private WeiXunApiService mService;

    public WeiXunApi(WeiXunApiService weiXunApiService) {
        this.mService = weiXunApiService;
    }

    public static WeiXunApi getInstance(WeiXunApiService weiXunApiService) {
        if (mInstance == null)
            mInstance = new WeiXunApi(weiXunApiService);
        return mInstance;
    }

    /**
     * 获取新闻列表
     *
     * @param category
     * @param lastTime
     * @param currentTime
     * @return
     */
    public Observable<NewsResponse> getNewsList(String category, long lastTime, long currentTime) {
        return mService.getNewsList(category, lastTime, currentTime);
    }

    /**
     * 获取新闻详情
     *
     * @param url
     * @return
     */
    public Observable<ResultResponse<NewsDetail>> getNewsDetail(String url){
        return mService.getNewsDetail(url);
    }

    /**
     * 获取评论列表
     *
     * @param groupId
     * @param itemId
     * @param offset
     * @param count
     * @return
     */
    public Observable<CommentResponse> getCommentList(String groupId, String itemId, String offset, String count){
        return mService.getCommentList(groupId, itemId, offset, count);
    }

    /**
     * 获取视频路径
     *
     * @param link
     * @param r
     * @param s
     * @return
     */
    public Observable<VideoPathResponse> getVideoDetail(String link, String r, String s){
        return mService.getVideoDetail(link, r, s);
    }

}
