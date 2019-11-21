package com.wecent.weixun.ui.video;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.loader.ImageLoader;
import com.wecent.weixun.model.entity.CommentData;
import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.ResultResponse;
import com.wecent.weixun.ui.base.BaseActivity;
import com.wecent.weixun.ui.video.adapter.CommentAdapter;
import com.wecent.weixun.ui.video.contract.VideoDetailContract;
import com.wecent.weixun.ui.video.presenter.VideoDetailPresenter;
import com.wecent.weixun.utils.TimeUtils;
import com.wecent.weixun.widget.PowerfulRecyclerView;
import com.wecent.weixun.widget.VideoPathDecoder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * desc: 头条新闻详情页
 * author: wecent
 * date: 2018/9/19
 */

public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements VideoDetailContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    public static final String CHANNEL_CODE = "channelCode";
    public static final String PROGRESS = "progress";
    public static final String POSITION = "position";
    public static final String DETAIL_URL = "detailUrl";
    public static final String GROUP_ID = "groupId";
    public static final String ITEM_ID = "itemId";

    @BindView(R.id.video_player)
    JCVideoPlayerStandard videoPlayer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.rv_comment)
    PowerfulRecyclerView rvComment;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @BindView(R.id.fl_comment_icon)
    FrameLayout flCommentIcon;

    private SensorManager mSensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    private List<CommentData> mCommentList = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private String mDetalUrl;
    private String mGroupId;
    private String mItemId;
    private int mProgress;
    private int mPosition;

    @Override
    public int getContentLayout() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setStatusBarColor(R.color.config_color_black);
        setStatusBarDark(false);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
    }

    @Override
    public void bindData() {
        Intent intent = getIntent();
        mProgress = intent.getIntExtra(PROGRESS, 0);
        mDetalUrl = intent.getStringExtra(DETAIL_URL);
        mPosition = intent.getIntExtra(POSITION, 0);
        mGroupId = intent.getStringExtra(GROUP_ID);
        mItemId = intent.getStringExtra(ITEM_ID);

        mPresenter.getNewsData(mDetalUrl);
        mPresenter.getConmentData(mGroupId, mItemId, 1);

        mCommentAdapter = new CommentAdapter(this, R.layout.item_detail_comment, mCommentList);
        rvComment.setAdapter(mCommentAdapter);

        mCommentAdapter.setEnableLoadMore(true);
        mCommentAdapter.setOnLoadMoreListener(this, rvComment);

        videoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        videoPlayer.titleTextView.setVisibility(GONE);
        videoPlayer.setPosition(mPosition);
    }

    @OnClick({R.id.iv_back, R.id.fl_comment_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //底部评论的图标
                finish();
                break;
                case R.id.fl_comment_icon:
                //底部评论的图标
                RecyclerView.LayoutManager layoutManager = rvComment.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int firstPosition = linearManager.findFirstVisibleItemPosition();
                    int last = linearManager.findLastVisibleItemPosition();
                    if (firstPosition == 0 && last == 0) {
                        //处于头部，滚动到第一个条目
                        rvComment.scrollToPosition(1);
                    } else {
                        //不是头部，滚动到头部
                        rvComment.scrollToPosition(0);
                    }
                }
                break;
        }
    }

    @Override
    public void loadNewsData(final ResultResponse<NewsDetail> news) {
        VideoPathDecoder decoder = new VideoPathDecoder() {
            @Override
            public void onDecodeSuccess(final String url) {
                Logger.i("Video url:" + url);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
//                        videoPlayer.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST, news.data.title);
//                        videoPlayer.seekToInAdvance = mProgress;//设置进度
//                        videoPlayer.startVideo();
                    }
                });
            }

            @Override
            public void onDecodeFailure() {

            }
        };
        decoder.decodePath(news.data.url);
        Logger.i("Video url:" + news.data.url);

        if (news.data.media_user == null) {
            //如果没有用户信息
            llInfo.setVisibility(GONE);
        } else {
            if (!TextUtils.isEmpty(news.data.media_user.avatar_url)) {
                ImageLoader.getInstance().displayImage(getBaseContext(), news.data.media_user.avatar_url, ivAvatar);
            }
            tvAuthor.setText(news.data.media_user.screen_name);
            tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(news.data.publish_time * 1000L));
        }
        Logger.e("onGetNewsDetailSuccess", news.data.url);
        showSuccess();
    }

    @Override
    public void loadConmentData(CommentResponse comment) {
        if (comment.data == null || comment.data.size() == 0) {
            //没有评论了
            mCommentAdapter.loadMoreEnd();
            return;
        }

        if (comment.total_number > 0) {
            //如果评论数大于0，显示红点
            tvCommentCount.setVisibility(View.VISIBLE);
            tvCommentCount.setText(String.valueOf(comment.total_number));
        }

        mCommentList.addAll(comment.data);
        mCommentAdapter.notifyDataSetChanged();

        if (!comment.has_more) {
            mCommentAdapter.loadMoreEnd();
        } else {
            mCommentAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getConmentData(mGroupId, mItemId, mCommentList.size() / 20 + 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 发送事件，用于更新上个页面的播放进度以及评论数
     */
    private void postVideoEvent(boolean isVideoDetail) {
//        DetailCloseEvent event = new DetailCloseEvent();
//        event.setChannelCode(mChannelCode);
//        event.setPosition(mPosition);
//
//        if (mCommentResponse != null){
//            event.setCommentCount(mCommentResponse.total_number);
//        }
//
//        if (isVideoDetail && JCMediaManager.instance().mediaPlayer != null && JCVideoPlayerManager.getCurrentJcvd() != null){
//            //如果是视频详情
//            int progress = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
//            event.setProgress(progress);
//        }
//
//        EventBus.getDefault().postSticky(event);
        finish();
    }

}
