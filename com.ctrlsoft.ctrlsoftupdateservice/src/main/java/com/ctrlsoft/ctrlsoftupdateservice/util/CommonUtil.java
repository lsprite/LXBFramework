package com.ctrlsoft.ctrlsoftupdateservice.util;


import java.io.File;
import java.util.Vector;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;


public class CommonUtil {

	/**
	 * 分隔字符串
	 * @param fgf 分隔符
	 * @param content 字符
	 * @return 分隔好后的数组
	 */
	public static String[] splitString(String fgf, String content) {
		String[] result = null;
		if (content.indexOf(fgf) == -1) {
			result = new String[1];
			result[0] = content;

		} else {
			Vector<String> vector = new Vector<String>();
			int nowposition = 0;
			int nextpos;
			String temp;
			nextpos = content.indexOf(fgf, nowposition);
			temp = read(fgf, content, nowposition, nextpos);
			while (temp != "") {
				vector.add(temp);
				nowposition = nextpos + fgf.length();
				nextpos = content.indexOf(fgf, nowposition);
				if (nextpos < 0 && nowposition < content.length())// 如果现在为止不是最后，当时又找不到下一个分隔符的位置，说明当前这个是最后一个，直接加入，退出
				{
					temp = read(fgf, content, nowposition, content.length());
					vector.add(temp);
					break;
				}
				temp = read(fgf, content, nowposition, nextpos);
			}
			if (content.substring(content.length() - fgf.length(),
					content.length()).equals(fgf))// 如果最后一个是fgf，添加最后一个数组纬度，内容为“”
				result = new String[vector.size() + 1];
			else
				result = new String[vector.size()];
			for (int i = 0; i < vector.size(); i++) {
				result[i] = vector.elementAt(i).toString();
			}
			if (content.substring(content.length() - fgf.length(),
					content.length()).equals(fgf))
				result[result.length - 1] = "";
		}
		return result;
	}

	public static String read(String FGF, String content, int nowposition,
							  int nextpos) {
		if (nextpos < 0) {
			return "";
		} else {
			String result = content.substring(nowposition, nextpos);
			return result;
		}
	}

	/**
	 * 获取APP的versionName
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context)
	{
		String pkName = context.getPackageName();
		try {
			return context.getPackageManager().getPackageInfo(pkName, 0).versionName;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取APP的Name(非通过R.string方式)
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context)
	{
		String pkName = context.getPackageName();
		try {
			return context.getPackageManager().getApplicationInfo(pkName, 0).loadLabel(context.getPackageManager()).toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取APP的Ico(非通过R.string方式)
	 * @param context
	 * @return
	 */
	public static Drawable getAppIco(Context context)
	{
		String pkName = context.getPackageName();
		try {
			return context.getPackageManager().getApplicationInfo(pkName, 0).loadIcon(context.getPackageManager());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取APP的Ico
	 * @param context
	 * @return int
	 */
	public static int getAppIcoInt(Context context)
	{
		String pkName = context.getPackageName();
		try {
			Log.i("GYH", "to string="+context.getPackageManager().getApplicationInfo(pkName, 0).loadIcon(context.getPackageManager()).toString());
			ComponentName cn = new ComponentName(context, context.getClass());
			Log.i("GYH", "ComponentName init");

			return context.getPackageManager().getReceiverInfo(cn, 0).getIconResource();

		} catch (Exception e) {
			Log.i("GYH", e.getMessage());
			return 0;
		}
	}

	/**
	 * 根据datetime格式返回日期
	 * @param dateTime 2013-9-23 8:09:23
	 * @return 2013-9-23
	 */
	public static String getDateFormDateTime(String dateTime)
	{
		try {
			String[] result = splitString(" ", dateTime);
			return result[0];
		} catch (Exception e) {
			return dateTime;
		}
	}

	/**
	 * 安装下载好的apk包
	 * @param file
	 * @param context
	 */
	public static void Instanll(File file,Context context){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context){
		ConnectivityManager mgr=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(mgr!=null){
			NetworkInfo[] netWorks=mgr.getAllNetworkInfo();
			for(NetworkInfo netWork:netWorks){
				if(NetworkInfo.State.CONNECTED==netWork.getState()){
					return true;
				}
			}
		}
		return false;
	}

}
