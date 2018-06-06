package com.yuliu.base;

import android.app.Application;

import com.xdandroid.hellodaemon.DaemonEnv;


public class App extends Application {
    public static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //需要在 Application 的 onCreate() 中调用一次 DaemonEnv.initialize()
        DaemonEnv.initialize(this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        TraceServiceImpl.sShouldStopService = false;
        DaemonEnv.startServiceMayBind(TraceServiceImpl.class);
    }
}
