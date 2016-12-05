package com.lxb.view.fileselectlibrary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.util.Log;

public final class FileUtil {

    public static void copyFile(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {

        	Log.e("aa", "FileUtil 文件开始复制...");
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            Log.e("aa", "FileUtil 文件结束复制...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if(null != fi){
            		fi.close();
            	}
                if(null != in){
                	in.close();
                }
                if(null != fo){
                	fo.close();
                }
                if(null != out){
                	out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
}
