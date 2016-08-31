package com.nemo.imageloaderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.lxb.util.UILUtils;

/**
 * Created by Administrator on 2016/3/2.
 */
public class MainActivity extends Activity {
    private ImageView img, img2, img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().getDbHelper(this).insertCache("a", "sss");
        img = (ImageView) findViewById(R.id.img);
//        UILUtils.displayLocalImage("/storage/sdcard0/UCDownloads/d2239ea6a922486c9e5f46f67f9d90dc_R_640_320.jpg", img);
        img2 = (ImageView) findViewById(R.id.img2);
        UILUtils.displayLocalImage("/storage/sdcard0/UCDownloads/d2239ea6a922486c9e5f46f67f9d90dc_R_640_320.jpg", img2);
        img3 = (ImageView) findViewById(R.id.img3);
//        UILUtils.displayLocalImageEqualWindows(this, "/storage/sdcard0/UCDownloads/d2239ea6a922486c9e5f46f67f9d90dc_R_640_320.jpg", img3);
    }
}
