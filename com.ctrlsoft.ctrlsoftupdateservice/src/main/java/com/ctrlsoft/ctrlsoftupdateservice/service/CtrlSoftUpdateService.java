package com.ctrlsoft.ctrlsoftupdateservice.service;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ctrlsoft.ctrlsoftupdateservice.bean.UpdateBean;
import com.ctrlsoft.ctrlsoftupdateservice.constant.ICtrlSoftUpdateConstant;
import com.ctrlsoft.ctrlsoftupdateservice.util.CommonUtil;
import com.ctrlsoft.ctrlsoftupdateservice.util.CtrlSoftUpdateUtil;
import com.ctrlsoft.ctrlsoftupdateservice.util.HttpUtil;
import com.ctrlsoft.ctrlsoftupdateservice.util.UpdateStatus;

public class CtrlSoftUpdateService extends Service {
	static String TAG = "com.ctrlsoft.ctrlsoftupdateservice.CtrlSoftUpdateService";
	boolean isShowLog = true;// 默认不显示日志
	// boolean isAutoPopup = true;// 默认自动弹出更新dialog
	SharedPreferences sp;
	ExecutorService pool = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// CtrlSoftUpdateUtil.showLog(TAG, TAG+" onCreate.", isShowLog);

		try {// 初始化参数

		} catch (Exception ex) {
			// if (ex != null) {
			// CtrlSoftUpdateUtil.showLog(TAG, TAG+"onCreateException=" +
			// ex.getMessage(), isShowLog);
			// }
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		try {
			if (sp == null) {
				sp = getSharedPreferences("ctrlsoft_update_config",
						Activity.MODE_PRIVATE);
			}
			isShowLog = intent.getBooleanExtra(
					ICtrlSoftUpdateConstant.SERVICE_NEED_LOG_ACTION, false);
			CtrlSoftUpdateUtil.showLog(TAG, TAG + " onStart.", isShowLog);
			// 服务执行
			// 自动更新
			if (intent
					.getStringExtra(ICtrlSoftUpdateConstant.SERVICE_ACTION)
					.equalsIgnoreCase(
							ICtrlSoftUpdateConstant.IntentActionName.UPDATEAUTOMATIC)) {
				if (CommonUtil.isNetWorkConnected(getBaseContext())) {
					checkUpdateInfo(false);
				} else {
					CtrlSoftUpdateUtil.showLog(TAG, TAG
							+ "checkupdate auto no network", isShowLog);
					stopSelf();
				}
			}
			// 手动更新
			else if (intent.getStringExtra(
					ICtrlSoftUpdateConstant.SERVICE_ACTION).equalsIgnoreCase(
					ICtrlSoftUpdateConstant.IntentActionName.UPDATEMANUAL)) {
				if (CommonUtil.isNetWorkConnected(getBaseContext())) {
					checkUpdateInfo(true);
				} else {
					CtrlSoftUpdateUtil.showLog(TAG, TAG
							+ "checkupdate manual no network", isShowLog);
					Toast.makeText(CtrlSoftUpdateService.this,
							ICtrlSoftUpdateConstant.noNetWork,
							Toast.LENGTH_SHORT).show();
					stopSelf();
				}
			} else {
				stopSelf();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 判断更新
	 *
	 * @param isManual
	 *            是否是手动更新，是的话强制弹出更新dialog
	 */
	private void checkUpdateInfo(final boolean isManual) {
		System.out.println("开始判断版本是否一致");
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
							CtrlSoftUpdateUtil
									.getCtrlSoftUpdateKey(getBaseContext())));
					nameValuePairs.add(new BasicNameValuePair("os_type", "0"));
					nameValuePairs.add(new BasicNameValuePair("version_code",
							CtrlSoftUpdateUtil
									.getAppVersionCode(getBaseContext()) + ""));
					String result = HttpUtil.posturl(nameValuePairs,
							ICtrlSoftUpdateConstant.UPDATEADDRESS, "utf-8");
					System.out.println(result);
					JSONObject dataJson = new JSONObject(result);
					String ret = dataJson.getString("result");
					UpdateBean ub = null;
					int updateStatus = UpdateStatus.No;
					if (ret.equals("true"))// 有新的版本
					{
						JSONObject app_version = dataJson
								.getJSONObject("content");
						ub = JSON.parseObject(app_version.toString(),
								UpdateBean.class);
						updateStatus = UpdateStatus.Yes;
						if (sp.getBoolean("silent", false)) {
							CtrlSoftUpdateUtil.startDownUpdateAPKService(
									getBaseContext(), isShowLog, ub);
						} else {
							if (sp.getString("ignore", "").equals(
									ub.getVersionCode())) {
								if (isManual) {
									postMsg(ub);
								}
							} else {
								postMsg(ub);
							}
						}
					} else {
						CtrlSoftUpdateUtil.showLog(TAG,
								"checkUpdateInfo success,no update", isShowLog);
						if (isManual) {
							postMsg("isnew");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (e != null) {
						CtrlSoftUpdateUtil.showLog(TAG,
								"checkUpdateInfo exception=" + e.getMessage(),
								isShowLog);
					}
				} finally {
					stopSelf();
				}
			}
		});
	}

	/**
	 * 开启更新提示的Activity
	 *
	 * @param context
	 * @param result
	 */
	private void showUpdateDialog(Context context, final UpdateBean ub) {
		Intent intent = new Intent(
				this,
				com.ctrlsoft.ctrlsoftupdateservice.ui.UpdateDialogActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ub", ub);
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CtrlSoftUpdateUtil.showLog(TAG, TAG + " onDestroy.", isShowLog);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj instanceof UpdateBean) {
				System.out.println("显示更新Dialog");
				showUpdateDialog(getApplicationContext(), (UpdateBean) msg.obj);
			} else {
				String m = (String) msg.obj;
				if (m == "isnew") {
					Toast.makeText(CtrlSoftUpdateService.this,
							ICtrlSoftUpdateConstant.isNews, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	};

	private void postMsg(String s) {
		Message msg = Message.obtain();
		msg.obj = s;
		handler.sendMessage(msg);
	}

	private void postMsg(UpdateBean s) {
		Message msg = Message.obtain();
		msg.obj = s;
		handler.sendMessage(msg);
	}
}
