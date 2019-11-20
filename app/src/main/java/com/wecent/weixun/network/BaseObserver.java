package com.wecent.weixun.network;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * desc:
 * author: wecent
 * date: 2018/9/2
 */
public abstract class BaseObserver<T> implements Observer<T> {

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable e);

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }
}
