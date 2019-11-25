package com.wecent.weixun.ui.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.orhanobut.logger.Logger;
import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.video.adapter.VideoListAdapter;
import com.wecent.weixun.ui.video.contract.VideoListContract;
import com.wecent.weixun.ui.video.presenter.VideoListPresenter;
import com.wecent.weixun.widget.CustomLoadMoreView;
import com.wecent.weixun.widget.SimpleRecyclerView;
import com.wecent.weixun.widget.CustomRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

/**
 * desc: 头条新闻分类页
 * author: wecent
 * date: 2018/9/19
 */
public class VideoListFragment extends BaseFragment<VideoListPresenter> implements VideoListContract.View {

    @BindView(R.id.mRecyclerView)
    SimpleRecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private String channelCode;
    private List<News> beanList;
    private VideoListAdapter detailAdapter;
    private int upPullNum = 1;
    private int downPullNum = 1;
    private boolean isRemoveHeaderView = false;
    private CustomRefreshView mHeader;
    private PtrFrameLayout mFrame;

    public static VideoListFragment newInstance(String channelCode) {
        Bundle args = new Bundle();
        args.putString("channelCode", channelCode);
        VideoListFragment fragment = new VideoListFragment();
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
        mHeader = new CustomRefreshView(mContext);
        mPtrFrameLayout.setHeaderView(mHeader);
        mPtrFrameLayout.addPtrUIHandler(mHeader);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Logger.e(": " + downPullNum);
                mFrame = frame;
                isRemoveHeaderView = true;
                mPresenter.getData(channelCode, VideoListPresenter.ACTION_DOWN);
            }
        });
        beanList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new VideoListAdapter(beanList, getActivity()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Logger.e("onLoadMoreRequested: " + upPullNum);
                mPresenter.getData(channelCode, VideoListPresenter.ACTION_UP);
            }
        }, mRecyclerView);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerStandard videoPlayer = (JCVideoPlayerStandard) JCVideoPlayerManager.getCurrentJcvd();
                    if (videoPlayer.currentState == CURRENT_STATE_PLAYING) {
                        //如果正在播放
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                        if (firstVisibleItemPosition > videoPlayer.getPosition() || lastVisibleItemPosition < videoPlayer.getPosition()) {
                            //如果第一个可见的条目位置大于当前播放videoPlayer的位置
                            //或最后一个可见的条目位置小于当前播放videoPlayer的位置，释放资源
                            JCVideoPlayer.releaseAllVideos();
                        }
                    }
                }
            }
        });

        detailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                News news = beanList.get(position);

                String itemId = news.item_id;
                StringBuffer urlSb = new StringBuffer("http://m.toutiao.com/i");
                urlSb.append(itemId).append("/info/");
                String url = urlSb.toString();//http://m.toutiao.com/i6412427713050575361/info/
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.CHANNEL_CODE, channelCode);
                intent.putExtra(VideoDetailActivity.POSITION, position);
                intent.putExtra(VideoDetailActivity.DETAIL_URL, url);
                intent.putExtra(VideoDetailActivity.GROUP_ID, news.group_id);
                intent.putExtra(VideoDetailActivity.ITEM_ID, itemId);
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    //传递进度
                    int progress = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
                    if (progress != 0) {
                        intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                    }
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void bindData() {
        mPresenter.getData(channelCode, VideoListPresenter.ACTION_DEFAULT);
    }

    @Override
    public void onReload() {
        bindData();
    }

    @Override
    public void loadData(List<News> newsList) {
        if (newsList == null || newsList.size() == 0) {
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(false, mFrame);
            }
            showFailure();
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
            Logger.e("loadData: " + newsList.toString());
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
            Logger.e("loadMoreData: " + newsList.toString());
        }
    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.video_toast), num + ""));
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
