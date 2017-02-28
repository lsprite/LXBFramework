package com.nemo.imageloaderdemo.ssl;

import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * webView.setWebViewClient(new SslPinningWebViewClient(
 * WebActivity.this) {}
 */
public class SslPinningWebViewClient extends WebViewClient {

    private Context context;

    public SslPinningWebViewClient(Context context) throws IOException {
        this.context = context;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view,
                                                      String url) {
        return processRequest(Uri.parse(url));
    }

    @Override
    @TargetApi(21)
    public WebResourceResponse shouldInterceptRequest(final WebView view,
                                                      WebResourceRequest interceptedRequest) {
        return processRequest(interceptedRequest.getUrl());
    }

    private WebResourceResponse processRequest(Uri uri) {
        try {
            HttpsURLConnection urlConnection = HttpsUtil
                    .getHttpsURLConnection(context, uri.toString(), "GET");
            // Setup connection
            urlConnection.connect();
            // Get content, contentType and encoding
            InputStream is = urlConnection.getInputStream();
            //
            // Map<String, List<String>> headerFields = urlConnection
            // .getHeaderFields();
            // List<String> cookiesHeader = headerFields.get("Set-Cookie");
            // if (cookiesHeader != null) {
            // for (String cookie : cookiesHeader) {
            // LogUtil.log("urlconnection set-cookie", cookie.toString());
            // }
            // }
            // Map map = urlConnection.getHeaderFields();
            // Set ks = map.keySet();
            // for (Iterator iterator = ks.iterator(); iterator.hasNext();) {
            // String key = (String) iterator.next();
            // System.out.println(key + ":" + map.get(key));
            // }
            // BufferedReader in = new BufferedReader(new InputStreamReader(is,
            // "GBK"));
            // StringBuffer buffer = new StringBuffer();
            // String line = "";
            // while ((line = in.readLine()) != null) {
            // buffer.append(line);
            // }
            // String str = buffer.toString();
            // System.out.println("++++res:" + str);
            //
            String contentType = urlConnection.getContentType();
            String encoding = urlConnection.getContentEncoding();
            // If got a contentType header
            if (contentType != null) {
                String mimeType = contentType;
                // Parse mime type from contenttype string
                if (contentType.contains(";")) {
                    mimeType = contentType.split(";")[0].trim();
                }
                // Return the response
                return new WebResourceResponse(mimeType, encoding, is);
            } else {
                if (uri.toString().contains("jquery")
                        || uri.toString().contains("js")) {
                    return new WebResourceResponse("text/javascript", encoding,
                            is);
                }
                return new WebResourceResponse("", encoding, is);
            }

        } catch (SSLHandshakeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return empty response for this request
        return new WebResourceResponse(null, null, null);
    }
}