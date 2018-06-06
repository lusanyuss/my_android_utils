package com.yuliu.base.http;


import java.util.Map;

import io.reactivex.Observable;
import model.User;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * For Retrofit
 * Created by jaycee on 2017/6/23.
 */
public interface ApiService {



    // TODO: 2018/6/6 post请求
    @Headers({"urlname:url1"})
    @FormUrlEncoded
    @POST("account/login")
    Observable<BaseEntity<User>> rqPostUser(@FieldMap Map<String, Object> map);

    // TODO: 2018/6/6 get请求
    @GET("video/getUrl")
    Observable<BaseEntity<User>> rqGetUser(@QueryMap Map<String, Object> map);


}
