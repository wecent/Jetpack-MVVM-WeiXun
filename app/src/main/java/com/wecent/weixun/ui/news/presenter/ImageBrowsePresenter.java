package com.wecent.weixun.ui.news.presenter;

import com.wecent.weixun.bean.NewsArticleBean;
import com.wecent.weixun.network.NewsApi;
import com.wecent.weixun.network.RxSchedulers;
import com.wecent.weixun.ui.base.BasePresenter;
import com.wecent.weixun.ui.news.contract.ImageBrowseContract;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/24 .
 */
public class ImageBrowsePresenter extends BasePresenter<ImageBrowseContract.View> implements ImageBrowseContract.Presenter {

    NewsApi mNewsApi;

    @Inject
    public ImageBrowsePresenter(NewsApi newsApi) {
        this.mNewsApi = newsApi;
    }

    @Override
    public void getData(String aid,boolean isCmpp) {
        mNewsApi.getNewsArticle(aid)
                .compose(RxSchedulers.<NewsArticleBean>applySchedulers())
                .compose(mView.<NewsArticleBean>bindToLife())
                .subscribe(new Observer<NewsArticleBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull NewsArticleBean articleBean) {
                        mView.loadData(articleBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showFaild();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
