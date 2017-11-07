package com.nemo.imageloaderdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lxb.util.UILUtils;
import com.mining.app.zxing.camera.MipcaActivityCapture;

/**
 * Created by Administrator on 2016/3/2.
 */
public class MainActivity extends Activity {
    private ImageView img, img2, img3;
    private Button btn_zxing;
    public static final int RESULT_QRCODE = 104;// 二维码

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
        btn_zxing = (Button) findViewById(R.id.btn_zxing);
        btn_zxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RESULT_QRCODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_QRCODE) {
                final String reString = intent.getStringExtra("result");
                System.out.println("---扫描结果:" + reString);
            }
        }
    }
}
