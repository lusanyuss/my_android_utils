package com.yuliu.base.http;

import android.text.TextUtils;

import com.yuliu.BuildConfig;
import com.yuliu.base.App;
import com.yuliu.base.MMLog;

import java.io.IOException;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import utils.Utils;

public class InterceptorUtil {
    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                MMLog.v(message);
            }
        }).setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.HEADERS :
                HttpLoggingInterceptor.Level.NONE);//设置打印数据的级别
    }

    //header拦截器
    public static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                // TODO: 2018/6/6   添加通用的Header
                //                    builder.addHeader("token", "123");
                return chain.proceed(builder.build());
            }
        };

    }

    //多baseUrl拦截器
    public static Interceptor MoreBaseUrlInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //获取原始的originalRequest
                Request originalRequest = chain.request();
                //获取老的url
                HttpUrl oldUrl = originalRequest.url();
                //获取originalRequest的创建者builder
                Request.Builder builder = originalRequest.newBuilder();
                //获取头信息的集合如：manage,mdffx
                List<String> urlnameList = originalRequest.headers("urlname");
                if (urlnameList != null && urlnameList.size() > 0) {
                    //删除原有配置中的值,就是namesAndValues集合里的值
                    builder.removeHeader("urlname");
                    //获取头信息中配置的value,如：manage或者mdffx
                    String urlname = urlnameList.get(0);
                    HttpUrl baseURL = null;
                    //根据头信息中配置的value,来匹配新的base_url地址
                    if (RetrofitApi.BASE_URL_NAME1.equals(urlname)) {
                        baseURL = HttpUrl.parse(RetrofitApi.BASE_URL1);

                    } else if (RetrofitApi.BASE_URL_NAME2.equals(urlname)) {
                        baseURL = HttpUrl.parse(RetrofitApi.BASE_URL2);
                    }
                    //重建新的HttpUrl，需要重新设置的url部分
                    HttpUrl newHttpUrl = oldUrl.newBuilder()
                            .scheme(baseURL.scheme())//http协议如：http或者https
                            .host(baseURL.host())//主机地址
                            .port(baseURL.port())//端口
                            .build();
                    //获取处理后的新newRequest
                    Request newRequest = builder.url(newHttpUrl).build();
                    return chain.proceed(newRequest);
                } else {
                    return chain.proceed(originalRequest);
                }
            }
        };

    }


    //header拦截器
    //有网和没有网都是先读缓存
    public static Interceptor HttpCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                //                maxStale ：设置最大失效时间，失效则不使用
                //                minFresh ：设置最小有效时间，失效则不使用
                //                FORCE_NETWORK ：强制走网络
                //                FORCE_CACHE ：强制走缓存

                Request request = chain.request();
                String cacheControl = request.cacheControl().toString();

                if (!Utils.isNetworkAvailable(App.mApp.getApplicationContext())) {
                    // TODO: 2018/6/7 无网强制走缓存
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);


                if (Utils.isNetworkAvailable(App.mApp.getApplicationContext())) {
                    // TODO: 2018/6/7 有网络的情况下设置max-age=60 x 60，即1分钟
                    if (!TextUtils.isEmpty(cacheControl)) {
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", cacheControl)
                                .build();
                    } else {
                        return response.newBuilder()
                                .build();
                    }
                } else {
                    // TODO: 2018/6/7 没有网络的情况下设置max-stale=60 x 60 x 24 x 28，即4周。
                    int maxStale = 60 * 60 * 24 * 28; //  4-weeks stale
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }

            }
        };

    }


}
