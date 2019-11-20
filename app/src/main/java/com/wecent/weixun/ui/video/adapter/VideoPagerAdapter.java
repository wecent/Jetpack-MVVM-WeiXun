package com.wecent.weixun.ui.video.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.video.VideoListFragment;

import java.util.List;

/**
 * desc:
 * author: wecent .
 * date: 2018/9/10 .
 */
public class VideoPagerAdapter extends FragmentStatePagerAdapter {

    private List<Channel> mChannels;


    public VideoPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannels = channels;
    }

    @Override
    public BaseFragment getItem(int position) {
        return VideoListFragment.newInstance(mChannels.get(position).getChannelCode());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getChannelName();
    }

    @Override
    public int getCount() {
        return mChannels != null ? mChannels.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
