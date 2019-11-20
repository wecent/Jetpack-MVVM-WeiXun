package com.wecent.weixun.ui.news.contract;

import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.ui.base.BaseContract;

import java.util.List;

/**
 * desc: .
 * author: wecent .
 * date: 2018/9/7 .
 */
public interface NewsListContract {

    interface View extends BaseContract.BaseView {

        /**
         * 加载新闻数据
         *
         * @param newsList
         */
        void loadData(List<News> newsList);

        /**
         * 加载更多新闻数据
         *
         * @param newsList
         */
        void loadMoreData(List<News> newsList);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取新闻详细信息
         *
         * @param channelCode
         * @param action
         */
        void getData(String channelCode, String action);
    }

}
