package com.lxb.view.calendar;

/**
 * 把公历时间处理成农历时间
 */
public class NumberHelper {
	public static String LeftPad_Tow_Zero(int str) {
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		return format.format(str);
	}
}
