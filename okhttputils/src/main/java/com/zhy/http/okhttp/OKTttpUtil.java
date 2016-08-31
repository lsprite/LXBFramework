package com.zhy.http.okhttp;

import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nemo on 2016/2/25.
 */
public class OKTttpUtil {

    public static void posturl(String url, Map<String, String> paramters,
                               Map<String, File> filesMap, Callback callback) {
        PostFormBuilder builder = OkHttpUtils.post();//
        builder.url(url)
                .params(paramters);
        Iterator iter = filesMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            File val = (File) entry.getValue();
            builder.addFile("mFile", key, val);
        }
        builder.build().execute(callback);
    }

    public static void posturl(String url, Map<String, String> paramters,
                               Callback callback) {
        PostFormBuilder builder = OkHttpUtils.post();//
        builder.url(url)
                .params(paramters);
        builder.build().execute(callback);
    }
}
