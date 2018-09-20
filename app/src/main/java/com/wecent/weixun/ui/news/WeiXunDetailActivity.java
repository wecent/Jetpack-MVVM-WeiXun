package com.wecent.weixun.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.model.entity.CommentData;
import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.ResultResponse;
import com.wecent.weixun.ui.base.BaseActivity;
import com.wecent.weixun.ui.news.adapter.CommentAdapter;
import com.wecent.weixun.ui.news.contract.WeiXunDetailContract;
import com.wecent.weixun.ui.news.presenter.WeiXunDetailPresenter;
import com.wecent.weixun.utils.ImageLoaderUtil;
import com.wecent.weixun.widget.NewsDetailHeaderView;
import com.wecent.weixun.widget.PowerfulRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * desc: 头条新闻详情页 .
 * author: wecent .
 * date: 2017/9/19 .
 */

public class WeiXunDetailActivity extends BaseActivity<WeiXunDetailPresenter> implements WeiXunDetailContract.View,BaseQuickAdapter.RequestLoadMoreListener {

    public static final String CHANNEL_CODE = "channelCode";
    public static final String PROGRESS = "progress";
    public static final String POSITION = "position";
    public static final String DETAIL_URL = "detailUrl";
    public static final String GROUP_ID = "groupId";
    public static final String ITEM_ID = "itemId";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_topLogo)
    ImageView ivTopLogo;
    @BindView(R.id.tv_topname)
    TextView tvTopname;
    @BindView(R.id.tv_TopUpdateTime)
    TextView tvTopUpdateTime;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.rv_comment)
    PowerfulRecyclerView rvComment;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @BindView(R.id.fl_comment_icon)
    FrameLayout flCommentIcon;

    private List<CommentData> mCommentList = new ArrayList<>();
    private String mDetalUrl;
    private String mGroupId;
    private String mItemId;
    private CommentAdapter mCommentAdapter;
    private CommentResponse mCommentResponse;
    private NewsDetailHeaderView mHeaderView;
    private String mChannelCode;
    private int mPosition;

    @Override
    public int getContentLayout() {
        return R.layout.activity_weixun_detail;
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
        setStatusBarColor(Color.parseColor("#BDBDBD"),30);

        // 解决ScrollView默认位置不是最顶部
        getView().setFocusable(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
    }

    @Override
    public void bindData() {
        Intent intent = getIntent();

        mChannelCode = intent.getStringExtra(CHANNEL_CODE);
        mPosition = intent.getIntExtra(POSITION, 0);

        mDetalUrl = intent.getStringExtra(DETAIL_URL);
        mGroupId = intent.getStringExtra(GROUP_ID);
        mItemId = intent.getStringExtra(ITEM_ID);
        mItemId = mItemId.replace("i", "");

        mPresenter.getNewsData(mDetalUrl);
        mPresenter.getConmentData(mGroupId, mItemId, 1);

        mCommentAdapter = new CommentAdapter(this, R.layout.item_weixun_detail_comment, mCommentList);
        rvComment.setAdapter(mCommentAdapter);

        mHeaderView = new NewsDetailHeaderView(this);
        mCommentAdapter.addHeaderView(mHeaderView);

        mCommentAdapter.setEnableLoadMore(true);
        mCommentAdapter.setOnLoadMoreListener(this, rvComment);

        final int llInfoBottom = mHeaderView.llInfo.getBottom();
        final LinearLayoutManager layoutManager = (LinearLayoutManager) rvComment.getLayoutManager();
        rvComment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisiableChildView.getHeight();
                int scrollHeight = (position) * itemHeight - firstVisiableChildView.getTop();

                KLog.i("scrollHeight: " + scrollHeight);
                KLog.i("llInfoBottom: " + llInfoBottom);

                rlTop.setVisibility(scrollHeight > llInfoBottom ? View.VISIBLE : View.GONE);//如果滚动超过用户信息一栏，显示标题栏中的用户头像和昵称
            }
        });
    }

    @OnClick({R.id.fl_comment_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
    public void loadNewsData(ResultResponse<NewsDetail> news) {
        mHeaderView.setDetail(news.data, new NewsDetailHeaderView.LoadWebListener() {
            @Override
            public void onLoadFinished() {
                //加载完成后，显示内容布局
                showFaild();
            }
        });

        rlTop.setVisibility(View.GONE);

        if (news.data.media_user != null){
            ImageLoaderUtil.LoadImage(this,news.data.media_user.avatar_url, ivTopLogo);
            tvTopname.setText(news.data.media_user.screen_name);
        }
        showSuccess();
    }

    @Override
    public void loadConmentData(CommentResponse comment) {
        mCommentResponse = comment;

        if (comment.data == null || comment.data.size() == 0){
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
        }else{
            mCommentAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getConmentData(mGroupId, mItemId, mCommentList.size() / 20 + 1);
    }

    /**
     *  发送事件，用于更新上个页面的播放进度以及评论数
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
