package com.yuliu.base.http;

import android.content.Context;
import android.widget.Toast;

import com.yuliu.base.MMLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import utils.ToastUtils;

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private Context mContext;
    protected BaseObserver(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseEntity<T> value) {
        if (value.isSuccess()) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        MMLog.v("onError:" + e.toString());
    }

    @Override
    public void onComplete() {
        MMLog.v("onComplete");
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
