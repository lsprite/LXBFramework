package com.example.fragmentdemo;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
    // 避免http://blog.csdn.net/love100628/article/details/43238135提到的错误,在启动的activity里设置
    private boolean isStartApp = true;

    //
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MyApplication.onCreate()");
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public boolean isStartApp() {
        return isStartApp;
    }

    public void setStartApp(boolean isStartApp) {
        this.isStartApp = isStartApp;
    }

}