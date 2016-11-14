package com.lxb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Download {
    /**
     * 连接url
     */
    private String urlstr;
    /**
     * sd卡目录路径
     */
    private String sdcard;
    /**
     * http连接管理类
     */
    private HttpURLConnection urlcon;
    private String filename = "";

    public Download(Context context, String url) {
        this.urlstr = url;
        filename = getReallyFileName(url) + ".tmp";
        // 获取设备sd卡目录
        this.sdcard = Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/download/";
        // this.sdcard = Environment.getExternalStorageDirectory() + "/";
        urlcon = getConnection();
    }

    /*
     * 读取网络文本
     */
    public String downloadAsString() {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        try {
            InputStream is = urlcon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /*
     * 获取http连接处理类HttpURLConnection
     */
    private HttpURLConnection getConnection() {
        URL url;
        HttpURLConnection urlcon = null;
        try {
            url = new URL(urlstr);
            urlcon = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlcon;
    }

    /*
     * 获取连接文件长度。
     */
    public int getLength() {
        return urlcon.getContentLength();
    }

    /*
     * 写文件到sd卡 demo 前提需要设置模拟器sd卡容量，否则会引发EACCES异常 先创建文件夹，在创建文件
     */
    public int down2sd(Context context, DownListener listener) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "请检查sd卡是否存在或者是否有程序限制了sd卡访问权限",
                    Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.error();
            }
            return -1;
        }
        StringBuilder sb = new StringBuilder(sdcard);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
            // 创建文件夹
            Log.d("log", sb.toString());
        }
        // 获取文件全名
        sb.append(filename);
        try {
            File l = new File(filename);
            if (l.exists()) {
                l.delete();
            }
            l = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024 * 8];
        int read;
        int ava = 0;
        long start = System.currentTimeMillis();
        BufferedInputStream bin;
        try {
            URL u = new URL(urlstr);
            HttpURLConnection urlcon = (HttpURLConnection) u.openConnection();
            double fileLength = (double) urlcon.getContentLength();
            bin = new BufferedInputStream(u.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(sb.toString()));
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                int a = (int) Math.floor((ava / fileLength * 100));
                long speed = ava / (System.currentTimeMillis() - start);
                // System.out.println("Download: " + ava + " byte(s)"
                // + "    avg speed: " + speed + "  (kb/s)");
            }
            bout.flush();
            bout.close();
            File tf = new File(sdcard + filename);
            String t = sdcard + filename;
            tf.renameTo(new File(t.substring(0, t.length() - 4)));
            if (listener != null) {
                listener.complete(
                        (sdcard + filename).substring(0,
                                (sdcard + filename).length() - 4),
                        filename.substring(0, filename.length() - 4));
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;
    }

    /*
     * 内部回调接口类
     */
    public abstract class DownListener {
        public abstract void setSize(int size);

        public abstract void complete(String path, String filename);

        public abstract void error();
    }

    public static String getReallyFileName(String url) {
        try {
            System.out.println(url);
            String[] ss = url.split("\\?");// "url":
            // "/api/records?token=BD765FE9AC6DB",去掉后面的token
            String[] t = ss[0].split("/");
            return t[t.length - 1];
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }
}
