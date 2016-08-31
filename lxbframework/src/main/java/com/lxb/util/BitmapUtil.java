package com.lxb.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * Created by Administrator on 2016/3/4.
 */
public class BitmapUtil {
    //获取Bitmap的大小
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static int getLocalImgWidth(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        return options.outWidth;
    }

    public static int getLocalImgHeigh(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        return options.outHeight;
    }

//    public static int getLocalImgSize(String pathName) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(pathName, options);
//        return options.outHeight * options.outWidth;
//    }
}
