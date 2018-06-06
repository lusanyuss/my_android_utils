package com.yuliu.base.http;

import com.yuliu.base.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    private static final long   TIMEOUT          = 30;
    public static final  String BASE_URL1        = "https://testapi.uhouzz.com";
    public static final  String BASE_URL2        = "https://scanner.tradingview.com";
    public static final  String BASE_URL_NAME1   = "url1";//ApiService的header中使用  例如:{"urlname:url1"}
    public static final  String BASE_URL_NAME2   = "url2";//ApiService的header中使用  例如:{"urlname:url1"}
    public static final  String BASE_URL_DEFAULT = BASE_URL1; //默认


    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            //多baseUrl拦截器
            .addInterceptor(InterceptorUtil.MoreBaseUrlInterceptor())
            //添加header拦截器方便调试接口
            .addInterceptor(InterceptorUtil.HeaderInterceptor())
            //添加HttpLoggingInterceptor拦截器方便调试接口
            .addInterceptor(InterceptorUtil.LogInterceptor())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            //缓存
            .cache(new Cache(new File(App.mApp.getApplicationContext().getCacheDir(), "cache"), 1024 * 1024 * 100))
            .build();


    private static ApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL_DEFAULT)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create())
            // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(ApiService.class);

    public static ApiService getInstance() {
        return apiService;
    }

}
