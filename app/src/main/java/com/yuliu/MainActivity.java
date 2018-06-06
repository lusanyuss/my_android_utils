package com.yuliu;

import android.os.Bundle;

import com.yuliu.base.BaseActivity;
import com.yuliu.base.http.BaseEntity;
import com.yuliu.base.http.BaseObserver;
import com.yuliu.base.http.RetrofitApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import model.User;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public int getLayoudId() {
        return 0;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }


    public void login(String userId, String password) {

        Map<String, Object> map = new HashMap<>();
        Observable<BaseEntity<User>> observable = RetrofitApi.getInstance().rqPostUser(map);
        observable.compose(compose(this.<BaseEntity<User>>bindToLifecycle())).subscribe(new BaseObserver<User>(this) {
            @Override
            protected void onHandleSuccess(User user) {
                // 保存用户信息等操作
            }
        });
    }
}
