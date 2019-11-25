package com.wecent.weixun.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.wecent.weixun.R;

/**
 * desc: 自定义上拉加载更多布局
 * author: wecent
 * date: 2018/9/27
 */
public final class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.layout_load_more;
    }

    /**
     * 重写正在加载中布局
     *
     * @return 正在加载中布局
     */
    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    /**
     * 重写加载更多失败布局
     *
     * @return 加载更多失败布局
     */
    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_failure_view;
    }

    /**
     * 重写没有更多数据布局
     *
     * @return 没有更多数据布局
     */
    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_empty_view;
    }
}