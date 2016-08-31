package com.lxb.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lxb.framework.R;

public class MyAlertDialog extends Dialog {

	Context context;
	Dialog ad;
	TextView titleView;
	TextView messageView;
	LinearLayout ll_progress;
	ProgressBar progressBar1;
	TextView progressBarTV;
	LinearLayout buttonLayout;
	ColorStateList csl;
	ColorStateList cslCannel;

	public MyAlertDialog(Context context) {
		super(context, R.style.custom_dialog_theme);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.custom_dialog_view, null);
		// ad = new AlertDialog.Builder(new ContextThemeWrapper(context,
		// R.style.custom_dialog_theme)).setView(view).create();
		// Replace the source alert dialog.
		// Window window = ad.getWindow();
		// window.setContentView(R.layout.custom_dialog_view);
		titleView = (TextView) view.findViewById(R.id.title);
		messageView = (TextView) view.findViewById(R.id.message);
		ll_progress = (LinearLayout) view.findViewById(R.id.ll_progress);
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		progressBarTV = (TextView) view.findViewById(R.id.progressBarTV);
		buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
		try {
			csl = (ColorStateList) context.getResources().getColorStateList(
					R.color.color_dialog_btn);
			cslCannel = (ColorStateList) context.getResources()
					.getColorStateList(R.color.color_dialog_cannel_btn);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// XmlResourceParser xpp = Resources.getSystem().getXml(
		// R.color.color_dialog_btn);
		// try {
		// csl = ColorStateList.createFromXml(context.getResources(), xpp);
		// System.out.println("csl:" + csl);
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

		setContentView(view);
	}

	// public MyAlertDialog(Context context) {
	// this.context = context;
	// LayoutInflater inflater = LayoutInflater.from(context);
	// View view = inflater.inflate(R.layout.custom_dialog_view, null);
	// // ad = new AlertDialog.Builder(new ContextThemeWrapper(context,
	// // R.style.custom_dialog_theme)).setView(view).create();
	// ad = new Dialog(context, R.style.custom_dialog_theme);
	// ad
	// // Replace the source alert dialog.
	// // Window window = ad.getWindow();
	// // window.setContentView(R.layout.custom_dialog_view);
	// titleView = (TextView) view.findViewById(R.id.title);
	// messageView = (TextView) view.findViewById(R.id.message);
	// buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
	// }

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	public void setProgressBarPrecent(int progress) {
		if (progressBar1 != null) {
			ll_progress.setVisibility(View.VISIBLE);
			progressBar1.setProgress(progress);
			progressBarTV.setText("下载" + progress + "%");
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

	/**
	 * Button style
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1f;
		// params.setMargins(DensityConvert.dip2px(context, 5), 0,
		// DensityConvert.dip2px(context, 20), 0);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.dialog_bg_bottom_right_shape);
		button.setText(text);
		button.setTextColor(csl);
		button.setTextSize(20);
		button.setOnClickListener(listener);
		buttonLayout.addView(button);
	}

	/**
	 * Button style
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1f;
		// params.setMargins(DensityConvert.dip2px(context, 20), 0,
		// DensityConvert.dip2px(context, 5), 0);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.dialog_bg_bottom_left_shape);
		button.setText(text);
		button.setTextColor(cslCannel);
		button.setTextSize(20);
		button.setOnClickListener(listener);
		buttonLayout.addView(button);

		// if (buttonLayout.getChildCount() > 0) {
		// params.setMargins(20, 0, 0, 0);
		// button.setLayoutParams(params);
		// buttonLayout.addView(button, 1);
		// } else {
		// button.setLayoutParams(params);
		// buttonLayout.addView(button);
		// }
	}

	/**
	 * Button style
	 * 
	 * @param text
	 * @param listener
	 */
	public void setOnlyOneButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1f;
		// params.setMargins(DensityConvert.dip2px(context, 10), 0,
		// DensityConvert.dip2px(context, 10), 0);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.dialog_bg_down_shape);
		button.setText(text);
		button.setTextColor(csl);
		button.setTextSize(20);
		button.setOnClickListener(listener);
		buttonLayout.addView(button);
	}
}