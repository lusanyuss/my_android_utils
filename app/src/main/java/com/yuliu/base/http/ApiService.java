package com.yuliu.base.http;


import com.yuliu.base.results.InfoResult;
import com.yuliu.base.results.TokenResult;

import java.util.Map;

import io.reactivex.Observable;
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

    //    https://testapi.uhouzz.com/uhouzz5.9.2/index.php/region/configuration?src=android&sourceApp=1

    // TODO: 2018/6/6 post请求
    @Headers({"urlname:url1"})
    @FormUrlEncoded
    @POST("uhouzz5.9.2/index.php/region/configuration")
    Observable<TokenResult> rqPostUser(@FieldMap Map<String, Object> map);

    // TODO: 2018/6/6 get请求
    @Headers({"urlname:url2"})
    @GET("crypto/scan")
    Observable<InfoResult> rqGetUser(@QueryMap Map<String, Object> map);


}
