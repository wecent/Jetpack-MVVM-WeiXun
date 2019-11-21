package com.wecent.weixun.ui.video;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.orhanobut.logger.Logger;
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

/**
 * desc: 视频列表
 * author: wecent
 * date: 2018/9/2
 */
public class VideoFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.vp_video_content)
    ViewPager vpVideoContent;
    @BindView(R.id.tb_video_chancel)
    SlidingTabLayout tbVideoChancel;

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
        mPresenter.getChannel();
    }

    @Override
    public void onReload() {
        bindData();
    }

    @Override
    public void loadData(List<Channel> channels) {
        if (channels != null) {
            Logger.e("loadVideoChannel: " + channels.toString());
            mVideoPagerAdapter = new VideoPagerAdapter(getChildFragmentManager(), channels);
            vpVideoContent.setAdapter(mVideoPagerAdapter);
            vpVideoContent.setOffscreenPageLimit(1);
            vpVideoContent.setCurrentItem(0, false);
            tbVideoChancel.setViewPager(vpVideoContent);
        } else {
            showShort("数据异常!");
        }
    }
}
