package com.wecent.weixun.widget.trans;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wecent.weixun.R;
import com.wecent.weixun.utils.SizeUtils;

/**
 * desc: 一个简单的标题栏
 * author: wecent
 * date: 2018/9/2
 */
public final class TransToolBar extends LinearLayout {

    private RelativeLayout mTransRoot;
    private View mTransBottom;
    public TextView mTransTitle;
    public TextView mTransLeft;
    public TextView mTransRight;

    public TransToolBar(Context context) {
        this(context, null);
    }

    public TransToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTransContent();
    }

    public TransToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initTransContent() {
        setOrientation(HORIZONTAL);
        View contentView = inflate(getContext(), R.layout.layout_toolbar_trans, this);
        mTransRoot = (RelativeLayout) contentView.findViewById(R.id.rl_trans_root);
        mTransBottom = contentView.findViewById(R.id.v_trans_bottom);
        mTransBottom.setVisibility(View.GONE);
        mTransTitle = (TextView) contentView.findViewById(R.id.tv_trans_title);
        mTransTitle.setVisibility(View.GONE);
        mTransLeft = (TextView) contentView.findViewById(R.id.tv_trans_left);
        mTransLeft.setVisibility(View.GONE);
        mTransRight = (TextView) contentView.findViewById(R.id.tv_trans_right);
        mTransRight.setVisibility(View.GONE);
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
        mTransRoot.setBackgroundColor(getContext().getResources().getColor(resBackground));
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
    public void setTitleText(String strTitle) {
        if (!TextUtils.isEmpty(strTitle)) {
            mTransTitle.setVisibility(View.VISIBLE);
            mTransTitle.setText(strTitle);
        } else {
            mTransTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color) {
        mTransTitle.setTextColor(getContext().getResources().getColor(color));
    }

    /**
     * 设置左边
     *
     * @param strLeft
     */
    public void setLeft(String strLeft, int color, final OnClickListener listener) {
        if (!TextUtils.isEmpty(strLeft)) {
            mTransLeft.setVisibility(View.VISIBLE);
            mTransLeft.setText(strLeft);
            mTransLeft.setTextColor(getContext().getResources().getColor(color));
            mTransLeft.setOnClickListener(listener);
        } else {
            mTransLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边
     *
     * @param resLeft
     * @param listener
     */
    public void setLeft(int resLeft, final OnClickListener listener) {
        if (resLeft != 0) {
            mTransLeft.setVisibility(View.VISIBLE);
            Drawable icLeft = ContextCompat.getDrawable(getContext(), resLeft);
            icLeft.setBounds(0, 0, icLeft.getMinimumWidth(), icLeft.getMinimumHeight());
            mTransLeft.setCompoundDrawables(icLeft, null, null, null);
            mTransLeft.setOnClickListener(listener);
        } else {
            mTransLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边文本
     *
     * @param strLeft
     */
    public void setLeftText(String strLeft) {
        if (!TextUtils.isEmpty(strLeft)) {
            mTransLeft.setVisibility(View.VISIBLE);
            mTransLeft.setText(strLeft);
        } else {
            mTransLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setLeftTextColor(int color) {
        mTransLeft.setTextColor(getContext().getResources().getColor(color));
    }

    /**
     * 设置左边图片
     *
     * @param resLeft
     */
    public void setLeftIcon(int resLeft) {
        if (resLeft != 0) {
            mTransLeft.setVisibility(View.VISIBLE);
            Drawable icLeft = ContextCompat.getDrawable(getContext(), resLeft);
            icLeft.setBounds(0, 0, icLeft.getMinimumWidth(), icLeft.getMinimumHeight());
            mTransLeft.setCompoundDrawables(icLeft, null, null, null);
        } else {
            mTransLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边监听事件
     *
     * @param listener
     */
    public void setOnLeftClickListener(final OnClickListener listener) {
        mTransLeft.setOnClickListener(listener);
    }

    /**
     * 设置右边
     *
     * @param strRight
     * @param color
     * @param listener
     */
    public void setRight(String strRight, int color, final OnClickListener listener) {
        if (!TextUtils.isEmpty(strRight)) {
            mTransRight.setVisibility(View.VISIBLE);
            mTransRight.setText(strRight);
            mTransRight.setTextColor(getContext().getResources().getColor(color));
            mTransRight.setOnClickListener(listener);
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边
     *
     * @param strRight
     * @param color
     * @param resRight
     * @param listener
     */
    public void setRight(String strRight, int color, int resRight, final OnClickListener listener) {
        if (!TextUtils.isEmpty(strRight)) {
            mTransRight.setVisibility(View.VISIBLE);
            mTransRight.setText(strRight);
            mTransRight.setTextColor(getContext().getResources().getColor(color));
            mTransRight.setBackground(getContext().getResources().getDrawable(resRight));
            mTransRight.setOnClickListener(listener);
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边
     *
     * @param resRight
     * @param listener
     */
    public void setRight(int resRight, final OnClickListener listener) {
        if (resRight != 0) {
            mTransRight.setVisibility(View.VISIBLE);
            Drawable icRight = ContextCompat.getDrawable(getContext(), resRight);
            icRight.setBounds(0, 0, icRight.getMinimumWidth(), icRight.getMinimumHeight());
            mTransRight.setCompoundDrawables(null, null, icRight, null);
            mTransRight.setOnClickListener(listener);
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边文本
     *
     * @param strRight
     */
    public void setRightText(String strRight) {
        if (!TextUtils.isEmpty(strRight)) {
            mTransRight.setVisibility(View.VISIBLE);
            mTransRight.setText(strRight);
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边字体的颜色
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        mTransRight.setTextColor(getContext().getResources().getColor(color));
    }

    /**
     * 设置右边字体的图片背景
     *
     * @param resRight
     */
    public void setRightBackground(int resRight) {
        if (resRight != 0) {
            mTransRight.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, SizeUtils.dp2px(16), 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            mTransRight.setLayoutParams(params);
            mTransRight.setBackground(getContext().getResources().getDrawable(resRight));
        } else {
            mTransRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边字体是否选择
     *
     * @param selected
     */
    public void setRightSelected(boolean selected) {
        mTransRight.setSelected(selected);
    }

    /**
     * 获取右边字体是否选择
     */
    public boolean getRightSelected() {
        return mTransRight.isSelected();
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
    public void setOnRightClickListener(final OnClickListener listener) {
        mTransRight.setOnClickListener(listener);
    }

    /**
     * 设置左右边监听事件
     *
     * @param listener
     */
    public void setOnTransClickListener(final OnTransClickListener listener) {
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

    /**
     * 设置主页文本
     *
     * @param isShow
     */
    public void showBottomLine(Boolean isShow) {
        if (isShow) {
            mTransBottom.setVisibility(View.VISIBLE);
        } else {
            mTransBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 设置主页文本颜色
     *
     * @param color
     */
    public void setBottomLineColor(int color) {
        mTransBottom.setBackgroundColor(getContext().getResources().getColor(color));
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
    public void setAllData(String strTitle, int resLeft, String strLeft, int resRight, String strRight, final OnTransClickListener listener) {
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
            mTransLeft.setCompoundDrawables(icLeft, null, null, null);
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
