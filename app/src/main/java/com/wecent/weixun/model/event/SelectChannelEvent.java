package com.wecent.weixun.model.event;


/**
 * desc:
 * author: wecent
 * date: 2018/9/10
 */
public class SelectChannelEvent {

    public String channelName;

    public SelectChannelEvent(String channelName) {
        this.channelName = channelName;
    }
}
