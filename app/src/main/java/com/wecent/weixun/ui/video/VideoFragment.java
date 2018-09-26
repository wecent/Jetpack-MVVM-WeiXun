package com.wecent.weixun.ui.video;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.socks.library.KLog;
import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.video.adapter.VideoPagerAdapter;
import com.wecent.weixun.ui.video.contract.VideoContract;
import com.wecent.weixun.ui.video.presenter.VideoPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * desc: 视频页面
 * author: wecent .
 * date: 2017/9/2 .
 */
public class VideoFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.SlidingTabLayout)
    SlidingTabLayout mTabLayout;
    Unbinder unbinder;

    private VideoPagerAdapter mVideoPagerAdapter;

    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_video;
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

    }

    @Override
    public void bindData() {
        setStatusBarHeight(getStatusBarHeight());
        mPresenter.getChannel();
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
    public void loadData(List<Channel> channels) {
        if (channels != null) {
            KLog.e("loadVideoChannel: " + channels.toString());
            mVideoPagerAdapter = new VideoPagerAdapter(getChildFragmentManager(), channels);
            mViewpager.setAdapter(mVideoPagerAdapter);
            mViewpager.setOffscreenPageLimit(1);
            mViewpager.setCurrentItem(0, false);
            mTabLayout.setViewPager(mViewpager);
        } else {
            T("数据异常");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
