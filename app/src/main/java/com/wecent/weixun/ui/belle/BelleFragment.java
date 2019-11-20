package com.wecent.weixun.ui.belle;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.model.entity.BelleEntity;
import com.wecent.weixun.network.JanDanApiManager;
import com.wecent.weixun.ui.belle.adapter.BellePicAdapter;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.belle.contract.BelleContract;
import com.wecent.weixun.ui.belle.presenter.BellePresenter;
import com.wecent.weixun.widget.CustomLoadMoreView;
import com.wecent.weixun.widget.PtrWeiXunHeader;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * desc: 煎蛋
 * author: wecent .
 * date: 2018/9/2 .
 */
public class BelleFragment extends BaseFragment<BellePresenter> implements BelleContract.View {

    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private BellePicAdapter mAdapter;
    private int pageNum = 1;
    private PtrWeiXunHeader mHeader;
    private PtrFrameLayout mFrame;

    public static BelleFragment newInstance() {
        Bundle args = new Bundle();
        BelleFragment fragment = new BelleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_jiandan;
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
        setStatusBarHeight(getStatusBarHeight());

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
                mPresenter.getDetailData(JanDanApiManager.TYPE_GIRLS, pageNum);
            }
        });
        mPresenter.getDetailData(JanDanApiManager.TYPE_GIRLS, pageNum);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new BellePicAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setPreLoadNumber(1);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getDetailData(JanDanApiManager.TYPE_GIRLS, pageNum);
            }
        }, mRecyclerView);
    }

    @Override
    public void bindData() {


    }

    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight
     */
    public void setStatusBarHeight(int statusBarHeight) {
        ViewGroup.LayoutParams params = fakeStatusBar.getLayoutParams();
        params.height = statusBarHeight;
        fakeStatusBar.setLayoutParams(params);
    }

    @Override
    public void onRetry() {
        bindData();
    }

    @Override
    public void loadDetailData(String type, BelleEntity belleEntity) {
        if (belleEntity == null) {
            mPtrFrameLayout.refreshComplete();
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(false, mFrame);
            }
            showFaild();
        } else {
            pageNum++;
            mAdapter.setNewData(belleEntity.getComments());
            mPtrFrameLayout.refreshComplete();
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(true, mFrame);
            }
            showSuccess();
        }
    }

    @Override
    public void loadMoreDetailData(String type, BelleEntity belleEntity) {
        if (belleEntity == null) {
            mAdapter.loadMoreFail();
        } else {
            pageNum++;
            mAdapter.addData(belleEntity.getComments());
            mAdapter.loadMoreComplete();
        }
    }
}
