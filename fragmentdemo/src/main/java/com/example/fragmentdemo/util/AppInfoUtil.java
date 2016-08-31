package com.example.fragmentdemo.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * http://blog.csdn.net/xfks55/article/details/6890730
 */
public class AppInfoUtil {
	public static void getAppInfo(Context context) {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(0);

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					context.getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;
			tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context
					.getPackageManager());
			// Only display the non-system app info
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				appList.add(tmpInfo);// 如果非系统应用，则添加至appList
				tmpInfo.print();
			}

		}
	}
}

class AppInfo {
	public String appName = "";
	public String packageName = "";
	public String versionName = "";
	public int versionCode = 0;
	public Drawable appIcon = null;

	public void print() {
		Log.v("app", "Name:" + appName + " Package:" + packageName);
		Log.v("app", "Name:" + appName + " versionName:" + versionName);
		Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
	}

}