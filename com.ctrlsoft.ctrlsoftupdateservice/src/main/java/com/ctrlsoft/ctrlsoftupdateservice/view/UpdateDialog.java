package com.ctrlsoft.ctrlsoftupdateservice.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ctrlsoft.ctrlsoftupdateservice.bean.UpdateBean;
import com.ctrlsoft.ctrlsoftupdateservice.util.CPResourceUtil;

public class UpdateDialog extends Dialog {

	Context context;
	Dialog ad;
	//
	TextView ctrlsoft_update_content;// 内容
	CheckBox ctrlsoft_update_id_check;// 忽略更新
	Button ctrlsoft_update_id_ok;// 立即更新
	Button ctrlsoft_update_id_cancel;// 取消更新
	ProgressBar progressBar1;
	LinearLayout buttonLayout;

	SharedPreferences sp;
	UpdateBean ub;
	boolean isForced = false;// 是否强制更新,true为强制更新

	//
	public UpdateDialog(Context context, UpdateBean ub, boolean isForced) {
		super(context, CPResourceUtil.getStyleId(context,
				"ctrlsoft_update_dialog_theme"));
		// TODO Auto-generated constructor stub
		this.context = context;
		this.ub = ub;
		this.isForced = isForced;
		setTitle(null);
		sp = context.getSharedPreferences("ctrlsoft_update_config",
				Activity.MODE_PRIVATE);
		//
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(
				CPResourceUtil.getLayoutId(context, "ctrlsoftupdatedialog"),
				null);
		// 重点在于这句话，把背景的透明度设为完全透明，就看不到后面那个稍大一点的视图了。
		view.getBackground().setAlpha(0);
		//
		ctrlsoft_update_content = (TextView) view.findViewById(CPResourceUtil
				.getId(context, "ctrlsoft_update_content"));
		//
		ctrlsoft_update_id_ok = (Button) view.findViewById(CPResourceUtil
				.getId(context, "ctrlsoft_update_id_ok"));
		//
		ctrlsoft_update_id_cancel = (Button) view.findViewById(CPResourceUtil
				.getId(context, "ctrlsoft_update_id_cancel"));
		//
		ctrlsoft_update_id_check = (CheckBox) view.findViewById(CPResourceUtil
				.getId(context, "ctrlsoft_update_id_check"));
		//
		progressBar1 = (ProgressBar) view.findViewById(CPResourceUtil.getId(
				context, "progressBar1"));
		//
		buttonLayout = (LinearLayout) view.findViewById(CPResourceUtil.getId(
				context, "buttonLayout"));
		//
		ctrlsoft_update_id_check
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						// TODO Auto-generated method stub
						setIgnore(isChecked);
					}
				});
		if (isForced) {
			ctrlsoft_update_id_check.setVisibility(View.GONE);
			ctrlsoft_update_id_cancel.setVisibility(View.GONE);
		}
		setContentView(view);
	}

	public void setIgnore(boolean ignore) {
		Editor editor = sp.edit();
		// editor.putBoolean("ignore", ignore);
		if (ignore) {
			editor.putString("ignore", ub.getVersionCode());
		} else {
			editor.putString("ignore", "");
		}
		editor.commit();
	}

	public void setMessage(String message) {
		ctrlsoft_update_content.setText(message);
	}

	// 立即更新按钮
	public void setPositiveButton(final View.OnClickListener listener) {
		ctrlsoft_update_id_ok.setOnClickListener(listener);
	}

	// 忽略更新按钮
	public void setNegativeButton(final View.OnClickListener listener) {
		ctrlsoft_update_id_cancel.setOnClickListener(listener);
	}

	// 强制更新进度条
	public void setProgressBarPrecent(int progress) {
		if (progressBar1 != null) {
			progressBar1.setVisibility(View.VISIBLE);
			progressBar1.setProgress(progress);
			buttonLayout.setVisibility(View.GONE);
		}
	}

	public int getProgressBarPrecent() {
		if (progressBar1 != null) {
			return progressBar1.getProgress();
		} else {
			return 0;
		}
	}
}
