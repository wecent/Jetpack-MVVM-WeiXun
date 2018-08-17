package com.wecent.weixun.ui.news.contract;

import com.wecent.weixun.bean.Channel;
import com.wecent.weixun.ui.base.BaseContract;

import java.util.List;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/7 .
 */
public interface NewsContract{

    interface View extends BaseContract.BaseView{

        void loadData(List<Channel> channels, List<Channel> otherChannels);

    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        /**
         * 初始化频道
         */
        void getChannel();

    }

}
