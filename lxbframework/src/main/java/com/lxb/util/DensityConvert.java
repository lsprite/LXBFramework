package com.lxb.util;

import android.content.Context;

/**
 * @COMPANY:sunnyTech
 * @CLASS:DensityConvert
 * @DESCRIPTION:Density(密度)
 * @AUTHOR:Sunny
 * @VERSION:v1.0
 * @DATE:2014-10-21 下午4:50:34
 */
public class DensityConvert {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
