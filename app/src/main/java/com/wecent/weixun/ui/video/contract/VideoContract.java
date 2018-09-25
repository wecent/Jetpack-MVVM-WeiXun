package com.wecent.weixun.ui.video.contract;

import com.wecent.weixun.model.Channel;
import com.wecent.weixun.model.VideoChannelBean;
import com.wecent.weixun.model.VideoDetailBean;
import com.wecent.weixun.ui.base.BaseContract;
import com.wecent.weixun.ui.news.contract.NewsContract;

import java.util.List;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/10 .
 */
public interface VideoContract {

    interface View extends BaseContract.BaseView{

        void loadData(List<Channel> channels);

    }

    interface Presenter extends BaseContract.BasePresenter<VideoContract.View>{
        /**
         * 初始化频道
         */
        void getChannel();

    }
}
