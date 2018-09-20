package com.wecent.weixun.ui.news.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.wecent.weixun.R;
import com.wecent.weixun.model.entity.News;
import com.wecent.weixun.utils.ImageLoaderUtil;
import com.wecent.weixun.utils.TimeUtils;

import java.util.List;

/**
 * desc: 新闻列表的适配器 .
 * author: wecent .
 * date: 2017/9/19 .
 */
public class WeiXunAdapter extends BaseMultiItemQuickAdapter<News, BaseViewHolder> {

    private Context mContext;
    private String mChannelCode;

    public WeiXunAdapter(List<News> data, String code, Context context) {
        super(data);
        this.mContext = context;
        this.mChannelCode = code;
        addItemType(News.TYPE_TEXT_NEWS, R.layout.item_news_text);
        addItemType(News.TYPE_CENTER_PIC_NEWS, R.layout.item_news_center_pic);
        addItemType(News.TYPE_RIGHT_PIC_NEWS, R.layout.item_news_right_pic);
        addItemType(News.TYPE_THREE_PIC_NEWS, R.layout.item_news_three_pic);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, News news) {
        if (TextUtils.isEmpty(news.title)) {
            //如果没有标题，则直接跳过
            return;
        }

        //设置标题、底部作者、评论数、发表时间
        baseViewHolder.setText(R.id.tv_title, news.title)
                .setText(R.id.tv_author, news.source)
                .setText(R.id.tv_comment_num, news.comment_count + "评论")
                .setText(R.id.tv_time, TimeUtils.getShortTime(news.behot_time * 1000))
                .setText(R.id.tv_author_none, news.source)
                .setText(R.id.tv_comment_num_none, news.comment_count + "评论")
                .setText(R.id.tv_time_none, TimeUtils.getShortTime(news.behot_time * 1000));

        //根据情况显示置顶、广告和热点的标签
        int position = baseViewHolder.getAdapterPosition();
        String[] channelCodes = mContext.getResources().getStringArray(R.array.weixun_channel_code);
        boolean isTop = position == 0 && mChannelCode.equals(channelCodes[0]); //属于置顶
        boolean isHot = news.hot == 1;//属于热点新闻
        boolean isAD = !TextUtils.isEmpty(news.tag) && news.tag.equals("ad");//属于广告新闻
        baseViewHolder.setVisible(R.id.ll_content, isTop || isHot || isAD);//如果是上面任意一个，显示标签
        baseViewHolder.setVisible(R.id.ll_content_none, !isTop && !isHot && !isAD);//如果是上面任意一个，显示标签
        baseViewHolder.setVisible(R.id.tv_comment_num, !isAD);//如果是广告，则隐藏评论数
        if (isTop) {
            baseViewHolder.setText(R.id.tv_tag, "置顶");
            baseViewHolder.setTextColor(R.id.tv_tag, mContext.getResources().getColor(R.color.config_color_f9696b));
        } else if (isHot) {
            baseViewHolder.setText(R.id.tv_tag, "热点");
            baseViewHolder.setTextColor(R.id.tv_tag, mContext.getResources().getColor(R.color.config_color_f9696b));
        } else if (isAD) {
            baseViewHolder.setText(R.id.tv_tag, "广告");
            baseViewHolder.setTextColor(R.id.tv_tag, mContext.getResources().getColor(R.color.config_color_3091d8));
        }
        switch (baseViewHolder.getItemViewType()) {
            case News.TYPE_TEXT_NEWS:
                break;
            case News.TYPE_CENTER_PIC_NEWS:
                //中间大图布局，判断是否有视频
                TextView tvBottomRight = baseViewHolder.getView(R.id.tv_bottom_right);
                if (news.has_video) {
                    baseViewHolder.setVisible(R.id.iv_play, true);//显示播放按钮
                    tvBottomRight.setCompoundDrawables(null, null, null, null);//去除TextView左侧图标
                    baseViewHolder.setText(R.id.tv_bottom_right, TimeUtils.secToTime(news.video_duration));//设置时长
                    //中间图片使用视频大图
                    ImageLoaderUtil.LoadImage(mContext, news.video_detail_info.detail_video_large_image.url, (ImageView) baseViewHolder.getView(R.id.iv_img));
                } else {
                    baseViewHolder.setVisible(R.id.iv_play, false);//隐藏播放按钮
                    if (news.gallary_image_count == 1){
                        tvBottomRight.setCompoundDrawables(null, null, null, null);//去除TextView左侧图标
                    }else{
                        tvBottomRight.setCompoundDrawables(mContext.getResources().getDrawable(R.drawable.ic_weixun_detail_picture), null, null, null);//TextView增加左侧图标
                        baseViewHolder.setText(R.id.tv_bottom_right, news.gallary_image_count + "图");//设置图片数
                    }
                    //中间图片使用image_list第一张
                    ImageLoaderUtil.LoadImage(mContext, news.image_list.get(0).url.replace("list/300x196", "large"), (ImageView) baseViewHolder.getView(R.id.iv_img));
                }
                break;
            case News.TYPE_RIGHT_PIC_NEWS:
                //右侧小图布局，判断是否有视频
                if (news.has_video) {
                    baseViewHolder.setVisible(R.id.ll_duration, true);//显示时长
                    baseViewHolder.setText(R.id.tv_duration, TimeUtils.secToTime(news.video_duration));//设置时长
                } else {
                    baseViewHolder.setVisible(R.id.ll_duration, false);//隐藏时长
                }
                ImageLoaderUtil.LoadImage(mContext,news.middle_image.url, (ImageView) baseViewHolder.getView(R.id.iv_img));
                break;
            case News.TYPE_THREE_PIC_NEWS:
                //三张图片的新闻
                ImageLoaderUtil.LoadImage(mContext, news.image_list.get(0).url, (ImageView) baseViewHolder.getView(R.id.iv_img1));
                ImageLoaderUtil.LoadImage(mContext, news.image_list.get(1).url, (ImageView) baseViewHolder.getView(R.id.iv_img2));
                ImageLoaderUtil.LoadImage(mContext, news.image_list.get(2).url, (ImageView) baseViewHolder.getView(R.id.iv_img3));
                break;
        }
    }
}
