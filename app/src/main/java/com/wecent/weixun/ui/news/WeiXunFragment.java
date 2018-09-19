package com.wecent.weixun.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.animation.SlideEnter.SlideRightEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.wecent.weixun.WXApplication;
import com.wecent.weixun.R;
import com.wecent.weixun.model.NewsDetail;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.network.NewsApi;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.news.adapter.WeiXunListAdapter;
import com.wecent.weixun.ui.news.contract.WeiXunContract;
import com.wecent.weixun.ui.news.presenter.WeiXunPresenter;
import com.wecent.weixun.utils.ContextUtils;
import com.wecent.weixun.widget.CustomLoadMoreView;
import com.wecent.weixun.widget.NewsDelPop;
import com.wecent.weixun.widget.PtrWeiXunHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/8 .
 */
public class WeiXunFragment extends BaseFragment<WeiXunPresenter> implements WeiXunContract.View {
    private static final String TAG = "DetailFragment";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private NewsDelPop newsDelPop;
    private String channelCode;
    private List<News> beanList;
    private WeiXunListAdapter detailAdapter;
    private int upPullNum = 1;
    private int downPullNum = 1;
    private boolean isRemoveHeaderView = false;
    private PtrWeiXunHeader mHeader;
    private PtrFrameLayout mFrame;

    public static WeiXunFragment newInstance(String newsid) {
        Bundle args = new Bundle();
        args.putString("newsid", newsid);
        WeiXunFragment fragment = new WeiXunFragment();
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
                Log.i(TAG, "onRefreshBegin: " + downPullNum);
                mFrame = frame;
                isRemoveHeaderView = true;
                mPresenter.getData(channelCode, NewsApi.ACTION_DOWN);
            }
        });
        beanList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new WeiXunListAdapter(beanList, getActivity()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.i(TAG, "onLoadMoreRequested: " + upPullNum);
                mPresenter.getData(channelCode, NewsApi.ACTION_UP);

            }
        }, mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) baseQuickAdapter.getItem(i);
                toRead(itemBean);

            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) baseQuickAdapter.getItem(i);
//                switch (view.getId()) {
//                    case R.id.iv_close:
//                        view.getHeight();
//                        int[] location = new int[2];
//                        view.getLocationInWindow(location);
//                        Log.i("DetailFragment", "点击的item的高度:" + view.getHeight() + "x值:" + location[0] + "y值" + location[1]);
//                        if (itemBean.getStyle() == null) return;
//                        if (ContextUtils.getSreenWidth(WXApplication.getContext()) - 50 - location[1] < ContextUtils.dip2px(WXApplication.getContext(), 80)) {
//                            newsDelPop
//                                    .anchorView(view)
//                                    .gravity(Gravity.TOP)
//                                    .setBackReason(itemBean.getStyle().getBackreason(), true, i)
//                                    .show();
//                        } else {
//                            newsDelPop
//                                    .anchorView(view)
//                                    .gravity(Gravity.BOTTOM)
//                                    .setBackReason(itemBean.getStyle().getBackreason(), false, i)
//                                    .show();
//                        }
//                        break;
//                }
            }
        });

        newsDelPop = new NewsDelPop(getActivity())
                .alignCenter(false)
                .widthScale(0.95f)
                .showAnim(new SlideRightEnter())
                .dismissAnim(new SlideRightExit())
                .offset(-100, 0)
                .dimEnabled(true);
        newsDelPop.setClickListener(new NewsDelPop.onClickListener() {
            @Override
            public void onClick(int position) {
                newsDelPop.dismiss();
                detailAdapter.remove(position);
                showToast(0, false);
            }
        });
    }

    @Override
    public void bindData() {
        if (getArguments() == null) return;
        channelCode = getArguments().getString("channelCode");
        mPresenter.getData(channelCode, NewsApi.ACTION_DEFAULT);
    }

    @Override
    public void onRetry() {
        bindData();
    }

    @Override
    public void loadData(List<News> newsList) {
        if (newsList == null || newsList.size() == 0) {
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(false, mFrame);
            }
            showFaild();
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                detailAdapter.removeAllHeaderView();
            }
            detailAdapter.setNewData(newsList);
            showToast(newsList.size(), true);
            mPtrFrameLayout.refreshComplete();
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(true, mFrame);
            }
            showSuccess();
            Log.i(TAG, "loadData: " + newsList.toString());
        }
    }

    @Override
    public void loadMoreData(List<News> newsList) {
        if (newsList == null || newsList.size() == 0) {
            detailAdapter.loadMoreFail();
        } else {
            upPullNum++;
            detailAdapter.addData(newsList);
            detailAdapter.loadMoreComplete();
            Log.i(TAG, "loadMoreData: " + newsList.toString());
        }
    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            mTvToast.setText("将为你减少此类内容");
        }
        mRlTopToast.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mRlTopToast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mRlTopToast)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });
    }

    private void toRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getItemType()) {
            case NewsDetail.ItemBean.TYPE_DOC_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG:
                Intent intent = new Intent(getActivity(), NewsArticleActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsDetail.ItemBean.TYPE_SLIDE:
                NewsImageActivity.launch(getActivity(), itemBean);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG:
                NewsAdvertActivity.launch(getActivity(), itemBean.getLink().getWeburl());
                break;
            case NewsDetail.ItemBean.TYPE_PHVIDEO:
                T("TYPE_PHVIDEO");
                break;
        }
    }

}
