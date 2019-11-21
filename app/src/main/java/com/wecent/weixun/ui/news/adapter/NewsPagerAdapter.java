package com.wecent.weixun.ui.news.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.ui.news.NewsListFragment;

import java.util.List;

/**
 * desc:
 * author: wecent
 * date: 2018/9/7
 */
public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Channel> mChannels;

    public NewsPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannels = channels;
    }

    public void updateChannel(List<Channel> channels){
        this.mChannels = channels;
        notifyDataSetChanged();
    }

    @Override
    public BaseFragment getItem(int position) {
//        return DetailFragment.newInstance(mChannels.get(position).getChannelCode(), position);
        return NewsListFragment.newInstance(mChannels.get(position).getChannelCode());
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
