package com.wecent.weixun.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.wecent.weixun.ui.MainActivity;
import com.wecent.weixun.ui.base.BaseActivity;
import com.wecent.weixun.ui.news.adapter.CommentAdapter;
import com.wecent.weixun.ui.news.contract.NewsDetailContract;
import com.wecent.weixun.ui.news.presenter.NewsDetailPresenter;
import com.wecent.weixun.utils.LogUtils;
import com.wecent.weixun.utils.SizeUtils;
import com.wecent.weixun.utils.TimeUtils;
import com.wecent.weixun.widget.NewsDetailHeaderView;
import com.wecent.weixun.widget.PowerfulRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc: 新闻详情页
 * author: wecent
 * date: 2018/9/19
 */
public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailContract.View,BaseQuickAdapter.RequestLoadMoreListener {

    public static final String KEY_DETAIL_URL = "key_detail_url";
    public static final String KEY_GROUP_ID = "key_group_id";
    public static final String KEY_ITEM_ID = "key_item_id";

    @BindView(R.id.iv_info_avatar)
    ImageView ivInfoAvatar;
    @BindView(R.id.tv_info_name)
    TextView tvInfoName;
    @BindView(R.id.tv_info_time)
    TextView tvInfoTime;
    @BindView(R.id.rl_status_info)
    RelativeLayout rlStatusInfo;
    @BindView(R.id.rv_comment)
    PowerfulRecyclerView rvComment;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;

    private List<CommentData> mCommentList = new ArrayList<>();
    private String mDetailUrl;
    private String mGroupId;
    private String mItemId;
    private CommentAdapter mCommentAdapter;
    private NewsDetailHeaderView mHeaderView;

    public static void launch(Activity context, String detailUrl, String groupId, String itemId) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(KEY_DETAIL_URL, detailUrl);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_ITEM_ID, itemId);
        context.startActivity(intent);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_news_detail;
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
        setStatusBarColor(R.color.config_color_white);
        setStatusBarDark(true);

        // 解决默认位置不是最顶部
        getView().setFocusable(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
    }

    @Override
    public void bindData() {
        Intent intent = getIntent();

        mDetailUrl = intent.getStringExtra(KEY_DETAIL_URL);
        mGroupId = intent.getStringExtra(KEY_GROUP_ID);
        mItemId = intent.getStringExtra(KEY_ITEM_ID);
        mItemId = mItemId.replace("i", "");

        mPresenter.getNewsData(mDetailUrl);
        mPresenter.getCommentData(mGroupId, mItemId, 1);

        mCommentAdapter = new CommentAdapter(this, R.layout.item_detail_comment, mCommentList);
        rvComment.setAdapter(mCommentAdapter);

        mHeaderView = new NewsDetailHeaderView(this);
        mCommentAdapter.addHeaderView(mHeaderView);

        mCommentAdapter.setEnableLoadMore(true);
        mCommentAdapter.setOnLoadMoreListener(this, rvComment);

        final int llInfoBottom = SizeUtils.dp2px(80);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) rvComment.getLayoutManager();
        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisibleChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisibleChildView.getHeight();
                int scrollHeight = (position) * itemHeight - firstVisibleChildView.getTop();

                LogUtils.e("scrollHeight: " + scrollHeight);
                LogUtils.e("llInfoBottom: " + llInfoBottom);

                if (!NewsDetailActivity.this.isFinishing()) {
                    //如果滚动超过用户信息一栏，显示标题栏中的用户头像和昵称
                    rlStatusInfo.setVisibility(scrollHeight > llInfoBottom ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.iv_status_back, R.id.fl_comment_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_status_back:
                //顶部返回图标
                finish();
                break;
            case R.id.fl_comment_icon:
                //底部评论图标
                RecyclerView.LayoutManager layoutManager = rvComment.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int firstPosition = linearManager.findFirstVisibleItemPosition();
                    int lastPosition = linearManager.findLastVisibleItemPosition();
                    if (firstPosition == 0 && lastPosition == 0) {
                        //处于头部，滚动到第一个条目
                        rvComment.scrollToPosition(1);
                        if (rlStatusInfo.getVisibility() == View.GONE) {
                            rlStatusInfo.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //不是头部，滚动到头部
                        rvComment.scrollToPosition(0);
                        if (rlStatusInfo.getVisibility() == View.VISIBLE) {
                            rlStatusInfo.setVisibility(View.GONE);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void loadNewsData(final ResultResponse<NewsDetail> news) {
        if (news.data == null) {
            showFailure();
        } else {
            mHeaderView.setDetail(news.data, new NewsDetailHeaderView.LoadWebListener() {
                @Override
                public void onLoadFinished() {
                    //加载完成后，显示内容布局
                    rlStatusInfo.setVisibility(View.GONE);
                    if (news.data.media_user != null){
                        ImageLoader.getInstance().displayImage(getBaseContext(), news.data.media_user.avatar_url, ivInfoAvatar);
                        tvInfoName.setText(news.data.media_user.screen_name);
                        tvInfoTime.setText(TimeUtils.getFriendlyTimeSpanByNow(news.data.publish_time * 1000L));
                    }
                    showSuccess();
                }
            });
        }
    }

    @Override
    public void loadCommentData(CommentResponse comment) {
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
    public void onReload() {

    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getCommentData(mGroupId, mItemId, mCommentList.size() / 20 + 1);
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
