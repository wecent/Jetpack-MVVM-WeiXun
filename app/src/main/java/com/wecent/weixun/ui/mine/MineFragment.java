package com.wecent.weixun.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecent.weixun.R;
import com.wecent.weixun.component.ApplicationComponent;
import com.wecent.weixun.ui.base.BaseFragment;
import com.wecent.weixun.widget.trans.OnTransClickListener;
import com.wecent.weixun.widget.trans.TransScrollView;
import com.wecent.weixun.widget.trans.TransToolBar;

import butterknife.BindView;

/**
 * desc: 个人页面
 * author: wecent
 * date: 2018/9/2
 */
public class MineFragment extends BaseFragment implements OnTransClickListener, TransScrollView.TranslucentChangedListener {

    @BindView(R.id.v_status)
    View vStatus;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_weibo)
    ImageView ivWeibo;
    @BindView(R.id.tv_weibo)
    TextView tvWeibo;
    @BindView(R.id.iv_zhihu)
    ImageView ivZhihu;
    @BindView(R.id.tv_zhihu)
    TextView tvZhihu;
    @BindView(R.id.iv_ding)
    ImageView ivDing;
    @BindView(R.id.tv_ding)
    TextView tvDing;
    @BindView(R.id.iv_taobao)
    ImageView ivTaobao;
    @BindView(R.id.tv_taobao)
    TextView tvTaobao;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.iv_sketch)
    ImageView ivSketch;
    @BindView(R.id.tv_ie)
    TextView tvIe;
    @BindView(R.id.iv_google)
    ImageView ivGoogle;
    @BindView(R.id.tv_google)
    TextView tvGoogle;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.tv_facebook)
    TextView tvFacebook;
    @BindView(R.id.iv_dropbox)
    ImageView ivDropbox;
    @BindView(R.id.tv_dropbox)
    TextView tvDropbox;
    @BindView(R.id.iv_linkedin)
    ImageView ivLinkedin;
    @BindView(R.id.tv_linkedin)
    TextView tvLinkedin;
    @BindView(R.id.iv_reddit)
    ImageView ivReddit;
    @BindView(R.id.tv_reddit)
    TextView tvReddit;
    @BindView(R.id.scrollTrans)
    TransScrollView scrollTrans;
    @BindView(R.id.toolTrans)
    TransToolBar toolTrans;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        //初始actionBar
        toolTrans.setTitleText("我的");
        toolTrans.setTitleTextColor(R.color.config_color_white);
        toolTrans.setRightText("设置");
        toolTrans.setRightTextColor(R.color.config_color_white);
        //开启渐变
        toolTrans.setNeedTranslucent();
        //设置状态栏高度
//        toolTrans.setStatusBarHeight(getStatusBarHeight());
        //设置透明度变化监听
        scrollTrans.setTranslucentChangedListener(this);
        //关联需要渐变的视图
        scrollTrans.setTransView(toolTrans);
        //设置ActionBar键渐变颜色
        scrollTrans.setTransColor(getResources().getColor(R.color.config_color_blue));
        //关联伸缩的视图
        scrollTrans.setPullZoomView(llHeader);
    }

    @Override
    public void bindData() {

    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onTranslucentChanged(int transAlpha) {
        toolTrans.mTransTitle.setVisibility(transAlpha > 45 ? View.VISIBLE : View.GONE);
        toolTrans.mTransLeft.setVisibility(transAlpha > 45 ? View.VISIBLE : View.GONE);
        toolTrans.mTransRight.setVisibility(transAlpha > 45 ? View.VISIBLE : View.GONE);
    }
}
