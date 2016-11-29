package com.ctrlsoft.ctrlsoftupdateservice.util;

/**
 * 主要方法<br>
 * 1)自动更新:<br>
 * 		CtrlSoftUpdateUtil.startAutoUpdate(MainActivity.this, true);
 * 2)手动更新:<br>
 * 		CtrlSoftUpdateUtil.startManualUpdate()<br>
 * 		PS：添加更新监听	CtrlSoftUpdateUtil.setOnManualListening(
 * 3)静默更新:<br>
 * 		在自动更新或手动监听之前添加CtrlSoftUpdateUtil.silentUpdate(MainActivity.this,true)
 */
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ctrlsoft.ctrlsoftupdateservice.bean.UpdateBean;
import com.ctrlsoft.ctrlsoftupdateservice.constant.ICtrlSoftUpdateConstant;

public class CtrlSoftUpdateUtil {
	// private static CtrlSoftUpdateUtil instance = null;
	static ExecutorService pool = null;

	// public CtrlSoftUpdateUtil() {
	// // TODO Auto-generated constructor stub
	// }
	//
	// public static CtrlSoftUpdateUtil getInstance() {
	// if (instance == null) {
	// instance = new CtrlSoftUpdateUtil();
	// }
	// return instance;
	// }

	/**
	 * 获取更新key 读取androidmanifest的application节点中的meta-data的值
	 *
	 * @return
	 */
	public static String getCtrlSoftUpdateKey(Context context) {
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getString("CTRLSOFT_UPDATEKEY");
		} catch (Exception e) {
			return null;
		}

	}

	public static int getAppVersionCode(Context context) {
		String pkName = context.getPackageName();
		try {
			return context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 设置显示日志
	 *
	 * @param title
	 * @param content
	 * @param isShow
	 */
	public static void showLog(String title, String content, boolean isShow) {
		if (isShow) {
			Log.d(title, content);
		}
	}

	/**
	 * 开启自动更新
	 *
	 * @param context
	 * @param needLog是否需要日志
	 */
	public static void startAutoUpdate(Context context, boolean needLog) {
		Intent intent = new Intent(ICtrlSoftUpdateConstant.SERVICE_NAME);
		intent.setClass(
				context,
				com.ctrlsoft.ctrlsoftupdateservice.service.CtrlSoftUpdateService.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean(ICtrlSoftUpdateConstant.SERVICE_NEED_LOG_ACTION,
				needLog);
		bundle.putString(ICtrlSoftUpdateConstant.SERVICE_ACTION,
				ICtrlSoftUpdateConstant.IntentActionName.UPDATEAUTOMATIC);
		intent.putExtras(bundle);
		context.startService(intent);
	}

	/**
	 * 开启手动更新
	 *
	 * @param context
	 * @param needLog是否需要日志
	 */
	public static void startManualUpdate(Context context, boolean needLog) {
		Intent intent = new Intent(ICtrlSoftUpdateConstant.SERVICE_NAME);
		intent.setClass(
				context,
				com.ctrlsoft.ctrlsoftupdateservice.service.CtrlSoftUpdateService.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean(ICtrlSoftUpdateConstant.SERVICE_NEED_LOG_ACTION,
				needLog);
		bundle.putString(ICtrlSoftUpdateConstant.SERVICE_ACTION,
				ICtrlSoftUpdateConstant.IntentActionName.UPDATEMANUAL);
		intent.putExtras(bundle);
		context.startService(intent);
	}

	public static void silentUpdate(Context context, boolean isSilent) {
		SharedPreferences sp = context.getSharedPreferences(
				"ctrlsoft_update_config", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("silent", isSilent);
		// editor.putBoolean("ignore", ignore);
		// if (isSilent) {
		// editor.putString("silent", ub.getVersonName());
		// } else {
		// editor.putString("silent", "");
		// }
		editor.commit();
	}

	/**
	 * 开启下载更新包服务
	 *
	 * @param context
	 * @param needLog
	 * @param ub
	 */
	public static void startDownUpdateAPKService(Context context,
												 boolean needLog, UpdateBean ub) {
		Intent intent = new Intent();
		intent.setClass(
				context,
				com.ctrlsoft.ctrlsoftupdateservice.service.CtrlSoftDownApkService.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean(ICtrlSoftUpdateConstant.SERVICE_NEED_LOG_ACTION,
				needLog);
		bundle.putSerializable(ICtrlSoftUpdateConstant.SERVICE_UPDATEBEAN, ub);
		intent.putExtras(bundle);
		context.startService(intent);
	}

	/**
	 * 弹出自带的提示框
	 *
	 * @param context
	 * @param ub
	 */

	public static void showUpdateDialog(Context context, final UpdateBean ub) {
		Intent intent = new Intent(
				context,
				com.ctrlsoft.ctrlsoftupdateservice.ui.UpdateDialogActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ub", ub);
		context.startActivity(intent);
	}

	/**
	 * 手动更新时的监听
	 *
	 */
	public interface OnManualListening {
		void onUpdateReturned(int updateStatus, UpdateBean ub);
	}

	static OnManualListening mOnManualListening = null;

	public static void setOnManualListening(OnManualListening e, Context context) {
		mOnManualListening = e;
		checkUpdateInfo(context);
	}

	private static void checkUpdateInfo(final Context context) {
		System.out.println("单纯检查是否有更新,开始判断版本是否一致");
		if (pool == null) {
			pool = Executors.newFixedThreadPool(1);
		}
		pool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("type",
							"checkAppUpdate"));
					nameValuePairs.add(new BasicNameValuePair("pkg_name",
							CtrlSoftUpdateUtil.getCtrlSoftUpdateKey(context)));
					nameValuePairs.add(new BasicNameValuePair("os_type", "0"));
					nameValuePairs
							.add(new BasicNameValuePair("version_code",
									CtrlSoftUpdateUtil
											.getAppVersionCode(context) + ""));
					String result = HttpUtil.posturl(nameValuePairs,
							ICtrlSoftUpdateConstant.UPDATEADDRESS, "utf-8");
					System.out.println(result);
					JSONObject dataJson = new JSONObject(result);
					String ret = dataJson.getString("result");
					UpdateBean ub = null;
					int updateStatus = UpdateStatus.No;
					if (!result.equals("true"))// 有新的版本
					{
						JSONObject app_version = dataJson
								.getJSONObject("content");
						ub = JSON.parseObject(app_version.toString(),
								UpdateBean.class);
						updateStatus = UpdateStatus.Yes;
					} else {
						updateStatus = UpdateStatus.No;
					}
					if (mOnManualListening != null) {
						mOnManualListening.onUpdateReturned(updateStatus, ub);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (mOnManualListening != null) {
						mOnManualListening.onUpdateReturned(
								UpdateStatus.Timeout, null);
					}
				} finally {
					pool = null;
				}
			}
		});
	}
}
