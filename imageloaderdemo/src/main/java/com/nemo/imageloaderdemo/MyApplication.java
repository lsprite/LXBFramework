package com.nemo.imageloaderdemo;

import android.app.Application;
import android.content.Context;

import com.lxb.util.MySQLiteHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Administrator on 2016/3/4.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private MySQLiteHelper dbHelper;

    //
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MyApplication.onCreate");
        initImageLoader(getApplicationContext());
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    // ====================

    public MySQLiteHelper getDbHelper(Context context) {
        dbHelper = new MySQLiteHelper(context, "history", null,
                MySQLiteHelper.VERSION);
        return dbHelper;
    }


    // 使用imageloader加載圖片
    public static void initImageLoader(Context context) {
        System.out.println("=========初始化ImageLoader");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 3)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
