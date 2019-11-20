package com.wecent.weixun.ui.belle.presenter;

import android.util.Log;

import com.wecent.weixun.model.entity.BelleEntity;
import com.wecent.weixun.network.BaseObserver;
import com.wecent.weixun.network.JanDanApiManager;
import com.wecent.weixun.network.RxSchedulers;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.belle.contract.BelleContract;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * desc: .
 * author: wecent .
 * date: 2018/9/27 .
 */
public class BellePresenter extends BasePresenter<BelleContract.View> implements BelleContract.Presenter {
    private static final String TAG = "BellePresenter";
    JanDanApiManager mJanDanApi;

    @Inject
    public BellePresenter(JanDanApiManager janDanApi) {
        this.mJanDanApi = janDanApi;
    }

    @Override
    public void getDetailData(final String type, final int page) {
        mJanDanApi.getJdDetails(type, page)
                .compose(RxSchedulers.<BelleEntity>applySchedulers())
                .compose(mView.<BelleEntity>bindToLife())
                .map(new Function<BelleEntity, BelleEntity>() {
                    @Override
                    public BelleEntity apply(@NonNull BelleEntity belleEntity) throws Exception {
                        for (BelleEntity.CommentsBean bean : belleEntity.getComments()) {
                            if (bean.getPics() != null) {
                                if (bean.getPics().size() > 1) {
                                    bean.itemType = BelleEntity.CommentsBean.TYPE_MULTIPLE;
                                } else {
                                    bean.itemType = BelleEntity.CommentsBean.TYPE_SINGLE;
                                }
                            }
                        }
                        return belleEntity;
                    }
                })
                .subscribe(new BaseObserver<BelleEntity>() {
                    @Override
                    public void onSuccess(BelleEntity belleEntity) {
                        if (page > 1) {
                            mView.loadMoreDetailData(type, belleEntity);
                        } else {
                            mView.loadDetailData(type, belleEntity);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        if (page > 1) {
                            mView.loadMoreDetailData(type, null);
                        } else {
                            mView.loadDetailData(type, null);
                        }
                        Log.i(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }
}
