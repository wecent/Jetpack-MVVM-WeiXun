package com.wecent.weixun.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wecent.weixun.R;
import com.wecent.weixun.WXApplication;
import com.wecent.weixun.ui.inter.IBase;
import com.wecent.weixun.utils.ToastUtils;
import com.wecent.weixun.widget.MultiStateView;
import com.wecent.weixun.widget.SimpleMultiStateView;
import com.wecent.weixun.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * desc:
 * author: wecent .
 * date: 2018/9/2 .
 */
public abstract class BaseActivity<T1 extends BaseContract.BasePresenter> extends SupportActivity implements IBase, BaseContract.BaseView, BGASwipeBackHelper.Delegate {

    protected View mRootView;
    protected LoadingDialog mLoadingDialog;
    protected LoadingDialog mSuccessDialog;
    protected LoadingDialog mFailureDialog;
    protected Handler mHandler = new Handler();
    protected boolean mStatusBarDark = true;
    protected boolean mFitsSystemWindows = true;
    protected int mStatusBarColor = R.color.config_color_white;
    protected int mNavigationBarColor = R.color.config_color_blank_3;
    Unbinder unbinder;

    @Nullable
    @BindView(R.id.SimpleMultiStateView)
    SimpleMultiStateView mSimpleMultiStateView;

    @Nullable
    @Inject
    protected T1 mPresenter;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mRootView = createView(null, null, savedInstanceState);
        setContentView(mRootView);
        initInjector(WXApplication.getInstance().getApplicationComponent());
        attachView();
        bindView(mRootView, savedInstanceState);
        initStateView();
        initImmersionBar();
        bindData();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(getContentLayout(), container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    private void initStateView() {
        if (mSimpleMultiStateView == null) return;
        mSimpleMultiStateView.setEmptyResource(R.layout.layout_state_empty)
                .setRetryResource(R.layout.layout_state_reload)
                .setLoadingResource(R.layout.layout_state_loading)
                .setNoNetResource(R.layout.layout_state_nonet)
                .build()
                .setonReLoadlistener(new MultiStateView.onReLoadlistener() {
                    @Override
                    public void onReload() {
                        onRetry();
                    }
                });
    }

    private void initImmersionBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .fitsSystemWindows(mFitsSystemWindows)
                .statusBarColor(mStatusBarColor)
                .statusBarDarkFont(mStatusBarDark)
                .navigationBarColor(mNavigationBarColor)
                .keyboardEnable(true)
                .init();
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 设置顶部状态栏字体颜色为深色
     *
     * @param isFits
     */
    protected void setFitsSystemWindows(boolean isFits) {
        mFitsSystemWindows = isFits;
        initImmersionBar();
    }

    /**
     * 设置顶部状态栏字体颜色为深色
     *
     * @param isDark
     */
    protected void setStatusBarDark(boolean isDark) {
        mStatusBarDark = isDark;
        initImmersionBar();
    }

    /**
     * 设置顶部状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(int color) {
        mStatusBarColor = color;
        initImmersionBar();
    }

    /**
     * 设置底部导航栏颜色
     *
     * @param color
     */
    protected void setNavigationBarColor(int color) {
        mNavigationBarColor = color;
        initImmersionBar();
    }

    protected void showLoadingDialog(String str) {
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(str)
                .create();

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    protected void showSuccessDialog(String str) {
        mSuccessDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(str)
                .create();

        if (!mSuccessDialog.isShowing()) {
            mSuccessDialog.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSuccessDialog != null && mSuccessDialog.isShowing()) {
                        mSuccessDialog.dismiss();
                        mSuccessDialog = null;
                    }
                }
            }, 1000);
        }
    }

    protected void showFailureDialog(String str) {
        mFailureDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(str)
                .create();

        if (!mFailureDialog.isShowing()) {
            mFailureDialog.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mFailureDialog != null && mFailureDialog.isShowing()) {
                        mFailureDialog.dismiss();
                        mFailureDialog = null;
                    }
                }
            }, 1000);
        }
    }

    protected SimpleMultiStateView getStateView() {
        return mSimpleMultiStateView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showFaild() {
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
    public void onSwipeBackLayoutSlide(float v) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

}
