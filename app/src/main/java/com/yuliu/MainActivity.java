package com.yuliu;

import android.os.Bundle;

import com.yuliu.base.BaseActivity;
import com.yuliu.base.http.RetrofitApi;
import com.yuliu.base.results.InfoResult;
import com.yuliu.base.results.TokenResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoudId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        Map<String, Object> map = new HashMap<>();
        map.put("src", "android");
        map.put("sourceApp", "1");
        Observable<TokenResult> observable = RetrofitApi.getInstance().rqPostUser(map);
        observable.compose(compose(this.bindToLifecycle())).subscribe(mResult -> {

        }, throwable -> {

        });


        Map<String, Object> map1 = new HashMap<>();
        Observable<InfoResult> observable1 = RetrofitApi.getInstance().rqGetUser(map1);
        observable1.compose(compose(this.bindToLifecycle())).subscribe(mResult -> {

        }, throwable -> {

        });

    }

}
