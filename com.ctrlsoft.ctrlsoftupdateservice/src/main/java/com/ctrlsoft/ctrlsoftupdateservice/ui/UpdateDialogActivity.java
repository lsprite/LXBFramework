package com.ctrlsoft.ctrlsoftupdateservice.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ctrlsoft.ctrlsoftupdateservice.bean.UpdateBean;
import com.ctrlsoft.ctrlsoftupdateservice.service.CtrlSoftDownApkService;
import com.ctrlsoft.ctrlsoftupdateservice.service.CtrlSoftUpdateService;
import com.ctrlsoft.ctrlsoftupdateservice.util.CommonUtil;
import com.ctrlsoft.ctrlsoftupdateservice.util.CtrlSoftUpdateUtil;
import com.ctrlsoft.ctrlsoftupdateservice.view.UpdateDialog;

public class UpdateDialogActivity extends Activity {
	boolean isShowLog = false;// 默认不显示日志
	UpdateBean ub;
	boolean isForced = false;// 是否强制更新,true为强制更新
	UpdateDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerBoradcastReceiver();
		//
		ub = (UpdateBean) getIntent().getSerializableExtra("ub");
		if (ub.getForceUpdate().equalsIgnoreCase("0")) {
			isForced = true;
		} else {
			isForced = false;
		}
		System.out.println("UpdateDialogActivity.onCreate");
		//
		try {
			dialog = new UpdateDialog(this, ub, isForced);
			if (isForced) {
				dialog.setMessage("有新版本发布\n版本号:" + ub.getVersionName()
						+ "\n发布时间:"
						+ CommonUtil.getDateFormDateTime(ub.getUpdateTime())
						+ "\n本次更新内容:\n" + ub.getMemo().replace("**", "\n"));
			} else {
				dialog.setMessage("有新版本发布\n版本号:" + ub.getVersionName()
						+ "\n发布时间:"
						+ CommonUtil.getDateFormDateTime(ub.getUpdateTime())
						+ "\n本次更新内容:\n" + ub.getMemo().replace("**", "\n")
						+ "\n是否下载更新包？");
			}

			dialog.setPositiveButton(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// CtrlSoftUpdateUtil.startDownUpdateAPKService(
					// getBaseContext(), isShowLog, ub);
					if (CtrlSoftDownApkService
							.isRunning(UpdateDialogActivity.this)) {
						Toast.makeText(UpdateDialogActivity.this,
								"正在下载中，请稍后...", Toast.LENGTH_SHORT).show();
					} else {
						CtrlSoftUpdateUtil.startDownUpdateAPKService(
								getBaseContext(), isShowLog, ub);
						// Intent intent = new Intent(MainActivity.this,
						// CtrlSoftDownApkService.class);
						// intent.putExtra("path", path);
						// startService(intent);
						// rogueUpdateDialog.setProgressBarPrecent(1);
					}
					if (!isForced) {// 强制更新不让dialog消失
						dialog.dismiss();
						finish();
						overridePendingTransition(0, 0);
					}
				}
			});
			dialog.setNegativeButton(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					finish();
					overridePendingTransition(0, 0);
				}
			});
			WindowManager m = getWindowManager();
			WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
			Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
			Window dialogWindow = dialog.getWindow();
			p.width = (int) (d.getWidth() * 0.80); // 宽度设置为屏幕的0.8
			dialogWindow.setAttributes(p);
			dialog.setCanceledOnTouchOutside(false); // 点击外面区域不会让dialog消失
			dialog.setCancelable(false);
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
									 KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_SEARCH) {
						return true;
					} else {
						return true; // 默认返回 false，这里false不能屏蔽返回键，改成true就可以了
					}
				}
			});
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setMessage(
		// "有新版本发布\n版本号:"
		// + ub.getVersonName()
		// + "\n发布时间:"
		// + CommonUtil.getDateFormDateTime(ub.getUpdateTime())
		// + "\n本次更新内容:\n"
		// + ub.getUpdateContent().replace("**", "\n")
		// + "\n是否下载更新包？")
		// .setPositiveButton("确定",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// CtrlSoftUpdateUtil
		// .startDownUpdateAPKService(
		// getBaseContext(),
		// isShowLog, ub);
		// }
		// })
		// .setNegativeButton("取消",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// finish();
		// overridePendingTransition(0, 0);
		// }
		// });
		// AlertDialog ad = builder.create();
		// System.out.println("builder.create()");
		// //
		// ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
		// // //系统中关机对话框就是这个属性
		// //
		// ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// ad.setCanceledOnTouchOutside(false); // 点击外面区域不会让dialog消失
		// ad.show();
		// System.out.println("ad.show()");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			Intent server = new Intent(UpdateDialogActivity.this,
					CtrlSoftUpdateService.class);
			stopService(server);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			unregisterReceiver(mBroadcastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("KEYCODE_BACK");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	// =============广播
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(getPackageName()
					+ ".ctrlsoftdownapkservice.dialog.precent")) {
				try {
					if (dialog != null) {
						dialog.setProgressBarPrecent(intent.getIntExtra(
								"precent", 0));
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(getPackageName()
				+ ".ctrlsoftdownapkservice.dialog.precent");// 升级dialog更新进度条
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
}
