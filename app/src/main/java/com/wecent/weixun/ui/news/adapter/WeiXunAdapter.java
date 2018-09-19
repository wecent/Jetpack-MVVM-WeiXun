package com.wecent.weixun.ui.news.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecent.weixun.R;
import com.wecent.weixun.model.entity.News;

import java.util.List;

/**
 * desc: 新闻列表的适配器 .
 * author: wecent .
 * date: 2017/9/19 .
 */
public class WeiXunAdapter extends BaseMultiItemQuickAdapter<News, BaseViewHolder> {

    private Context mContext;

    public WeiXunAdapter(List<News> data, Context context) {
        super(data);
        this.mContext = context;
        addItemType(News.TYPE_TEXT_NEWS, R.layout.item_news_text);
        addItemType(News.TYPE_CENTER_PIC_NEWS, R.layout.item_news_center_pic);
        addItemType(News.TYPE_RIGHT_PIC_NEWS, R.layout.item_news_right_pic);
        addItemType(News.TYPE_THREE_PIC_NEWS, R.layout.item_news_three_pic);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, News item) {
        switch (baseViewHolder.getItemViewType()) {
            case News.TYPE_TEXT_NEWS:
                baseViewHolder.setText(R.id.tv_title, item.title);
                break;
            case News.TYPE_CENTER_PIC_NEWS:
                baseViewHolder.setText(R.id.tv_title, item.title);
                break;
            case News.TYPE_RIGHT_PIC_NEWS:
                baseViewHolder.setText(R.id.tv_title, item.title);
                break;
            case News.TYPE_THREE_PIC_NEWS:
                baseViewHolder.setText(R.id.tv_title, item.title);
                break;
        }
    }
}
