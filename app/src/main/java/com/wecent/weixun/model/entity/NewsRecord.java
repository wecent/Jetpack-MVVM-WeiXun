package com.wecent.weixun.model.entity;

import org.litepal.crud.DataSupport;

/**
 * desc:
 * author: wecent
 * date: 2018/9/3
 */
public class NewsRecord extends DataSupport {

    private String channelCode;
    private int page;
    private String json;
    private long time;

    public NewsRecord(){

    }

    public NewsRecord(String channelCode, int page, String json, long time) {
        this.channelCode = channelCode;
        this.page = page;
        this.json = json;
        this.time = time;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
