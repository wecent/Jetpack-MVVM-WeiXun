package com.wecent.weixun.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.wecent.weixun.widget.NewsDetailHeaderView;
import com.wecent.weixun.widget.PowerfulRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        setStatusBarColor(Color.parseColor("#000000"),30);

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
    }

    @Override
    public void loadNewsData(ResultResponse<NewsDetail> news) {

    }

    @Override
    public void loadConmentData(CommentResponse comment) {

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
