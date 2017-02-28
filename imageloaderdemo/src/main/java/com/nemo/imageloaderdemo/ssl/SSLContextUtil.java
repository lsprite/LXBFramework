package com.nemo.imageloaderdemo.ssl;


import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * 安卓使用两张bks证书
 * //keyManager使用的是xk.bks，trustManager使用的ca.bks
 */
public class SSLContextUtil {
    public static SSLContext setCertificate(Context con) {
        SSLContext sslContext = null;
        InputStream in_bks = null;
        try {
            InputStream is = con.getAssets().open("ca.bks"); // 得到证书的输入流
            CertificateFactory certificateFactory = CertificateFactory
                    .getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, "XJdMNy9Ktwmf1xNLvBC9kCalwU2cBtVd".toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory
                    .getTrustManagers();

            sslContext = SSLContext.getInstance("SSL");

            in_bks = con.getAssets().open("xj.bks");
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(in_bks,
                    "cau2FhDHu2SOQuigJZQ58Z/ICsNDb9Nc".toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore,
                    "cau2FhDHu2SOQuigJZQ58Z/ICsNDb9Nc".toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers,
                    new SecureRandom());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }
}
