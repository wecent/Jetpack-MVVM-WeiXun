package com.wecent.weixun.ui.news.contract;

import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.ResultResponse;
import com.wecent.weixun.ui.base.BaseContract;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/19 .
 */
public interface WeiXunDetailContract {

    interface View extends BaseContract.BaseView {

        /**
         * 加载新闻数据
         *
         * @param news
         */
        void loadNewsData(ResultResponse<NewsDetail> news);

        /**
         * 加载评论数据
         *
         * @param comment
         */
        void loadConmentData(CommentResponse comment);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取新闻详细信息
         *
         * @param url
         */
        void getNewsData(String url);

        /**
         * 获取新闻评论信息
         *
         * @param groupId
         * @param itemId
         * @param pageNow
         */
        void getConmentData(String groupId, String itemId, int pageNow);
    }

}
