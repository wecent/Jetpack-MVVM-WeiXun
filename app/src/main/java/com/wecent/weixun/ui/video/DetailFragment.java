package com.wecent.weixun.ui.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wecent.weixun.R;
import com.wecent.weixun.model.VideoChannelBean;
import com.wecent.weixun.model.VideoDetailBean;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.ui.adapter.VideoDetailAdapter;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.video.contract.VideoContract;
import com.wecent.weixun.ui.video.presenter.VideoPresenter;
import com.wecent.weixun.widget.CustomLoadMoreView;
import com.wecent.weixun.widget.PtrWeiXunHeader;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/10 .
 */
public class DetailFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    public static final String TYPEID = "typeId";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    private VideoDetailBean videoDetailBean;
    private VideoDetailAdapter detailAdapter;
    private int pageNum = 1;
    private String typeId;
    private PtrWeiXunHeader mHeader;
    private PtrFrameLayout mFrame;

    public static DetailFragment newInstance(String typeId) {
        Bundle args = new Bundle();
        args.putCharSequence(TYPEID, typeId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_detail;
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
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mHeader = new PtrWeiXunHeader(mContext);
        mPtrFrameLayout.setHeaderView(mHeader);
        mPtrFrameLayout.addPtrUIHandler(mHeader);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mFrame = frame;
                pageNum = 1;
                if (mPresenter != null) {
                    mPresenter.getVideoDetails(pageNum, "list", typeId);
                }
            }
        });
        videoDetailBean = new VideoDetailBean();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new VideoDetailAdapter(getActivity(), R.layout.item_detail_video, videoDetailBean.getItem()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getVideoDetails(pageNum, "list", typeId);
            }
        }, mRecyclerView);

    }

    @Override
    public void bindData() {
        if (getArguments() == null) return;
        typeId = getArguments().getString(TYPEID);
        if (mPresenter != null) {
            mPresenter.getVideoDetails(pageNum, "list", typeId);
        }
    }

    @Override
    public void onRetry() {
        bindData();
    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> detailBean) {
        if (detailBean == null) {
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(false, mFrame);
            }
            showFaild();
            return;
        }
        pageNum++;
        detailAdapter.setNewData(detailBean.get(0).getItem());
        mPtrFrameLayout.refreshComplete();
        if (mHeader != null && mFrame != null) {
            mHeader.refreshComplete(true, mFrame);
        }
        showSuccess();
    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> detailBean) {
        if (detailBean == null) {
            detailAdapter.loadMoreEnd();
            return;
        }
        pageNum++;
        detailAdapter.addData(detailBean.get(0).getItem());
        detailAdapter.loadMoreComplete();
    }

    @Override
    public void loadVideoChannel(List<VideoChannelBean> channelBean) {

    }
}
