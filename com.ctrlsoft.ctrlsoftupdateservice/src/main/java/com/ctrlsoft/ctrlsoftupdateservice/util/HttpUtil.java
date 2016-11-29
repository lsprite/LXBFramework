package com.ctrlsoft.ctrlsoftupdateservice.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import android.util.Log;

public class HttpUtil {

	/**
	 * 以get的方式获取服务器上的数据
	 *
	 * @param urlAddress
	 *            服务器地址（带参数）
	 * @param encode
	 *            编码方式
	 * @return 返回服务器信息
	 */
	public static String HttpGetInfo(String urlAddress, String encode) {
		URL url;
		URLConnection con;
		try {
			url = new URL(urlAddress);
			con = url.openConnection();
			String result = "";
			// HttpURLConnection.HTTP_OK

			InputStream is = con.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer bab = new ByteArrayBuffer(32);
			int current = 0;
			while ((current = bis.read()) != -1) {
				bab.append((byte) current);
			}
			result = EncodingUtils.getString(bab.toByteArray(), encode);

			bis.close();
			is.close();
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/**
	 * 将ArrayList<NameValuePair> nameValuePairs的信息post给服务器
	 *
	 * @param nameValuePairs
	 *            参数
	 * @param url
	 *            服务器地址
	 * @param encode
	 *            编码方式
	 * @return 服务器返回结果
	 */
	public static String posturl(ArrayList<NameValuePair> nameValuePairs,
								 String url, String encode) {
		String tmp = "";
		InputStream is = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String logString = url;
			for (int i = 0; i < nameValuePairs.size(); i++) {
				NameValuePair nameValuePair = nameValuePairs.get(i);
				if (i == (nameValuePairs.size() - 1)) {
					logString += nameValuePair.getName() + "="
							+ nameValuePair.getValue();
				} else {
					logString += nameValuePair.getName() + "="
							+ nameValuePair.getValue() + "&";
				}
			}
			Log.w("HttpManager", "HttpManager Post:" + logString);
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

		return tmp;
	}

}
