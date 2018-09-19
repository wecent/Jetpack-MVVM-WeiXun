package com.wecent.weixun.ui.news.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.model.entity.NewsData;
import com.wecent.weixun.model.response.NewsResponse;
import com.wecent.weixun.network.BaseObserver;
import com.wecent.weixun.network.NewsApi;
import com.wecent.weixun.network.RxSchedulers;
import com.wecent.weixun.network.WeiXunApi;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.news.contract.WeiXunContract;
import com.wecent.weixun.utils.PreUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/8 .
 */
public class WeiXunPresenter extends BasePresenter<WeiXunContract.View> implements WeiXunContract.Presenter {

    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    private long lastTime;

    WeiXunApi mWeiXunApi;

    @Inject
    public WeiXunPresenter(WeiXunApi mWeiXunApi) {
        this.mWeiXunApi = mWeiXunApi;
    }

    @Override
    public void getData(final String channelCode, final String action) {
        lastTime = PreUtils.getLong(channelCode,0);//读取对应频道下最后一次刷新的时间戳
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
                        PreUtils.putLong(channelCode, lastTime);//保存刷新的时间戳

                        List<NewsData> data = response.data;
                        List<News> newsList = new ArrayList<>();
                        if (data != null && data.size() != 0){
                            for (NewsData newsData : data) {
                                News news = new Gson().fromJson(newsData.content, News.class);
                                newsList.add(news);
                            }
                        }
                        for (int i = 0; i < newsList.size(); i++) {
                            if (newsList.get(i).has_video) {
                                //如果有视频
                                if (newsList.get(i).video_style == 0) {
                                    //右侧视频
                                    if (newsList.get(i).middle_image == null || TextUtils.isEmpty(newsList.get(i).middle_image.url)) {
                                        newsList.get(i).itemType = News.TYPE_TEXT_NEWS;
                                    }
                                    newsList.get(i).itemType = News.TYPE_RIGHT_PIC_NEWS;
                                } else if (newsList.get(i).video_style == 2) {
                                    //居中视频
                                    newsList.get(i).itemType = News.TYPE_CENTER_PIC_NEWS;
                                }
                            } else {
                                //非视频新闻
                                if (!newsList.get(i).has_image) {
                                    //纯文字新闻
                                    newsList.get(i).itemType = News.TYPE_TEXT_NEWS;
                                } else {
                                    if (newsList.get(i).image_list == null || newsList.get(i).image_list.size() == 0) {
                                        //图片列表为空，则是右侧图片
                                        newsList.get(i).itemType = News.TYPE_RIGHT_PIC_NEWS;
                                    }

                                    if (newsList.get(i).gallary_image_count == 3) {
                                        //图片数为3，则为三图
                                        newsList.get(i).itemType = News.TYPE_THREE_PIC_NEWS;
                                    }
                                    //中间大图，右下角显示图数
                                    newsList.get(i).itemType = News.TYPE_CENTER_PIC_NEWS;
                                }
                            }
                        }
                        KLog.e(newsList);
                        if (!action.equals(NewsApi.ACTION_UP)) {
                            mView.loadData(newsList);
                        } else {
                            mView.loadMoreData(newsList);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        KLog.e(e.getLocalizedMessage());
                        if (!action.equals(NewsApi.ACTION_UP)) {
                            mView.loadData(null);
                        } else {
                            mView.loadMoreData(null);
                        }
                    }
                });
    }
}
