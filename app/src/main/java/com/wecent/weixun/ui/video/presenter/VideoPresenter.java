package com.wecent.weixun.ui.video.presenter;

import com.wecent.weixun.R;
import com.wecent.weixun.WXApplication;
import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.video.contract.VideoContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * desc: .
 * author: wecent .
 * date: 2018/9/10 .
 */
public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {

    @Inject
    VideoPresenter() {

    }

    @Override
    public void getChannel() {
        List<Channel> channelList;
        List<String> channelName = Arrays.asList(WXApplication.getContext().getResources()
                .getStringArray(R.array.video_channel));
        List<String> channelCode = Arrays.asList(WXApplication.getContext().getResources()
                .getStringArray(R.array.video_channel_code));
        List<Channel> channels = new ArrayList<>();

        for (int i = 0; i < channelName.size(); i++) {
            Channel channel = new Channel();
            channel.setChannelCode(channelCode.get(i));
            channel.setChannelName(channelName.get(i));
            channel.setChannelType(i < 1 ? 1 : 0);
            channel.setChannelSelect(i < channelCode.size() - 3);
            channels.add(channel);
        }
        channelList = new ArrayList<>();
        channelList.addAll(channels);
        mView.loadData(channelList);
    }
}
