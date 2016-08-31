package com.lxb.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

//ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(getNameValuePair("type",
//        "get_seller_info"));
//        String retPost = HttpManager.posturl(nameValuePairs,
//        URLUtil.GETINFO, "utf-8");
//if (retPost == null || retPost.trim().equals("error")
//        || retPost.trim().equals("")
//        || retPost.trim().equals("null")) {
//        // intent = new Intent(
//        // "com.app.ztc_buyer_android.loginservice.networkerror");
//        // sendBroadcast(intent);
//        } else {
//
//        }

public class HttpManager {
    public static boolean ISDEBUG = true;

    public static String posturl(ArrayList<NameValuePair> nameValuePairs,
                                 String url, String encode) {
        String tmp = "";
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String logString = url + "?";
            for (int i = 0; i < nameValuePairs.size(); i++) {
                NameValuePair nameValuePair = nameValuePairs.get(i);
                if (i == (nameValuePairs.size() - 1)) {
                    logString += nameValuePair.getName() + "="
                            + nameValuePair.getValue();
                } else {
                    logString += nameValuePair.getName() + "="
                            + nameValuePair.getValue() + "&";
                }
                if (ISDEBUG) {
                    System.err.println(nameValuePair.getName() + "="
                            + nameValuePair.getValue());
                }
            }
            // verbose:详细的,黑色
            // Log.v("HttpManager", "HttpManager Post:"+logString);
            // debug:调试,蓝色
            // Log.d("HttpManager", "HttpManager Post:"+logString);
            // info:信息,绿色
            // Log.i("HttpManager", "HttpManager Post:" + logString);
            // warn:警告,橙色
            if (ISDEBUG) {
                Log.w("HttpManager", "HttpManager Post:" + logString);
            }
            // error:错误,红色
            // Log.e("HttpManager", "HttpManager Post:"+logString);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            return "error";
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, encode));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            tmp = sb.toString();
        } catch (Exception e) {
            return "error";
        }
        System.out.println(tmp);
        return tmp;
    }

    public static String uploadFiles(String url,
                                     Map<String, String> params, Map<String, File> filesMap,
                                     String encode) {
        String tmp = "";
        InputStream is = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpRequest = new HttpPost(url);
            MultipartEntity mEntity = new MultipartEntity();
            // 添加上传参数
            for (Map.Entry<String, String> entry : params.entrySet()) {
                ContentBody strBody = new StringBody(entry.getValue());
                mEntity.addPart(entry.getKey(), strBody);
                System.err.println(entry.getKey() + "=" + entry.getValue());
            }
            // 添加上传的文件
            Iterator<String> iterator = filesMap.keySet().iterator();
            while (iterator.hasNext()) {
                String fileName = iterator.next();
                ContentBody cBody = new FileBody(filesMap.get(fileName));
                mEntity.addPart(fileName, cBody);
                System.err.println("while-----iterator fileName:" + fileName
                        + "  file:" + filesMap.get(fileName));
            }
            httpRequest.setEntity(mEntity);
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            HttpEntity resEntity = httpResponse.getEntity();
            is = resEntity.getContent();
        } catch (Exception e) {
            return "error";
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, encode));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            tmp = sb.toString();
        } catch (Exception e) {
            return "error";
        }
        System.out.println(tmp);
        return tmp;
        // if (response.getStatusLine().getStatusCode() == 200) {
        // return true;
        // }
        // return false;
    }
}
