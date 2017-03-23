package com.nemo.imageloaderdemo.ssl;

import android.content.Context;


import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

public class HttpsUtil {

	public static HttpsURLConnection getHttpsURLConnection(Context mcontext,
			String urlpath, String method) throws Exception {
		// Setup connection
		//
		URL url = new URL(urlpath);
		HttpsURLConnection urlConnection = (HttpsURLConnection) url
				.openConnection();
		// urlConnection.setRequestProperty("Cookie",
		// CookieUtil.getCookieString(mcontext));
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		urlConnection.setRequestProperty("Content-Type", "binary/octet-stream");
		urlConnection.setRequestMethod(method);
		// Set SSL Socket Factory for this request
		SSLContext sslContext = SSLContextUtil.setCertificate(mcontext);

		urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
		// ��������֤��
		urlConnection.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// if ("222.76.241.234".equals(hostname)) {
				return true;
				// } else {
				// return false;
				// }
			}
		});
		return urlConnection;

	}

}
