package com.wecent.weixun.ui.news;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.component.DaggerHttpComponent;
import com.wecent.weixun.database.ChannelDao;
import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.model.event.NewChannelEvent;
import com.wecent.weixun.model.event.SelectChannelEvent;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.news.adapter.NewsPagerAdapter;
import com.wecent.weixun.ui.news.contract.NewsContract;
import com.wecent.weixun.ui.news.presenter.NewsPresenter;
import com.wecent.weixun.widget.ChannelDialogFragment;
import com.wecent.weixun.widget.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * desc: 新闻页面
 * author: wecent
 * date: 2018/9/7
 */
public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View {

    @BindView(R.id.tb_channel_slide)
    SlidingTabLayout tbChannelSlide;
    @BindView(R.id.vp_news_content)
    CustomViewPager vpNewsContent;

    private NewsPagerAdapter mNewsPagerAdapter;

    private List<Channel> mSelectedData;
    private List<Channel> mUnSelectedData;

    private int selectedIndex;
    private String selectedChannel;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news;
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
        vpNewsContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                selectedChannel = mSelectedData.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void bindData() {
        mSelectedData = new ArrayList<>();
        mUnSelectedData = new ArrayList<>();
        mPresenter.getChannel();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> unSelectedDatas) {
        if (channels != null) {
            mSelectedData.clear();
            mSelectedData.addAll(channels);
            mUnSelectedData.clear();
            mUnSelectedData.addAll(unSelectedDatas);
            mNewsPagerAdapter = new NewsPagerAdapter(getChildFragmentManager(), channels);
            vpNewsContent.setAdapter(mNewsPagerAdapter);
            vpNewsContent.setOffscreenPageLimit(2);
            vpNewsContent.setCurrentItem(0, false);
            tbChannelSlide.setViewPager(vpNewsContent);
        } else {
            showShort("数据异常");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateChannel(NewChannelEvent event) {
        if (event == null) return;
        if (event.selectedDatas != null && event.unSelectedDatas != null) {
            mSelectedData = event.selectedDatas;
            mUnSelectedData = event.unSelectedDatas;
            mNewsPagerAdapter.updateChannel(mSelectedData);
            tbChannelSlide.notifyDataSetChanged();
            ChannelDao.saveChannels(event.allChannels);

            List<String> integers = new ArrayList<>();
            for (Channel channel : mSelectedData) {
                integers.add(channel.getChannelName());
            }
            if (TextUtils.isEmpty(event.firstChannelName)) {
                if (!integers.contains(selectedChannel)) {
                    selectedChannel = mSelectedData.get(selectedIndex).getChannelName();
                    vpNewsContent.setCurrentItem(selectedIndex, false);
                } else {
                    setViewpagerPosition(integers, selectedChannel);
                }
            } else {
                setViewpagerPosition(integers, event.firstChannelName);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectChannel(SelectChannelEvent selectChannelEvent) {
        if (selectChannelEvent == null) return;
        List<String> integers = new ArrayList<>();
        for (Channel channel : mSelectedData) {
            integers.add(channel.getChannelName());
        }
        setViewpagerPosition(integers, selectChannelEvent.channelName);
    }

    /**
     * 设置 当前选中页
     *
     * @param integers
     * @param channelName
     */
    private void setViewpagerPosition(List<String> integers, String channelName) {
        if (TextUtils.isEmpty(channelName) || integers == null) return;
        for (int j = 0; j < integers.size(); j++) {
            if (integers.get(j).equals(channelName)) {
                selectedChannel = integers.get(j);
                selectedIndex = j;
                break;
            }
        }
        vpNewsContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                vpNewsContent.setCurrentItem(selectedIndex, false);
            }
        }, 100);
    }

    @OnClick(R.id.iv_channel_edit)
    public void onViewClicked() {
        ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(mSelectedData, mUnSelectedData);
        dialogFragment.show(getChildFragmentManager(), "CHANNEL");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
