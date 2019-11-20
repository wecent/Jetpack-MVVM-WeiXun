package com.wecent.weixun.ui.video.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wecent.weixun.R;
import com.wecent.weixun.WXApplication;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.model.entity.NewsData;
import com.wecent.weixun.model.response.NewsResponse;
import com.wecent.weixun.network.BaseObserver;
import com.wecent.weixun.network.RxSchedulers;
import com.wecent.weixun.network.WeiXunApiManager;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.video.contract.VideoListContract;
import com.wecent.weixun.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * desc: .
 * author: wecent .
 * date: 2018/9/8 .
 */
public class VideoListPresenter extends BasePresenter<VideoListContract.View> implements VideoListContract.Presenter {

    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    private long lastTime;

    WeiXunApiManager mWeiXunApi;

    @Inject
    public VideoListPresenter(WeiXunApiManager mWeiXunApi) {
        this.mWeiXunApi = mWeiXunApi;
    }

    @Override
    public void getData(final String channelCode, final String action) {
        lastTime = SPUtils.getInstance().getLong(channelCode,0);//读取对应频道下最后一次刷新的时间戳
        if (lastTime == 0){
            //如果为空，则是从来没有刷新过，使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }

        mWeiXunApi.getNewsList(channelCode, lastTime, System.currentTimeMillis() / 1000)
                .compose(RxSchedulers.<NewsResponse>applySchedulers())
                .compose(mView.<NewsResponse>bindToLife())
                .subscribe(new BaseObserver<NewsResponse>() {
                    @Override
                    public void onSuccess(NewsResponse response) {
                        lastTime = System.currentTimeMillis() / 1000;
                        SPUtils.getInstance().put(channelCode, lastTime);//保存刷新的时间戳

                        List<NewsData> data = response.data;
                        List<News> newsList = new ArrayList<>();
                        if (data != null && data.size() != 0){
                            for (NewsData newsData : data) {
                                News news = new Gson().fromJson(newsData.content, News.class);
                                news.itemType = getNewsType(news);
                                newsList.add(news);
                            }
                        }
                        Logger.e(new Gson().toJson(newsList));
                        if (!action.equals(WeiXunApiManager.ACTION_UP)) {
                            mView.loadData(newsList);
                        } else {
                            String[] channelCodes = WXApplication.getContext().getResources().getStringArray(R.array.weixun_channel_code);
                            if (channelCode.equals(channelCodes[0])) {
                                //如果是推荐频道
                                newsList.remove(0);//移除第一个，因为第一个是置顶新闻，重复
                            }
                            mView.loadMoreData(newsList);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Logger.e(e.getLocalizedMessage());
                        if (!action.equals(WeiXunApiManager.ACTION_UP)) {
                            mView.loadData(null);
                        } else {
                            mView.loadMoreData(null);
                        }
                    }
                });
    }

    private int getNewsType(News news) {
        if (news.has_video) {
            //如果有视频
            if (news.video_style == 0) {
                //右侧视频
                if (news.middle_image == null || TextUtils.isEmpty(news.middle_image.url)) {
                    return News.TYPE_TEXT_NEWS;
                }
                return News.TYPE_RIGHT_PIC_NEWS;
            } else if (news.video_style == 2) {
                //居中视频
                return News.TYPE_CENTER_PIC_NEWS;
            }
        } else {
            //非视频新闻
            if (!news.has_image) {
                //纯文字新闻
                return News.TYPE_TEXT_NEWS;
            } else {
                if (news.image_list == null || news.image_list.size() == 0) {
                    //图片列表为空，则是右侧图片
                    return News.TYPE_RIGHT_PIC_NEWS;
                }

                if (news.gallary_image_count == 3) {
                    //图片数为3，则为三图
                    return News.TYPE_THREE_PIC_NEWS;
                }

                //中间大图，右下角显示图数
                return News.TYPE_CENTER_PIC_NEWS;
            }
        }
        return News.TYPE_TEXT_NEWS;
    }
}
