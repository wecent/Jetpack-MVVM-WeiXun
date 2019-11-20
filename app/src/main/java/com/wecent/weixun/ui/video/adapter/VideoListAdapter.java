package com.wecent.weixun.ui.video.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.wecent.weixun.R;
import com.wecent.weixun.loader.ImageLoader;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.utils.AppUtils;
import com.wecent.weixun.utils.TimeUtils;
import com.wecent.weixun.widget.VideoPathDecoder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.OnVideoClickListener;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * desc: .视频列表的Adapter .
 * author: wecent .
 * date: 2018/9/10 .
 */

public class VideoListAdapter extends BaseQuickAdapter<News,BaseViewHolder> {

    private Context mContext;

    public VideoListAdapter(@Nullable List<News> data, Context context) {
        super(R.layout.item_video_list, data);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final News news) {
        if (TextUtils.isEmpty(news.title)){
            //如果没有标题，则直接跳过
            return;
        }

        helper.setVisible(R.id.ll_title,true);//显示标题栏
        helper.setText(R.id.tv_title, news.title);//设置标题

        String format = "%s次播放";
        int watchCount = news.video_detail_info.video_watch_count;
        String countUnit = "";
        if (watchCount> 10000){
            watchCount = watchCount / 10000;
            countUnit = "万";
        }

        helper.setText(R.id.tv_watch_count, String.format(format, watchCount + countUnit));//播放次数
        ImageLoader.getInstance().displayImage(mContext, news.user_info.avatar_url, (ImageView) helper.getView(R.id.iv_avatar));//作者头像

        helper.setVisible(R.id.ll_duration, true)//显示时长
                .setText(R.id.tv_duration, TimeUtils.millis2String(news.video_duration, new SimpleDateFormat("mm:ss", Locale.getDefault())));//设置时长

        helper.setText(R.id.tv_author, news.user_info.name)//昵称
                .setText(R.id.tv_comment_count, String.valueOf(news.comment_count));//评论数


        final JCVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);
        ImageLoader.getInstance().displayImage(mContext, news.video_detail_info.detail_video_large_image.url, videoPlayer.thumbImageView, R.color.color_c0bebe);//设置缩略图

        videoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        videoPlayer.tinyBackImageView.setVisibility(GONE);
        videoPlayer.setPosition(helper.getAdapterPosition());//绑定Position

        videoPlayer.titleTextView.setText("");//清除标题,防止复用的时候出现

        videoPlayer.setOnVideoClickListener(new OnVideoClickListener() {
            @Override
            public void onVideoClickToStart() {
                //点击播放
                helper.setVisible(R.id.ll_duration, false);//隐藏时长
                helper.setVisible(R.id.ll_title,false);//隐藏标题栏
                //
                VideoPathDecoder decoder = new VideoPathDecoder() {
                    @Override
                    public void onDecodeSuccess(final String url) {
                        Logger.i("Video url:" + url);
                        AppUtils.postTaskSafely(new Runnable() {
                            @Override
                            public void run() {
                                videoPlayer.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST, news.title);
                                videoPlayer.seekToInAdvance = news.video_detail_info.progress;
                                videoPlayer.startVideo();
                            }
                        });
                    }

                    @Override
                    public void onDecodeFailure() {

                    }
                };
                decoder.decodePath(news.url);
            }
        });
    }
}
