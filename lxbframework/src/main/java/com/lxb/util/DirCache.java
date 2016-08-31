package com.lxb.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/2/18.
 * 保存在Android/data/包名/  目录下
 */
public class DirCache {
    public static String createCachePath(Context context) {
        String dirPath = Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/files";
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        return dirPath;
    }

    public static String createCachePath(Context context, String name) {
        String dirPath = Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/files/" + name;
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        return dirPath;
    }
}
