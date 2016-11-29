package com.ctrlsoft.ctrlsoftupdateservice.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ctrlsoft.ctrlsoftupdateservice.bean.UpdateBean;
import com.ctrlsoft.ctrlsoftupdateservice.constant.ICtrlSoftUpdateConstant;
import com.ctrlsoft.ctrlsoftupdateservice.util.CPResourceUtil;
import com.ctrlsoft.ctrlsoftupdateservice.util.CommonUtil;
import com.ctrlsoft.ctrlsoftupdateservice.util.CtrlSoftUpdateUtil;

public class CtrlSoftDownApkService extends Service {
	static String TAG = "com.ctrlsoft.ctrlsoftupdateservice.CtrlSoftDownApkService";
	boolean isShowLog = false;// 默认不显示日志

	private static final int NOTIFICATION_ID = 0x83743755;
	private Notification notification = null;
	private NotificationManager manager = null;

	private File tempFile = null;
	private boolean cancelUpdate = false;
	private MyHandler myHandler;
	private int download_precent = 0;
	// private RemoteViews views;

	private UpdateBean updateBean;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// CtrlSoftUpdateUtil.showLog(TAG, TAG+" onCreate.", isShowLog);
		try {// 初始化参数
			// intent = getBaseContext().
			notification = new Notification();
			// notification.icon = CommonUtil.getAppIco(getBaseContext()).
			int resid = CommonUtil.getAppIcoInt(getBaseContext());
			Log.i(TAG, TAG + " resid=" + resid);
			// notification.icon =
			// R.drawable.ic_launcher;//CommonUtil.getAppIcoInt(getBaseContext());
			// notification.icon = getApplicationInfo().icon;
			notification.icon = android.R.drawable.stat_sys_download;
			notification.tickerText = CommonUtil.getAppName(getBaseContext())
					+ "更新";
			notification.when = System.currentTimeMillis();
			notification.defaults = Notification.DEFAULT_LIGHTS;

			// notification.contentView = new RemoteViews(getBaseContext()
			// .getPackageName(), R.layout.ctrlsoftupdatenotification);
			// notification.contentView.setProgressBar(
			// R.ctrlsoftupdate_id.progressBar1, 100, 0, false);
			//
			// notification.contentView.setTextViewText(
			// R.ctrlsoftupdate_id.progressBarTV, "进度" + download_precent
			// + "%");
			// notification.contentView.setTextViewText(
			// R.ctrlsoftupdate_id.appnameTV,
			// CommonUtil.getAppName(getBaseContext()) + "下载更新包");
			// =============
			notification.contentView = new RemoteViews(getBaseContext()
					.getPackageName(), CPResourceUtil.getLayoutId(
					getBaseContext(), "ctrlsoftupdatenotification"));
			try {
				notification.contentView.setImageViewResource(
						CPResourceUtil.getId(getBaseContext(), "appicon"),
						getApplicationInfo().icon);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("图标出错了");
				e.printStackTrace();
			}
			// notification.contentView.setImageViewResource(CPResourceUtil.getId(
			// getBaseContext(), "appicon"), CPResourceUtil.getDrawableId(
			// getBaseContext(), "ic_launcher"));
			notification.contentView.setProgressBar(
					CPResourceUtil.getId(getBaseContext(), "progressBar1"),
					100, 0, false);
			notification.contentView.setTextViewText(
					CPResourceUtil.getId(getBaseContext(), "progressBarTV"),
					"进度" + download_precent + "%");
			notification.contentView.setTextViewText(
					CPResourceUtil.getId(getBaseContext(), "appnameTV"),
					CommonUtil.getAppName(getBaseContext()) + "下载更新包");
			// ==============
			manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			myHandler = new MyHandler(Looper.myLooper(), this);
			// 初始化下载任务内容views
			Message message = myHandler.obtainMessage(3, 0);
			myHandler.sendMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex != null) {
				CtrlSoftUpdateUtil
						.showLog(TAG,
								TAG + "onCreateException=" + ex.getMessage(),
								isShowLog);
			}
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// notification.icon = intent.getIntExtra(
		// ICtrlSoftUpdateConstant.SERVICE_ICO_ID, 0);
		try {
			isShowLog = intent.getBooleanExtra(
					ICtrlSoftUpdateConstant.SERVICE_NEED_LOG_ACTION, false);
			CtrlSoftUpdateUtil.showLog(TAG, TAG + " onStart.", isShowLog);
			// 服务执行
			updateBean = (UpdateBean) intent
					.getSerializableExtra(ICtrlSoftUpdateConstant.SERVICE_UPDATEBEAN);

			CtrlSoftUpdateUtil.showLog(TAG, TAG + " start down apk,url ="
					+ updateBean.getAppUrl(), isShowLog);
			// + ICtrlSoftUpdateConstant.DOWNAPKADDRESS

			if (CommonUtil.isNetWorkConnected(getBaseContext())) {
				manager.notify(NOTIFICATION_ID, notification);
				// startForeground(NOTIFICATION_ID, notification);
				downFile(updateBean.getAppUrl());
			} else {
				CtrlSoftUpdateUtil.showLog(TAG, TAG + "downapk no network",
						isShowLog);
				stopSelf();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CtrlSoftUpdateUtil.showLog(TAG, TAG + " onDestroy.", isShowLog);
		try {
			manager.cancel(NOTIFICATION_ID);
		} catch (Exception e) {
		}
	}

	// 下载更新文件
	private void downFile(final String url) {
		new Thread() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					// params[0]代表连接的url
					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					if (is != null) {
						File rootFile = new File(
								Environment.getExternalStorageDirectory(),
								"/download");
						if (!rootFile.exists() && !rootFile.isDirectory())
							rootFile.mkdir();

						tempFile = new File(
								Environment.getExternalStorageDirectory(),

								"/download/"
										+ url.substring(url.lastIndexOf("/") + 1));
						if (tempFile.exists())
							tempFile.delete();
						tempFile.createNewFile();

						// 已读出流作为参数创建一个带有缓冲的输出流
						BufferedInputStream bis = new BufferedInputStream(is);

						// 创建一个新的写入流，讲读取到的图像数据写入到文件中
						FileOutputStream fos = new FileOutputStream(tempFile);
						// 已写入流作为参数创建一个带有缓冲的写入流
						BufferedOutputStream bos = new BufferedOutputStream(fos);

						int read;
						long count = 0;
						int precent = 0;
						byte[] buffer = new byte[1024];
						while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
							bos.write(buffer, 0, read);
							count += read;
							precent = (int) (((double) count / length) * 100);

							// 每下载完成5%就通知任务栏进行修改下载进度
							if (precent - download_precent >= 1) {
								download_precent = precent;
								Message message = myHandler.obtainMessage(3,
										precent);
								myHandler.sendMessage(message);
								Intent intent = new Intent(
										getPackageName()
												+ ".ctrlsoftdownapkservice.dialog.precent");
								intent.putExtra("precent", download_precent);
								sendBroadcast(intent);
							}
						}
						bos.flush();
						bos.close();
						fos.flush();
						fos.close();
						is.close();
						bis.close();
					}

					if (!cancelUpdate) {
						Message message = myHandler.obtainMessage(2, tempFile);
						myHandler.sendMessage(message);
					} else {
						tempFile.delete();
					}
				} catch (ClientProtocolException e) {
					CtrlSoftUpdateUtil.showLog(TAG,
							"ClientProtocolException => " + e.getMessage(),
							isShowLog);
					Message message = myHandler.obtainMessage(4, "下载更新文件失败");
					myHandler.sendMessage(message);
				} catch (IOException e) {
					CtrlSoftUpdateUtil.showLog(TAG,
							"IOException => " + e.getMessage(), isShowLog);
					Message message = myHandler.obtainMessage(4, "下载更新文件失败");
					myHandler.sendMessage(message);
				} catch (Exception e) {
					CtrlSoftUpdateUtil.showLog(TAG,
							"Exception => " + e.getMessage(), isShowLog);
					Message message = myHandler.obtainMessage(4, "下载更新文件失败");
					myHandler.sendMessage(message);
				}
			}
		}.start();
	}

	/* 事件处理类 */
	class MyHandler extends Handler {
		private Context context;

		public MyHandler(Looper looper, Context c) {
			super(looper);
			this.context = c;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg != null) {
				switch (msg.what) {
					case 0:
						Toast.makeText(context, msg.obj.toString(),
								Toast.LENGTH_SHORT).show();
						break;
					case 1:
						break;
					case 2:
						// 下载完成后清除所有下载信息，执行安装提示
						download_precent = 0;
						manager.cancel(NOTIFICATION_ID);
						CommonUtil.Instanll((File) msg.obj, context);

						// 停止掉当前的服务
						stopSelf();
						break;
					case 3:
						// 更新状态栏上的下载进度信息
						// notification.contentView.setTextViewText(
						// R.ctrlsoftupdate_id.progressBarTV, "已下载"
						// + download_precent + "%");
						// notification.contentView.setProgressBar(
						// R.ctrlsoftupdate_id.progressBar1, 100,
						// download_precent, false);
						// ========
						notification.contentView.setTextViewText(CPResourceUtil
								.getId(getBaseContext(), "progressBarTV"), "已下载"
								+ download_precent + "%");
						notification.contentView.setProgressBar(CPResourceUtil
										.getId(getBaseContext(), "progressBar1"), 100,
								download_precent, false);
						// ==========
						manager.notify(NOTIFICATION_ID, notification);
						break;
					case 4:
						manager.cancel(NOTIFICATION_ID);
						break;
				}
			}
		}
	}

	// 本方法判断自己些的一个Service
	public static boolean isRunning(Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service
					.getClassName()
					.toString()
					.equals(context.getPackageName()
							+ ".service.CtrlSoftDownApkService")) {
				return true;
			}
		}
		return false;
	}
}
