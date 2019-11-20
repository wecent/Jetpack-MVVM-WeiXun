package com.wecent.weixun.ui.video.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecent.weixun.R;
import com.wecent.weixun.loader.ImageLoader;
import com.wecent.weixun.model.entity.CommentData;
import com.wecent.weixun.utils.TimeUtils;

import java.util.List;

/**
 * desc: 新闻详情页评论的适配器 .
 * author: wecent .
 * date: 2018/9/19 .
 */
public class CommentAdapter extends BaseQuickAdapter<CommentData, BaseViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<CommentData> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentData commentData) {
        ImageLoader.getInstance().displayImage(mContext, commentData.comment.user_profile_image_url, (ImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, commentData.comment.user_name)
                .setText(R.id.tv_like_count, String.valueOf(commentData.comment.digg_count))
                .setText(R.id.tv_content, commentData.comment.text)
                .setText(R.id.tv_time, TimeUtils.getFriendlyTimeSpanByNow(commentData.comment.create_time * 1000));
    }
}
