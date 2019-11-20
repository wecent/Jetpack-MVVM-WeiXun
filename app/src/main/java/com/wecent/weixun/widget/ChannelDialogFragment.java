package com.wecent.weixun.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.wecent.weixun.R;
import com.wecent.weixun.model.entity.Channel;
import com.wecent.weixun.model.event.NewChannelEvent;
import com.wecent.weixun.model.event.SelectChannelEvent;
import com.wecent.weixun.ui.news.adapter.ChannelAdapter;
import com.wecent.weixun.ui.inter.ItemDragHelperCallBack;
import com.wecent.weixun.ui.inter.OnChannelListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * desc: 新闻分类弹框
 * author: wecent
 * date: 2018/9/7
 */
public class ChannelDialogFragment extends DialogFragment implements OnChannelListener {

    private RecyclerView mRecyclerView;
    private ImageView mClose;
    private boolean isUpdate = false;
    private ChannelAdapter mAdapter;
    private List<Channel> mData = new ArrayList<>();
    private List<Channel> mSelectedData;
    private List<Channel> mUnSelectedData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);

//        ImmersionBar.with(this)
//                .transparentStatusBar()
//                .fitsSystemWindows(true)
//                .statusBarColor(R.color.config_color_white)
//                .statusBarDarkFont(true)
//                .navigationBarColor(R.color.config_color_base_5)
//                .keyboardEnable(true)
//                .init();
    }

    private OnChannelListener onChannelListener;

    public void setOnChannelListener(OnChannelListener onChannelListener) {
        this.onChannelListener = onChannelListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            //添加动画
            dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        }
        return inflater.inflate(R.layout.layout_dialog_channel, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_channel);
        mClose = (ImageView) view.findViewById(R.id.iv_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        processLogic();
    }

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas) {
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataSelected", (Serializable) selectedDatas);
        bundle.putSerializable("dataUnselected", (Serializable) unselectedDatas);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    private void setDataType(List<Channel> datas, int type) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemtype(type);
        }
    }

    private void processLogic() {
        Channel channel = new Channel();
        channel.setItemtype(Channel.TYPE_MY);
        channel.setChannelName("我的频道");
        mData.add(channel);

        Bundle bundle = getArguments();
        mSelectedData = (List<Channel>) bundle.getSerializable("dataSelected");
        mUnSelectedData = (List<Channel>) bundle.getSerializable("dataUnselected");
        setDataType(mSelectedData, Channel.TYPE_MY_CHANNEL);
        setDataType(mUnSelectedData, Channel.TYPE_OTHER_CHANNEL);
        mData.addAll(mSelectedData);

        Channel morechannel = new Channel();
        morechannel.setItemtype(Channel.TYPE_OTHER);
        morechannel.setChannelName("频道推荐");
        mData.add(morechannel);

        mData.addAll(mUnSelectedData);

        ItemDragHelperCallBack callback = new ItemDragHelperCallBack(this);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mAdapter = new ChannelAdapter(mData, helper);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == Channel.TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL ? 1 : 4;
            }
        });
//        mHelper = new ItemTouchHelper(callBack);
        mAdapter.OnChannelListener(this);
//        //attachRecyclerView
//        mHelper.attachToRecyclerView(mRecyclerView);
    }


    @OnClick(R.id.iv_close)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
        if (starPos < 0 || endPos < 0) return;
        if (mData.get(endPos).getChannelName().equals("头条")) return;
        //我的频道之间移动
        if (onChannelListener != null)
            onChannelListener.onItemMove(starPos - 1, endPos - 1);//去除标题所占的一个index
        onMove(starPos, endPos, false);
    }

    private String firstAddChannelName = "";


    private void onMove(int starPos, int endPos, boolean isAdd) {
        isUpdate = true;
        Channel startChannel = mData.get(starPos);
        //先删除之前的位置
        mData.remove(starPos);
        //添加到现在的位置
        mData.add(endPos, startChannel);
        mAdapter.notifyItemMoved(starPos, endPos);
        if (isAdd) {
            if (TextUtils.isEmpty(firstAddChannelName)) {
                firstAddChannelName = startChannel.getChannelName();
            }
        } else {
            if (startChannel.getChannelName().equals(firstAddChannelName)) {
                firstAddChannelName = "";
            }
        }
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        onMove(starPos, endPos, true);
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        onMove(starPos, endPos, false);
    }

    @Override
    public void onFinish(String selectedChannelName) {
        EventBus.getDefault().post(new SelectChannelEvent(selectedChannelName));
        dismiss();
    }

    @Override
    public void onPause() {
        if (isUpdate) {
            EventBus.getDefault().post(new NewChannelEvent(mAdapter.getData(), firstAddChannelName));
        }
        super.onPause();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
