package com.wecent.weixun.ui.belle.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecent.weixun.R;
import com.wecent.weixun.loader.ImageLoader;
import com.wecent.weixun.model.entity.BelleEntity;
import com.wecent.weixun.ui.belle.BelleImageActivity;
import com.wecent.weixun.utils.TimeUtils;

import java.util.List;

/**
 * desc:
 * author: wecent
 * date: 2018/9/27
 */
public class BellePicAdapter extends BaseMultiItemQuickAdapter<BelleEntity.CommentsBean, BaseViewHolder> {

    private Activity mContext;

    public BellePicAdapter(Activity context, @Nullable List<BelleEntity.CommentsBean> data) {
        super(data);
        addItemType(BelleEntity.CommentsBean.TYPE_MULTIPLE, R.layout.item_meizi_picture);
        addItemType(BelleEntity.CommentsBean.TYPE_SINGLE, R.layout.item_meizi_picture);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final BelleEntity.CommentsBean commentsBean) {
        viewHolder.setText(R.id.tv_meizi_author, commentsBean.getComment_author());
        viewHolder.setText(R.id.tv_meizi_time, TimeUtils.millis2String(TimeUtils.string2Millis(commentsBean.getComment_date(), "yyyy-MM-dd HH:mm:ss")));
        viewHolder.setText(R.id.tv_meizi_like, commentsBean.getVote_negative() + "赞");
        switch (viewHolder.getItemViewType()) {
            case BelleEntity.CommentsBean.TYPE_MULTIPLE:
//                ImageView multipleImage = viewHolder.getView(R.id.iv_meizi_picture);
//                ImageLoader.getInstance().displayImage(mContext, commentsBean.getPics().get(0), multipleImage);
//                multipleImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String[] imageUrls = new String[commentsBean.getPics().size()];
//                        imageUrls[0] = commentsBean.getPics().get(0);
//                        BelleImageActivity.launch(mContext, imageUrls, 0);
//                    }
//                });
//                break;
            case BelleEntity.CommentsBean.TYPE_SINGLE:
                ImageView singleImage = viewHolder.getView(R.id.iv_meizi_picture);
                ImageLoader.getInstance().displayImage(mContext, commentsBean.getPics().get(0), singleImage);
                singleImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] imageUrls = new String[commentsBean.getPics().size()];
                        imageUrls[0] = commentsBean.getPics().get(0);
                        BelleImageActivity.launch(mContext, imageUrls, 0);
                    }
                });
                break;
        }

    }

}
