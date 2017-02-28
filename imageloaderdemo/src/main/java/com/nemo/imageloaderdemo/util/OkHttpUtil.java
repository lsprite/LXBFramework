package com.nemo.imageloaderdemo.util;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/28.
 */

public class OkHttpUtil {
    public static String posturl(String url, Map<String, String> params) {
        try {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(3600, TimeUnit.SECONDS);
            mOkHttpClient.setWriteTimeout(3600, TimeUnit.SECONDS);
            mOkHttpClient.setReadTimeout(3600, TimeUnit.SECONDS);
            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);
            Iterator it = params.entrySet().iterator();
            String key;
            String value;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                key = entry.getKey().toString();
                value = entry.getValue().toString();
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\""
                                + key + "\""), RequestBody.create(null, value));
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder().url(url).post(requestBody)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String uploadFiles(String url, Map<String, String> params, Map<String, File> filesMap) {
        try {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(3600, TimeUnit.SECONDS);
            mOkHttpClient.setWriteTimeout(3600, TimeUnit.SECONDS);
            mOkHttpClient.setReadTimeout(3600, TimeUnit.SECONDS);
            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);
            //
            Iterator<String> iterator = filesMap.keySet().iterator();
            RequestBody fileBody = null;
            while (iterator.hasNext()) {
                String fileName = iterator.next();
                fileBody = RequestBody.create(
                        MediaType.parse(guessMimeType(fileName)),
                        filesMap.get(fileName));
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\""
                                + fileName + "\"; filename=\"" + fileName
                                + "\""), fileBody);
            }
            //
            Iterator it = params.entrySet().iterator();
            String key;
            String value;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                key = entry.getKey().toString();
                value = entry.getValue().toString();
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\""
                                + key + "\""), RequestBody.create(null, value));
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder().url(url).post(requestBody)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    //获取文件类型
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
