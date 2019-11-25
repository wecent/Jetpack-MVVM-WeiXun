package com.wecent.weixun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.wecent.weixun.R;
import com.wecent.weixun.utils.SizeUtils;

/**
 * desc: 轻量级的RecyclerView，可以设置分割线
 * author: wecent
 * date: 2018/9/7
 */
public class SimpleRecyclerView extends RecyclerView {

    private Context mContext;

    //分割线的颜色
    private int mDividerColor;
    //分割线的大小
    private int mDividerSize = 1;
    //分割线的drawable
    private Drawable mDividerDrawable;
    //是否使用瀑布流布局,默认不是
    private boolean mUseStaggerLayout;
    //列数，默认为1
    private int mNumColumns = 1;
    //RecyclerView的方向，默认为垂直方向
    private int mOrientation = LinearLayoutManager.VERTICAL;

    private int mMarginLeft;
    private int mMarginRight;

    private LayoutManager mLayoutManager;
    private DividerDecoration mDividerDecoration;
    private Drawable mItemDrawable;


    public SimpleRecyclerView(Context context) {
        this(context,null);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleRecyclerView);

        mDividerColor = ta.getColor(R.styleable.SimpleRecyclerView_dividerColor, Color.parseColor("#ffd8d8d8"));
        mDividerSize = ta.getDimensionPixelSize(R.styleable.SimpleRecyclerView_dividerSize, SizeUtils.dp2px(1));

        mDividerDrawable = ta.getDrawable(R.styleable.SimpleRecyclerView_dividerDrawable);

        mUseStaggerLayout = ta.getBoolean(R.styleable.SimpleRecyclerView_useStaggerLayout, mUseStaggerLayout);
        mNumColumns = ta.getInt(R.styleable.SimpleRecyclerView_numColumns,mNumColumns);

        mOrientation = ta.getInt(R.styleable.SimpleRecyclerView_rvOrientation, mOrientation);

        mMarginLeft = ta.getDimensionPixelSize(R.styleable.SimpleRecyclerView_dividerMarginLeft, 0);
        mMarginRight = ta.getDimensionPixelSize(R.styleable.SimpleRecyclerView_dividerMarginRight, 0);

        ta.recycle();

        setLayoutManager();
        setDivider();
    }

    /**
     * 设置layoutManager
     */
    private void setLayoutManager() {
        if (!mUseStaggerLayout){
            //不是瀑布流布局，即是列表或网格布局
            if (mOrientation == LinearLayoutManager.VERTICAL){
                mLayoutManager =  new GridLayoutManager(mContext, mNumColumns);
            }else{
                mLayoutManager = new GridLayoutManager(mContext, mNumColumns,mOrientation,false);
            }
        }else{
            //瀑布流布局
            mLayoutManager = new StaggeredGridLayoutManager(mNumColumns,mOrientation);
        }

       setLayoutManager(mLayoutManager);
    }

    /**
     * 设置分割线
     */
    private void setDivider() {
        if (mDividerDrawable == null){
            //绘制颜色分割线
            mDividerDecoration = new DividerDecoration(mContext, mOrientation,mDividerColor, mDividerSize,mMarginLeft,mMarginRight);
        }else{
            //绘制图片分割线
            mDividerDecoration = new DividerDecoration(mContext,mOrientation,mDividerDrawable,mDividerSize,mMarginLeft,mMarginRight);
        }
        this.addItemDecoration(mDividerDecoration);
    }
}
