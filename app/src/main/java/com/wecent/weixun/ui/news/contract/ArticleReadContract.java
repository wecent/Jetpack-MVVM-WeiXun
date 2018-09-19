package com.wecent.weixun.ui.news.contract;

import com.wecent.weixun.model.NewsArticleBean;
import com.wecent.weixun.ui.base.BaseContract;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/7 .
 */
public interface ArticleReadContract {

    interface View extends BaseContract.BaseView{

        void loadData(NewsArticleBean articleBean);

    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        void getData(String aid);

    }

}
