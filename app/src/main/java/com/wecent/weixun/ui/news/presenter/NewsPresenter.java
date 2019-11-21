package com.wecent.weixun.ui.news.presenter;

import com.wecent.weixun.WXApplication;
import com.wecent.weixun.R;
import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.database.ChannelDao;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.news.contract.NewsContract;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * desc:
 * author: wecent
 * date: 2018/9/7
 */
public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {

    @Inject
    public NewsPresenter() {

    }

    @Override
    public void getChannel() {
        List<Channel> channelList;
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();
        channelList = ChannelDao.getChannels();
        if (channelList.size() < 1) {
            List<String> channelName = Arrays.asList(WXApplication.getContext().getResources()
                    .getStringArray(R.array.weixun_channel));
            List<String> channelCode = Arrays.asList(WXApplication.getContext().getResources()
                    .getStringArray(R.array.weixun_channel_code));
            List<Channel> channels = new ArrayList<>();

            for (int i = 0; i < channelName.size(); i++) {
                Channel channel = new Channel();
                channel.setChannelCode(channelCode.get(i));
                channel.setChannelName(channelName.get(i));
                channel.setChannelType(i < 1 ? 1 : 0);
                channel.setChannelSelect(i < channelCode.size() - 3);
                if (i < channelCode.size() - 3) {
                    myChannels.add(channel);
                } else {
                    otherChannels.add(channel);
                }
                channels.add(channel);
            }

            DataSupport.saveAllAsync(channels).listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                }
            });

            channelList = new ArrayList<>();
            channelList.addAll(channels);
        } else {
            channelList = ChannelDao.getChannels();
            Iterator<Channel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                if (!channel.isChannelSelect()) {
                    otherChannels.add(channel);
                    iterator.remove();
                }
            }
            myChannels.addAll(channelList);
        }
        mView.loadData(myChannels, otherChannels);
    }
}
