package com.lxb.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lxb.util.GetPathFromUri4kitkat;
import com.lxb.util.MemInfo;


/**
 * 获取本地图片 �?压缩并返�?
 *
 * @author Administrator
 */
public class LocalImageActivity extends Activity {
    final int IMAGE_SELECT = 15; // 浏览本地相册选择
    int maxPixel = 1024;
    private int screenWidth = 0;
    private int screenHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels; // 屏幕宽度（像素）
        screenHeight = metric.heightPixels; // 屏幕高度（像素）

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            if (MemInfo.getmem_UNUSED(LocalImageActivity.this) < 1024 * 210) {// 小于1g
//                Toast.makeText(this, "内存不足，无法使用，请尝试清理手机内存或重启再使用", Toast.LENGTH_SHORT).show();
//                finish();
//                overridePendingTransition(0, 0);
//            } else {
            try {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_SELECT);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            }
        } else {
            Toast.makeText(this, "sd卡不存在,请插入sd卡", Toast.LENGTH_LONG).show();
            Intent it = new Intent();
            it.putExtra("path", "");
            it.putExtra("tag", getIntent().getIntExtra("tag", 0));
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_SELECT)// 浏览本地相册选择
            {
                try {
                    Uri uri = data.getData();
                    String realPath = GetPathFromUri4kitkat.getPath(
                            LocalImageActivity.this, uri);
                    Intent it = new Intent();
                    it.putExtra("path", realPath);
                    it.putExtra("tag", getIntent().getIntExtra("tag", 0));
                    setResult(Activity.RESULT_OK, it);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else // 用户没�?择照�?
        {
            finish();
        }
    }

}
