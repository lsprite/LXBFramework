package com.nemo.imageloaderdemo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.os.Environment;

public class JSHelper {
	// 判断本地是否有缓存
	// return true:有存在
	public static boolean isURLDownValid(Context context, String url,
			InputStream ins) {
		try {
			String jsFileName = getReallyFileName(url);
			String urlMd5 = getUrltJSPath(context, getMd5(getReallyUrl(url)));
			File file = new File(urlMd5 + jsFileName);
			if (file.exists()) {
				return true;
			} else {
				OutputStream os = new FileOutputStream(file);
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				System.out.println("js保存在本地---" + url);
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	// 获取字符串md5码
	public static String getMd5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return result;
	}

	// // 获取缓存js根目录
	// public static String getRootJSPath(Context context) {
	// String dirPath = Environment.getExternalStorageDirectory()
	// + "/Android/data/" + context.getPackageName() + "/JS/";
	// File file = new File(dirPath);
	// if (!file.exists() || !file.isDirectory())
	// file.mkdirs();
	// return dirPath;
	// }

	// 获取url对应的缓存js根目录
	public static String getUrltJSPath(Context context, String urlMd5) {
		String dirPath = Environment.getExternalStorageDirectory()
				+ "/Android/data/" + context.getPackageName() + "/JS/" + urlMd5
				+ "/";
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory())
			file.mkdirs();
		return dirPath;
	}

	// public static boolean isLocalJsExit(Context context, String url) {
	// String filePath = Environment.getExternalStorageDirectory()
	// + "/Android/data/" + context.getPackageName() + "/JS/"
	// + getMd5(getReallyUrl(url)) + "/" + getReallyFileName(url);
	// File file = new File(filePath);
	// if (file.exists()) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	public static String getLocalJsPath(Context context, String url) {
		String filePath = Environment.getExternalStorageDirectory()
				+ "/Android/data/" + context.getPackageName() + "/JS/"
				+ getMd5(getReallyUrl(url)) + "/" + getReallyFileName(url);
		return filePath;
	}

	// 获取url中的文件名
	public static String getReallyFileName(String url) {
		try {
			String[] ss = url.split("\\?");// "url":
											// "/api/records?token=BD765FE9AC6DB",去掉后面的token
			String[] t = ss[0].split("/");
			return t[t.length - 1];
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

	// 获取url中的真实地址(去掉?后面的)
	public static String getReallyUrl(String url) {
		try {
			String[] ss = url.split("\\?");// "url":
											// "/api/records?token=BD765FE9AC6DB",去掉后面的token
			return ss[0];
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
}
