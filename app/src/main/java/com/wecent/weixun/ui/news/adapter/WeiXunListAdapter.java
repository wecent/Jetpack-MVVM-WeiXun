package com.wecent.weixun.ui.news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecent.weixun.R;
import com.wecent.weixun.model.NewsDetail;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.utils.ImageLoaderUtil;


import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻列表的适配器
 * @date 2018/3/22  11
 */

public class WeiXunListAdapter extends BaseMultiItemQuickAdapter<News, BaseViewHolder> {

    private Context mContext;

    public WeiXunListAdapter(List<News> data, Context context) {
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
