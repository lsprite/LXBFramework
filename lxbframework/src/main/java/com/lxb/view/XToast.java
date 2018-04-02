package com.lxb.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lxb.framework.R;

public class XToast {
	private Context mContext;
	private WindowManager wm;
	private int mDuration;
	private View mNextView;
	public static final int LENGTH_SHORT = 3000;
	public static final int LENGTH_LONG = 8000;
	private static Toast toast;

	public XToast(Context context) {
		mContext = context.getApplicationContext();
		wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	}

	public static XToast makeText(Context context, CharSequence text, int duration) {
		toast = Toast.makeText(context, text, duration);
		XToast result = new XToast(context);
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ll.setBackgroundResource(R.drawable.bg_toast);
		TextView tv = new TextView(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(dip2px(context, 15), dip2px(context, 10), dip2px(context, 15), dip2px(context, 10));
		tv.setLayoutParams(params);
		tv.setText(text);
		tv.setTextColor(Color.parseColor("#ffffff"));
		ll.addView(tv);
		//
		LinearLayout ll2 = new LinearLayout(context);
		ll2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ll2.setBackgroundResource(R.drawable.bg_toast);
		TextView tv2 = new TextView(context);
		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.setMargins(dip2px(context, 15), dip2px(context, 10), dip2px(context, 15), dip2px(context, 10));
		tv2.setLayoutParams(params2);
		tv2.setText(text);
		tv2.setTextColor(Color.parseColor("#ffffff"));
		ll2.addView(tv2);
		toast.setView(ll2);
		//
		result.mNextView = (View) ll;
		result.mDuration = duration;
		return result;
	}

	public static XToast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId), duration);
	}

	public void show() {
		try {
			if (mNextView != null) {
				WindowManager.LayoutParams params = new WindowManager.LayoutParams();
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				params.format = PixelFormat.TRANSLUCENT;
				params.windowAnimations = R.style.Animation_Toast;
				params.y = dip2px(mContext, 64);
				params.type = WindowManager.LayoutParams.TYPE_PHONE;
				wm.addView(mNextView, params);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (mNextView != null) {
							wm.removeView(mNextView);
							mNextView = null;
							wm = null;
						}
					}
				}, mDuration);
			}
			if (toast != null) {
				toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, dip2px(mContext, 64));
				toast.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}