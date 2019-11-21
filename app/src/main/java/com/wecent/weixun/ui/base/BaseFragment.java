package com.wecent.weixun.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wecent.weixun.WXApplication;
import com.wecent.weixun.R;
import com.wecent.weixun.ui.inter.IBase;
import com.wecent.weixun.utils.DialogHelper;
import com.wecent.weixun.utils.ToastUtils;
import com.wecent.weixun.widget.MultiStateView;
import com.wecent.weixun.widget.SimpleMultiStateView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * desc:
 * author: wecent
 * date: 2018/9/2
 */
public abstract class BaseFragment<T1 extends BaseContract.BasePresenter> extends SupportFragment implements IBase, BaseContract.BaseView {

    protected Context mContext;
    protected View mRootView;
    Unbinder unbinder;

    @Nullable
    @Inject
    protected T1 mPresenter;

    @Nullable
    @BindView(R.id.SimpleMultiStateView)
    SimpleMultiStateView mSimpleMultiStateView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = createView(inflater, container, savedInstanceState);
        }

        mContext = mRootView.getContext();
        return mRootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector(WXApplication.getInstance().getApplicationComponent());
        attachView();
        bindView(view, savedInstanceState);
        initStateView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        bindData();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Nullable
    @Override
    public View getView() {
        return mRootView;
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onReload() {

    }

    private void initStateView() {
        if (mSimpleMultiStateView == null) return;
        mSimpleMultiStateView.setEmptyResource(R.layout.layout_state_empty)
                .setRetryResource(R.layout.layout_state_reload)
                .setLoadingResource(R.layout.layout_state_loading)
                .setNoNetResource(R.layout.layout_state_nonet)
                .build()
                .setOnReloadListener(new MultiStateView.OnReloadListener() {
                    @Override
                    public void onReload() {
                        onReload();
                    }
                });
    }

    protected void showLoadingDialog(String str) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.showLoadingDialog(str);
    }

    protected void hideLoadingDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.hideLoadingDialog();
    }

    protected void showSuccessDialog(String str) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.showSuccessDialog(str);
    }

    protected void showFailureDialog(String str) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.showFailureDialog(str);
    }

    /**
     * 设置顶部状态栏字体颜色为深色
     *
     * @param isFits
     */
    protected void setFitsSystemWindows(boolean isFits) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setFitsSystemWindows(isFits);
    }

    /**
     * 设置顶部状态栏字体颜色为深色
     *
     * @param isDark
     */
    protected void setStatusBarDark(boolean isDark) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setStatusBarDark(isDark);
    }

    /**
     * 设置顶部状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(int color) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setStatusBarColor(color);
    }

    /**
     * 设置底部导航栏颜色
     *
     * @param color
     */
    protected void setNavigationBarColor(int color) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setNavigationBarColor(color);
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        hideLoadingDialog();
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showFailure() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showNoNet() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showNoNetView();
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    protected void showShort(String string) {
        ToastUtils.showShort(string);
    }

    protected void showLong(String string) {
        ToastUtils.showLong(string);
    }


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
