package com.lxb.view.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.ArrayWheelAdapter;
import com.lxb.view.wheel.util.NumericWheelAdapter;
import com.lxb.view.wheel.util.OnWheelChangedListener;
import com.lxb.view.wheel.util.WheelView;

import java.util.Calendar;

public class TimeStartToEndWheelView
{
	// 当前时间
	private int hourStartDate;
	private int minuteStartDate;
	private int hourEndDate;
	private int minuteEndDate;

	private Context context;
	// 日期控件对象
	private WheelView hourStart;
	private WheelView minuteStart;
	private WheelView text;
	private WheelView hourEnd;
	private WheelView minuteEnd;

	private int textSize = 16;

	/******** 当前时间参数 *********/
	// private int now_hour;
	// private int now_am;

	private AlertDialog dialog;
	public View okButton, cancelButton;

	public TimeStartToEndWheelView(Context context)
	{
		final Calendar c = Calendar.getInstance();
		this.hourStartDate = c.get(Calendar.HOUR_OF_DAY);
		this.minuteStartDate = c.get(Calendar.MINUTE);
		this.hourEndDate = c.get(Calendar.HOUR_OF_DAY);
		this.minuteEndDate = c.get(Calendar.MINUTE);
		this.context = context;
		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);
	}

	/*************** 设置默认时间 *************/
	public void setDefaultTime(int hourStartDate, int minuteStartDate, int hourEndDate, int minuteEndDate)
	{
		this.hourStartDate = hourStartDate;
		this.minuteStartDate = minuteStartDate;
		this.hourEndDate = hourEndDate;
		this.minuteEndDate = minuteEndDate;
	}

	/********* 创建时间对话框 ******************/
	public void createTimeDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogTime = inflater.inflate(R.layout.wheel_time_starttoend_layout, null);
		hourStart = (WheelView) dialogTime.findViewById(R.id.hour_start);
		minuteStart = (WheelView) dialogTime.findViewById(R.id.minute_start);
		text = (WheelView) dialogTime.findViewById(R.id.text);
		hourEnd = (WheelView) dialogTime.findViewById(R.id.hour_end);
		minuteEnd = (WheelView) dialogTime.findViewById(R.id.minute_end);

		OnWheelChangedListener listener = new OnWheelChangedListener()
		{
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				updateTime();
			}
		};

		OnWheelChangedListener listenerEnd = new OnWheelChangedListener()
		{
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				updateTimeEnd();
			}
		};

		hourStart.setAdapter(new NumericWheelAdapter(0, 23));
		hourStart.setCurrentItem(this.hourStartDate);
		hourStart.addChangingListener(listener);
		hourStart.setCyclic(true);
		hourStart.TEXT_SIZE = this.textSize;
		hourStart.setLabel("时");

		minuteStart.setAdapter(new NumericWheelAdapter(0, 59));
		minuteStart.setCurrentItem(this.minuteStartDate);
		minuteStart.addChangingListener(listener);
		minuteStart.setCyclic(true);
		minuteStart.TEXT_SIZE = this.textSize;
		minuteStart.setLabel("分");

		String textString[] =
		{ "至" };
		text.setAdapter(new ArrayWheelAdapter<String>(textString, 1));
		text.setCurrentItem(0);
		text.TEXT_SIZE = this.textSize;

		hourEnd.setAdapter(new NumericWheelAdapter(0, 23));
		hourEnd.setCurrentItem(this.hourEndDate);
		hourEnd.addChangingListener(listenerEnd);
		hourEnd.setCyclic(true);
		hourEnd.TEXT_SIZE = this.textSize;
		hourEnd.setLabel("时");

		minuteEnd.setAdapter(new NumericWheelAdapter(0, 59));
		minuteEnd.setCurrentItem(this.minuteEndDate);
		minuteEnd.addChangingListener(listenerEnd);
		minuteEnd.setCyclic(true);
		minuteEnd.TEXT_SIZE = this.textSize;
		minuteEnd.setLabel("分");

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
		// // 处理不同JDK版本左右相反BUG
		// if (android.os.Build.VERSION.SDK_INT < 14) {
		// cancelButton
		// .setBackgroundResource(R.drawable.dialog_button_left_bg_selector);
		// okButton.setBackgroundResource(R.drawable.dialog_button_right_bg_selector);
		// }
		cancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
	}

	/************ 获得第几小时(开始时间) ***************/
	public int getHourStartCurrentItem()
	{
		return hourStart.getCurrentItem();
	}

	/************ 获得第几分钟(开始时间) ***************/
	public int getMinuteStartCurrentItem()
	{
		return minuteStart.getCurrentItem();
	}

	/************ 获得第几小时(结束时间) ***************/
	public int getHourEndCurrentItem()
	{
		return hourEnd.getCurrentItem();
	}

	/************ 获得第几分钟(结束时间) ***************/
	public int getMinuteEndCurrentItem()
	{
		return minuteEnd.getCurrentItem();
	}

	/************ 设置字体大小 ***************/
	public void setTextSizes(int size)
	{
		this.textSize = size;
	}

	/************** 同步修改时间(开始滚动) *********************/
	void updateTime()
	{
		// System.out.println("滚动="+getHourStartCurrentItem()+"    "+getHourEndCurrentItem());
		if (getHourStartCurrentItem() > getHourEndCurrentItem())
		{
			hourEnd.setAdapter(new NumericWheelAdapter(0, 23));
			hourEnd.setCurrentItem(hourStart.getCurrentItem());
		}
		else
		{
			if (getMinuteStartCurrentItem() > getMinuteEndCurrentItem())
			{
				minuteEnd.setAdapter(new NumericWheelAdapter(0, 59));
				minuteEnd.setCurrentItem(minuteStart.getCurrentItem());
			}
		}
	}

	/************** 同步修改时间(结束滚动) *********************/
	void updateTimeEnd()
	{
		// System.out.println("滚动="+getHourStartCurrentItem()+"    "+getHourEndCurrentItem());
		if (getHourStartCurrentItem() > getHourEndCurrentItem())
		{
			hourStart.setAdapter(new NumericWheelAdapter(0, 23));
			hourStart.setCurrentItem(hourEnd.getCurrentItem());
		}
		else
		{
			if (getMinuteStartCurrentItem() > getMinuteEndCurrentItem())
			{
				minuteStart.setAdapter(new NumericWheelAdapter(0, 59));
				minuteStart.setCurrentItem(minuteEnd.getCurrentItem());
			}
		}
	}

	/************ 获得当前时间(时——分)串(开始时间) ***************/
	public String getHourMinuteStartString(int style)
	{
		String times_string;
		if (style == 0)
		{
			times_string = ((getHourStartCurrentItem() < 10) ? "0" + getHourStartCurrentItem() : getHourStartCurrentItem()) + ":" + ((getMinuteStartCurrentItem() < 10) ? "0" + getMinuteStartCurrentItem() : getMinuteStartCurrentItem());
		}
		else
		{
			times_string = ((getHourStartCurrentItem() < 10) ? "0" + getHourStartCurrentItem() : getHourStartCurrentItem()) + "时" + ((getMinuteStartCurrentItem() < 10) ? "0" + getMinuteStartCurrentItem() : getMinuteStartCurrentItem()) + "分";
		}
		return times_string;
	}

	/************ 获得当前时间(时——分)串(结束时间) ***************/
	public String getHourMinuteEndString(int style)
	{
		String times_string;
		if (style == 0)
		{
			times_string = ((getHourEndCurrentItem() < 10) ? "0" + getHourEndCurrentItem() : getHourEndCurrentItem()) + ":" + ((getMinuteEndCurrentItem() < 10) ? "0" + getMinuteEndCurrentItem() : getMinuteEndCurrentItem());
		}
		else
		{
			times_string = ((getHourEndCurrentItem() < 10) ? "0" + getHourEndCurrentItem() : getHourEndCurrentItem()) + "时" + ((getMinuteEndCurrentItem() < 10) ? "0" + getMinuteEndCurrentItem() : getMinuteEndCurrentItem()) + "分";
		}
		return times_string;
	}

	public void finish()
	{
		dialog.dismiss();
	}

}
