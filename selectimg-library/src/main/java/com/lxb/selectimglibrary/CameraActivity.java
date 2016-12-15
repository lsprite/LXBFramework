package com.lxb.selectimglibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.lxb.selectimglibrary.utils.DirCache;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hanchunping
 * @version V1.0
 * @Title: CameraActivity.java
 * @Package com.tch.main
 * @Description: 拍照�?自动调用Intent 来完成相机拍�?
 * @date 2010-9-16 上午08:59:10
 */
public class CameraActivity extends Activity {

    public static final String TAG = CameraActivity.class.getSimpleName();
    public static final int REQUESTCODE = 1;
    private String filename = "";
    private int screenWidth = 0;
    private int screenHeight = 0;
    Intent intent = null;
    // 图片的缓存目�?
    File local = new File(DirCache.createCachePath(CameraActivity.this, "camera"));

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels; // 屏幕宽度（像素）
        screenHeight = metric.heightPixels; // 屏幕高度（像素）

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            if (MemInfo.getmem_UNUSED(CameraActivity.this) < 1024 * 200) {// 小于1g
//                Toast.makeText(this, "内存不足，无法使用，请尝试清理手机内存或重启再使用", Toast.LENGTH_SHORT).show();
//                finish();
//                overridePendingTransition(0, 0);
//            } else {
            try {
                if (!local.exists()) {
                    local.mkdirs();
                }
                String filepath = local.getAbsolutePath();
                filename = filepath + "/" + createFileName() + ".jpg";
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File out = new File(filename);
                Uri uri = Uri.fromFile(out);
                intent.putExtra(ImageColumns.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUESTCODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            }

        } else {
            Toast.makeText(CameraActivity.this, "sd卡不存在,请插入sd卡", Toast.LENGTH_SHORT).show();
            Intent it = new Intent();
            it.putExtra("path", "");
            it.putExtra("tag", getIntent().getIntExtra("tag", 0));
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    Intent it = new Intent();
                    it.putExtra("path", filename);
                    it.putExtra("tag", getIntent().getIntExtra("tag", 0));
                    setResult(Activity.RESULT_OK, it);
                    finish();

                } else {
                    Intent it = new Intent();
                    it.putExtra("path", "");
                    it.putExtra("tag", getIntent().getIntExtra("tag", 0));
                    setResult(Activity.RESULT_CANCELED, it);
                    finish();
                }
            } else // 用户没照相
            {
                finish();
            }

//        }
        } catch (
                Exception e
                )

        {
            Log.v(TAG, e.getMessage());
            Toast.makeText(CameraActivity.this, "调用相机异常,现在打开程序内部相机",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public String createFileName() {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String today = sf.format(new Date());
            return today;
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
