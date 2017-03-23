package com.lxb.view.calendar;

import android.content.Context;
import android.content.res.Resources;

public class ViewUtil {

	/**
	 * 获取屏幕的宽度
	 * 
	 * @param context
	 * @return
	 */
	public int getScreenWidth(Context context) {
		Resources res = context.getResources();
		return res.getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public int getScreenHeight(Context context) {
		Resources res = context.getResources();
		return res.getDisplayMetrics().heightPixels;
	}

	/**
	 * 描述：根据分辨率获得字体大小.
	 * 
	 * @param screenWidth
	 *            the screen width
	 * @param screenHeight
	 *            the screen height
	 * @param textSize
	 *            the text size
	 * @return the int
	 */
	public static int resizeTextSize(int screenWidth, int screenHeight,
			int textSize) {
		float ratio = 1;
		try {
			float ratioWidth = (float) screenWidth / 480;
			float ratioHeight = (float) screenHeight / 800;
			ratio = Math.min(ratioWidth, ratioHeight);
		} catch (Exception e) {
		}
		return Math.round(textSize * ratio);
	}

	/**
	 * 
	 * 描述：dip转换为px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 * @throws
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return Math.round(dipValue * scale);
	}

	/**
	 * 
	 * 描述：px转换为dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 * @throws
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return Math.round(pxValue / scale);
	}

	/**
	 * 
	 * 描述：px转换为sp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 * @throws
	 */
	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return Math.round(pxValue / scale);
	}

	/**
	 * 
	 * 描述：sp转换为px
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 * @throws
	 */
	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return Math.round(spValue * scale);
	}
}
