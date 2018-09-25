package com.wecent.weixun.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.socks.library.KLog;
import com.wecent.weixun.R;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.news.adapter.NewsListAdapter;
import com.wecent.weixun.ui.news.contract.NewsListContract;
import com.wecent.weixun.ui.news.presenter.NewsListPresenter;
import com.wecent.weixun.widget.CustomLoadMoreView;
import com.wecent.weixun.widget.PowerfulRecyclerView;
import com.wecent.weixun.widget.PtrWeiXunHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * desc: 头条新闻分类页 .
 * author: wecent .
 * date: 2017/9/19 .
 */
public class NewsListFragment extends BaseFragment<NewsListPresenter> implements NewsListContract.View {

    @BindView(R.id.mRecyclerView)
    PowerfulRecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private String channelCode;
    private List<News> beanList;
    private NewsListAdapter detailAdapter;
    private int upPullNum = 1;
    private int downPullNum = 1;
    private boolean isRemoveHeaderView = false;
    private PtrWeiXunHeader mHeader;
    private PtrFrameLayout mFrame;

    public static NewsListFragment newInstance(String channelCode) {
        Bundle args = new Bundle();
        args.putString("channelCode", channelCode);
        NewsListFragment fragment = new NewsListFragment();
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
        if (getArguments() == null) return;
        channelCode = getArguments().getString("channelCode");

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
                KLog.e("onRefreshBegin: " + downPullNum);
                mFrame = frame;
                isRemoveHeaderView = true;
                mPresenter.getData(channelCode, NewsListPresenter.ACTION_DOWN);
            }
        });
        beanList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new NewsListAdapter(beanList, channelCode, getActivity()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                KLog.e("onLoadMoreRequested: " + upPullNum);
                mPresenter.getData(channelCode, NewsListPresenter.ACTION_UP);
            }
        }, mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                News news = beanList.get(position);

                String itemId = news.item_id;
                StringBuffer urlSb = new StringBuffer("http://m.toutiao.com/i");
                urlSb.append(itemId).append("/info/");
                String url = urlSb.toString();//http://m.toutiao.com/i6412427713050575361/info/
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.CHANNEL_CODE, channelCode);
                intent.putExtra(NewsDetailActivity.POSITION, position);
                intent.putExtra(NewsDetailActivity.DETAIL_URL, url);
                intent.putExtra(NewsDetailActivity.GROUP_ID, news.group_id);
                intent.putExtra(NewsDetailActivity.ITEM_ID, itemId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void bindData() {
        mPresenter.getData(channelCode, NewsListPresenter.ACTION_DEFAULT);
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
            beanList.addAll(newsList);
            detailAdapter.setNewData(newsList);
            showToast(newsList.size(), true);
            mPtrFrameLayout.refreshComplete();
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(true, mFrame);
            }
            showSuccess();
            KLog.e("loadData: " + newsList.toString());
        }
    }

    @Override
    public void loadMoreData(List<News> newsList) {
        if (newsList == null || newsList.size() == 0) {
            detailAdapter.loadMoreFail();
        } else {
            upPullNum++;
            beanList.addAll(newsList);
            detailAdapter.addData(newsList);
            detailAdapter.loadMoreComplete();
            KLog.e("loadMoreData: " + newsList.toString());
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
}
