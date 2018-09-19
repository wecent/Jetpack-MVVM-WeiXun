package com.wecent.weixun.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * @author ChayChan
 * @description: TODO
 * @date 2017/7/6  15:11
 */

public class News implements MultiItemEntity {

    /**
     * 纯文字布局(文章、广告)
     */
    public static final int TYPE_TEXT_NEWS = 100;
    /**
     * 居中大图布局(1.单图文章；2.单图广告；3.视频，中间显示播放图标，右侧显示时长)
     */
    public static final int TYPE_CENTER_PIC_NEWS = 200;
    /**
     * 右侧小图布局(1.小图新闻；2.视频类型，右下角显示视频时长)
     */
    public static final int TYPE_RIGHT_PIC_NEWS = 300;
    /**
     * 三张图片布局(文章、广告)
     */
    public static final int TYPE_THREE_PIC_NEWS = 400;

    public int itemType;
    public int article_type;
    public String tag;
    public String title;
    public int hot;
    public String source;
    public int comment_count;
    public String article_url;
    public int gallary_image_count;
    public int video_style;
    public String item_id;
    public UserEntity user_info;
    public long behot_time;
    public String url;
    public boolean has_image;
    public boolean has_video;
    public int video_duration;
    public VideoEntity video_detail_info;
    public String group_id;
    public ImageEntity middle_image;
    public List<ImageEntity> image_list;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
