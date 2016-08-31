package com.lxb.view.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.NumericWheelAdapter;
import com.lxb.view.wheel.util.WheelView;

import java.util.Calendar;

/**
 * @COMPANY:sunnyTech
 * @CLASS:DateTimeWheelView
 * @DESCRIPTION:日/时/分	
 * @AUTHOR:Sunny
 * @VERSION:v1.0
 * @DATE:2014-8-22 下午3:58:35
 */
public class DateTimeWheelView_ddhhmm {
	// 当前日期时间
	private int dayDate;
	private int hourDate;
	private int minuteDate;

	private Context context;
	// 日期时间控件对象
	private WheelView day;
	private WheelView hour;
	private WheelView minute;

	private int textSize = 16;

	private AlertDialog dialog;
	public View okButton, cancelButton;

	public DateTimeWheelView_ddhhmm(Context context) {
		final Calendar c = Calendar.getInstance();
		this.dayDate = c.get(Calendar.DATE);
		this.hourDate = c.get(Calendar.HOUR_OF_DAY);
		this.minuteDate = c.get(Calendar.MINUTE);
		this.context = context;
		textSize=context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);
	}

	/*************** 设置默认时间 *************/
	public void setDefaultTime(int dayDate, int hourDate, int minuteDate) {
		this.dayDate = dayDate;
		this.hourDate = hourDate;
		this.minuteDate = minuteDate;
	}

	/********* 创建时间对话框 ******************/
	public void createTimeDialog() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogTime = inflater.inflate(
				R.layout.wheel_datetime_ddhhmm_layout, null);
		day = (WheelView) dialogTime.findViewById(R.id.day);
		hour = (WheelView) dialogTime.findViewById(R.id.hour);
		minute = (WheelView) dialogTime.findViewById(R.id.minute);

		day.setAdapter(new NumericWheelAdapter(0, 30));
		day.setCurrentItem(this.dayDate);
		day.setCyclic(true);
		day.TEXT_SIZE = this.textSize;
		day.setLabel("日");

		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setCurrentItem(this.hourDate);
		hour.setCyclic(true);
		hour.TEXT_SIZE = this.textSize;
		hour.setLabel("时");

		minute.setAdapter(new NumericWheelAdapter(0, 59));
		minute.setCurrentItem(this.minuteDate);
		minute.setCyclic(true);
		minute.TEXT_SIZE = this.textSize;
		minute.setLabel("分");

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(dialogTime);
//		dialog.getWindow().findViewById(R.id.TitleButton)
//				.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//					}
//				});

		okButton = dialog.getWindow().findViewById(R.id.OkButton);
		cancelButton = dialog.getWindow().findViewById(R.id.CancelButton);
		// 处理不同JDK版本左右相反BUG
//		if (android.os.Build.VERSION.SDK_INT < 14) {
//			cancelButton
//					.setBackgroundResource(R.drawable.dialog_button_left_bg_selector);
//			okButton.setBackgroundResource(R.drawable.dialog_button_right_bg_selector);
//		}
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/************ 获得第几天 ***************/

	public int getDayCurrentItem() {
		return day.getCurrentItem();
	}

	/************ 获得第几小时 ***************/
	public int getHourCurrentItem() {
		return hour.getCurrentItem();
	}

	/************ 获得第几分钟 ***************/
	public int getMinuteCurrentItem() {
		return minute.getCurrentItem();
	}

	/************ 设置字体大小 ***************/
	public void setTextSizes(int size) {
		this.textSize = size;
	}

	/************ 获得当前时间(日——时——分)串 ***************/
	public String getDayHourMinuteString() {
		String times_string = ""
				+ getDayCurrentItem()
				+ "-"
				+ getHourCurrentItem()
				+ ":"
				+ ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem()
						: getMinuteCurrentItem());
		return times_string;
	}

	public void finish() {
		dialog.dismiss();
	}

}
