package com.wecent.weixun.ui.jandan;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.network.JanDanApi;
import com.wecent.weixun.ui.adapter.BoredPicAdapter;
import com.wecent.weixun.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * desc: 煎蛋
 * author: wecent .
 * date: 2017/9/2 .
 */
public class JanDanFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    Unbinder unbinder;
    private JanDanPagerAdapter mJanDanPagerAdapter;

    public static JanDanFragment newInstance() {
        Bundle args = new Bundle();
        JanDanFragment fragment = new JanDanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_jiandan;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
    }

    @Override
    public void bindData() {
        setStatusBarHeight(getStatusBarHeight());
        List<String> strings = new ArrayList<>();
//        strings.add("头条");
        strings.add("内涵");
        strings.add("妹子");
//        strings.add("段子");
        mJanDanPagerAdapter = new JanDanPagerAdapter(getChildFragmentManager(), strings);
        mViewpager.setAdapter(mJanDanPagerAdapter);
        mViewpager.setOffscreenPageLimit(1);
        mViewpager.setCurrentItem(0, false);
        mTabLayout.setupWithViewPager(mViewpager, true);
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

    public class JanDanPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> titles;

        public JanDanPagerAdapter(FragmentManager fm, List<String> titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public BaseFragment getItem(int position) {
            switch (position) {
//                case 0:
//                    return DetailFragment.newInstance(JanDanApi.TYPE_FRESH, new FreshNewsAdapter(getActivity(), null));
                case 0:
                    return DetailFragment.newInstance(JanDanApi.TYPE_BORED, new BoredPicAdapter(getActivity(), null));
                case 1:
                    return DetailFragment.newInstance(JanDanApi.TYPE_GIRLS, new BoredPicAdapter(getActivity(), null));
//                case 3:
//                    return DetailFragment.newInstance(JanDanApi.TYPE_Duan, new JokesAdapter(null));
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

}
