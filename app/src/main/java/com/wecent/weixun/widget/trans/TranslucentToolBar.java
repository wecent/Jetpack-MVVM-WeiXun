package com.wecent.weixun.widget.trans;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecent.weixun.R;

/**
 * desc:
 * author: wecent .
 * date: 2017/9/2 .
 */

public final class TranslucentToolBar extends LinearLayout {

    private LinearLayout mTransRoot;
    private View mTransStatus;
    public TextView mTransTitle;
    public TextView mTransLeft;
    public TextView mTransRight;

    public TranslucentToolBar(Context context) {
        this(context, null);
    }

    public TranslucentToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTransContent();
    }

    public TranslucentToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initTransContent() {
        setOrientation(HORIZONTAL);
        View contentView = inflate(getContext(), R.layout.layout_toolbar_trans, this);
        mTransRoot = (LinearLayout) contentView.findViewById(R.id.ll_trans_root);
        mTransStatus = contentView.findViewById(R.id.v_trans_status);
        mTransTitle = (TextView) contentView.findViewById(R.id.tv_trans_title);
        mTransTitle.setVisibility(View.GONE);
        mTransLeft = (TextView) contentView.findViewById(R.id.tv_trans_left);
        mTransLeft.setVisibility(View.GONE);
        mTransRight = (TextView) contentView.findViewById(R.id.tv_trans_right);
        mTransRight.setVisibility(View.GONE);
    }

    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight
     */
    public void setStatusBarHeight(int statusBarHeight) {
        ViewGroup.LayoutParams params = mTransStatus.getLayoutParams();
        params.height = statusBarHeight;
        mTransStatus.setLayoutParams(params);
    }

    /**
     * 设置是否需要渐变
     */
    public void setNeedTranslucent() {
        setNeedTranslucent(true, true);
    }

    /**
     * 设置是否需要渐变,并且隐藏标题
     *
     * @param translucent
     */
    public void setNeedTranslucent(boolean translucent, boolean hideContent) {
        if (translucent) {
            mTransRoot.setBackground(null);
        }
        if (hideContent) {
            mTransTitle.setVisibility(View.GONE);
            mTransLeft.setVisibility(View.GONE);
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置颜色背景
     *
     * @param resBackground
     */
    public void setColorBackground(int resBackground) {
        mTransRoot.setBackgroundColor(resBackground);
    }

    /**
     * 设置图片背景
     *
     * @param resBackground
     */
    public void setDrawalleBackground(Drawable resBackground) {
        mTransRoot.setBackground(resBackground);
    }

    /**
     * 设置标题文本
     *
     * @param strTitle
     */
    public void setTitle(String strTitle) {
        if (!TextUtils.isEmpty(strTitle)) {
            mTransTitle.setVisibility(View.VISIBLE);
            mTransTitle.setText(strTitle);
        } else {
            mTransTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边文本
     *
     * @param strLeft
     */
    public void setLeft(String strLeft) {
        if (!TextUtils.isEmpty(strLeft)) {
            mTransLeft.setVisibility(View.VISIBLE);
            mTransLeft.setText(strLeft);
        } else {
            mTransLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边图片
     *
     * @param resLeft
     */
    public void setLeftIcon(int resLeft) {
        Drawable icLeft = ContextCompat.getDrawable(getContext(), resLeft);
        icLeft.setBounds(0, 0, icLeft.getMinimumWidth(), icLeft.getMinimumHeight());
        mTransLeft.setCompoundDrawables(icLeft, null, null, null);
    }

    /**
     * 设置左边监听事件
     *
     * @param listener
     */
    public void setLeftClickListener(final ToolBarClickListener listener) {
        mTransLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLeftClick();
            }
        });
    }

    /**
     * 设置右边文本
     *
     * @param strRight
     */
    public void setRight(String strRight) {
        if (!TextUtils.isEmpty(strRight)) {
            mTransRight.setVisibility(View.VISIBLE);
            mTransRight.setText(strRight);
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边图片
     *
     * @param resRight
     */
    public void setRightIcon(int resRight) {
        if (resRight != 0) {
            mTransRight.setVisibility(View.VISIBLE);
            Drawable icRight = ContextCompat.getDrawable(getContext(), resRight);
            icRight.setBounds(0, 0, icRight.getMinimumWidth(), icRight.getMinimumHeight());
            mTransRight.setCompoundDrawables(null, null, icRight, null);
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边监听事件
     *
     * @param listener
     */
    public void setRightClickListener(final View.OnClickListener listener) {
        mTransRight.setOnClickListener(listener);
    }

    /**
     * 设置数据
     *
     * @param strTitle
     * @param resLeft
     * @param strLeft
     * @param resRight
     * @param strRight
     * @param listener
     */
    public void setData(String strTitle, int resLeft, String strLeft, int resRight, String strRight, final ToolBarClickListener listener) {
        if (!TextUtils.isEmpty(strTitle)) {
            mTransTitle.setVisibility(View.VISIBLE);
            mTransTitle.setText(strTitle);
        } else {
            mTransTitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(strLeft)) {
            mTransLeft.setVisibility(View.VISIBLE);
            mTransLeft.setText(strLeft);
        } else {
            mTransLeft.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(strRight)) {
            mTransRight.setVisibility(View.VISIBLE);
            mTransRight.setText(strRight);
        } else {
            mTransRight.setVisibility(View.GONE);
        }

        if (resLeft != 0) {
            mTransLeft.setVisibility(View.VISIBLE);
            Drawable icLeft = ContextCompat.getDrawable(getContext(), resLeft);
            icLeft.setBounds(0, 0, icLeft.getMinimumWidth(), icLeft.getMinimumHeight());
            mTransRight.setCompoundDrawables(icLeft, null, null, null);
        }

        if (resRight != 0) {
            mTransRight.setVisibility(View.VISIBLE);
            Drawable icRight = ContextCompat.getDrawable(getContext(), resRight);
            icRight.setBounds(0, 0, icRight.getMinimumWidth(), icRight.getMinimumHeight());
            mTransRight.setCompoundDrawables(null, null, icRight, null);
        }

        if (listener != null) {
            mTransLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            mTransRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }

}
