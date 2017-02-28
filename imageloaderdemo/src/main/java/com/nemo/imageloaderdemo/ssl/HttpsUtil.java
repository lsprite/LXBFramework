package com.nemo.imageloaderdemo.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.content.Context;

import com.ctrlsoft.xm_pwjkxj.MyApplication;
import com.ctrlsoft.xm_pwjkxj.util.LogUtil;

public class HttpsUtil {

	public static HttpsURLConnection getHttpsURLConnection(Context mcontext,
			String urlpath, String method) throws Exception {
		// Setup connection
		//
		LogUtil.log("HttpsURLConnection", urlpath);
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
				LogUtil.log("++hostname", hostname);
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
