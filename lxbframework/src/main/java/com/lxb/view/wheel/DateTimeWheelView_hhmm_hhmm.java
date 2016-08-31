package com.lxb.view.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.NumericWheelAdapter;
import com.lxb.view.wheel.util.WheelView;

/**
 * @COMPANY:sunnyTech
 * @CLASS:DateTimeWheelView
 * @DESCRIPTION:日/时/分
 * @AUTHOR:Sunny
 * @VERSION:v1.0
 * @DATE:2014-8-22 下午3:58:35
 */
public class DateTimeWheelView_hhmm_hhmm {
	// 当前日期时间
	private int staHourDate;
	private int staMinuteDate;

	private int endHourDate;
	private int endMinuteDate;

	private Context context;
	// 日期时间控件对象
	private WheelView staHour;
	private WheelView staMinute;

	private WheelView endHour;
	private WheelView endMinute;

	private int textSize = 16;

	private AlertDialog dialog;
	public View okButton, cancelButton;

	public DateTimeWheelView_hhmm_hhmm(Context context) {
		// final Calendar c = Calendar.getInstance();
		// this.staHourDate = c.get(Calendar.HOUR_OF_DAY);
		// this.staMinuteDate = c.get(Calendar.MINUTE);
		// this.endHourDate = c.get(Calendar.HOUR_OF_DAY);
		// this.endMinuteDate = c.get(Calendar.MINUTE);
		this.staHourDate = 8;
		this.staMinuteDate = 0;
		this.endHourDate = 17;
		this.endMinuteDate = 0;
		this.context = context;
		textSize = context.getResources().getDimensionPixelOffset(
				R.dimen.sdafwerwe);
	}

	/*************** 设置默认时间 *************/
	public void setDefaultTime(int staHourDate, int staMinuteDate,
			int endHourDate, int endMinuteDate) {
		this.staHourDate = staHourDate;
		this.staMinuteDate = staMinuteDate;
		this.endHourDate = endHourDate;
		this.endMinuteDate = endMinuteDate;
	}

	/********* 创建时间对话框 ******************/
	public void createTimeDialog() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogTime = inflater.inflate(
				R.layout.wheel_datetime_hhmm_hhmm_layout, null);
		staHour = (WheelView) dialogTime.findViewById(R.id.stahour);
		staMinute = (WheelView) dialogTime.findViewById(R.id.staminute);

		endHour = (WheelView) dialogTime.findViewById(R.id.endhour);
		endMinute = (WheelView) dialogTime.findViewById(R.id.endminute);

		staHour.setAdapter(new NumericWheelAdapter(0, 23));
		staHour.setCurrentItem(this.staHourDate);
		staHour.setCyclic(true);
		staHour.TEXT_SIZE = this.textSize;
		staHour.setLabel("时");
		staMinute.setAdapter(new NumericWheelAdapter(0, 59));
		staMinute.setCurrentItem(this.staMinuteDate);
		staMinute.setCyclic(true);
		staMinute.TEXT_SIZE = this.textSize;
		staMinute.setLabel("分");

		endHour.setAdapter(new NumericWheelAdapter(0, 23));
		endHour.setCurrentItem(this.endHourDate);
		endHour.setCyclic(true);
		endHour.TEXT_SIZE = this.textSize;
		endHour.setLabel("时");
		endMinute.setAdapter(new NumericWheelAdapter(0, 59));
		endMinute.setCurrentItem(this.endMinuteDate);
		endMinute.setCyclic(true);
		endMinute.TEXT_SIZE = this.textSize;
		endMinute.setLabel("分");

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(dialogTime);
		// dialog.getWindow().findViewById(R.id.TitleButton)
		// .setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });

		okButton = dialog.getWindow().findViewById(R.id.OkButton);
		cancelButton = dialog.getWindow().findViewById(R.id.CancelButton);
		// 处理不同JDK版本左右相反BUG
		// if (android.os.Build.VERSION.SDK_INT < 14) {
		// cancelButton
		// .setBackgroundResource(R.drawable.dialog_button_left_bg_selector);
		// okButton.setBackgroundResource(R.drawable.dialog_button_right_bg_selector);
		// }
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/************ 获得开始第几小时 ***************/
	public int getStaHourCurrentItem() {
		return staHour.getCurrentItem();
	}

	public int getEndHourCurrentItem() {
		return endHour.getCurrentItem();
	}

	/************ 获得第几分钟 ***************/
	public int getStaMinuteCurrentItem() {
		return staMinute.getCurrentItem();
	}

	public int getEndMinuteCurrentItem() {
		return endMinute.getCurrentItem();
	}

	/************ 设置字体大小 ***************/
	public void setTextSizes(int size) {
		this.textSize = size;
	}

	/************ 获得当前时间(日——时——分)串 ***************/
	public String getHourMinuteString() {
		String times_string = ""
				+ getStaHourCurrentItem()
				+ ":"
				+ ((getStaMinuteCurrentItem() < 10) ? "0"
						+ getStaMinuteCurrentItem() : getStaMinuteCurrentItem())
				+ "~"
				+ getEndHourCurrentItem()
				+ ":"
				+ ((getEndMinuteCurrentItem() < 10) ? "0"
						+ getEndMinuteCurrentItem() : getEndMinuteCurrentItem());
		return times_string;
	}

	public void finish() {
		dialog.dismiss();
	}

}
