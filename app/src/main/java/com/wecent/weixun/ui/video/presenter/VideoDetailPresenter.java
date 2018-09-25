package com.wecent.weixun.ui.video.presenter;

import com.socks.library.KLog;
import com.wecent.weixun.model.entity.NewsDetail;
import com.wecent.weixun.model.response.CommentResponse;
import com.wecent.weixun.model.response.ResultResponse;
import com.wecent.weixun.network.BaseObserver;
import com.wecent.weixun.network.RxSchedulers;
import com.wecent.weixun.network.WeiXunApi;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.video.contract.VideoDetailContract;

import javax.inject.Inject;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/19 .
 */
public class VideoDetailPresenter extends BasePresenter<VideoDetailContract.View> implements VideoDetailContract.Presenter {

    WeiXunApi mWeiXunApi;

    @Inject
    public VideoDetailPresenter(WeiXunApi mWeiXunApi) {
        this.mWeiXunApi = mWeiXunApi;
    }

    @Override
    public void getNewsData(String url) {
        mWeiXunApi.getNewsDetail(url)
                .compose(RxSchedulers.<ResultResponse<NewsDetail>>applySchedulers())
                .compose(mView.<ResultResponse<NewsDetail>>bindToLife())
                .subscribe(new BaseObserver<ResultResponse<NewsDetail>>() {
                    @Override
                    public void onSuccess(ResultResponse<NewsDetail> response) {
                        KLog.e(response);
                        mView.loadNewsData(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        KLog.e(e.getLocalizedMessage());
                        mView.loadNewsData(null);
                    }
                });
    }

    @Override
    public void getConmentData(String groupId, String itemId, int pageNow) {
        int offset = (pageNow - 1) * 20;
        mWeiXunApi.getCommentList(groupId, itemId,String.valueOf(offset), "20")
                .compose(RxSchedulers.<CommentResponse>applySchedulers())
                .compose(mView.<CommentResponse>bindToLife())
                .subscribe(new BaseObserver<CommentResponse>() {
                    @Override
                    public void onSuccess(CommentResponse response) {
                        KLog.e(response);
                        mView.loadConmentData(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        KLog.e(e.getLocalizedMessage());
                        mView.loadConmentData(null);
                    }
                });
    }
}
