package com.lxb.view.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.ArrayWheelAdapter;
import com.lxb.view.wheel.util.NumericWheelAdapter;
import com.lxb.view.wheel.util.OnWheelChangedListener;
import com.lxb.view.wheel.util.WheelView;

import java.util.Calendar;

public class TimeWheelView
{
	//标题
	private TextView titleTextView;
	private String titleString;
	
	// 当前时间
	private int hourDate;
	private int minuteDate;
	private int secondDate;

	private Context context;
	// 日期控件对象
	private WheelView am;
	private WheelView hour;
	private WheelView minute;
	private WheelView second;

	private int textSize = 16;

	String ams[] = new String[]
	{ "AM", "PM" };
	/******* 是否显示的参数 ***********/
	private boolean show_am = true;
	private boolean show_am_second = true;
	private boolean show_second = true;

	/******** 当前时间参数 *********/
	private int now_hour;
	private int now_am;

	private AlertDialog dialog;
	public View okButton, cancelButton;

	public TimeWheelView(Context context)
	{
		final Calendar c = Calendar.getInstance();
		this.hourDate = c.get(Calendar.HOUR_OF_DAY);
		this.minuteDate = c.get(Calendar.MINUTE);
		this.secondDate = c.get(Calendar.SECOND);
		this.context = context;
		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);
	}
	
	public TimeWheelView(Context context,String _titleString)
	{
		final Calendar c = Calendar.getInstance();
		this.hourDate = c.get(Calendar.HOUR_OF_DAY);
		this.minuteDate = c.get(Calendar.MINUTE);
		this.secondDate = c.get(Calendar.SECOND);
		this.context = context;
		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);
		titleString=_titleString;
	}

	/*************** 设置默认时间 *************/
	public void setDefaultTime(int hourDate, int minuteDate, int secondDate)
	{
		this.hourDate = hourDate;
		this.minuteDate = minuteDate;
		this.secondDate = secondDate;
	}

	public void setDefaultTime(int hourDate, int minuteDate)
	{
		this.hourDate = hourDate;
		this.minuteDate = minuteDate;
	}

	/********* 创建时间对话框 ******************/
	public void createTimeDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogTime = inflater.inflate(R.layout.wheel_time_layout, null);
		am = (WheelView) dialogTime.findViewById(R.id.am);
		hour = (WheelView) dialogTime.findViewById(R.id.hour);
		minute = (WheelView) dialogTime.findViewById(R.id.minute);
		second = (WheelView) dialogTime.findViewById(R.id.second);
		if (titleString!=null)
		{
			titleTextView=(TextView)dialogTime.findViewById(R.id.Title);
			titleTextView.setText(titleString);
		}
		
		OnWheelChangedListener listener = new OnWheelChangedListener()
		{
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				updateAms();
			}
		};

		hour.setAdapter(new NumericWheelAdapter(0, 23, "%1$,02d"));
		hour.setCurrentItem(this.hourDate);
		hour.addChangingListener(listener);
		hour.setCyclic(true);
		hour.TEXT_SIZE = this.textSize;
		//或者String value = application.getString(R.string.XXX);  
		hour.setLabel(context.getString(R.string.Hour));

		now_am = this.hourDate / 12;
		am.setAdapter(new ArrayWheelAdapter<String>(ams, 2));
		am.setCurrentItem(now_am);
		am.TEXT_SIZE = this.textSize;
		am.addChangingListener(listener);

		now_hour = this.hourDate;

		minute.setAdapter(new NumericWheelAdapter(0, 59, "%1$,02d"));
		minute.setCurrentItem(this.minuteDate);
		minute.setCyclic(true);
		minute.TEXT_SIZE = this.textSize;
		minute.setLabel(context.getString(R.string.Minute));

		second.setAdapter(new NumericWheelAdapter(0, 59, "%1$,02d"));
		second.setCurrentItem(this.secondDate);
		second.setCyclic(true);
		second.TEXT_SIZE = this.textSize;
		second.setLabel(context.getString(R.string.Second));

		if (!this.show_am)
		{
			am.setVisibility(View.GONE);
		}
		else if (!this.show_am_second)
		{
			am.setVisibility(View.GONE);
			second.setVisibility(View.GONE);
		}
		else if (!this.show_second)
		{
			second.setVisibility(View.GONE);
		}

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

	/************ 获得第几小时 ***************/
	public int getHourCurrentItem()
	{
		return hour.getCurrentItem();
	}

	/************ 获得第几分钟 ***************/
	public int getMinuteCurrentItem()
	{
		return minute.getCurrentItem();
	}

	/************ 获得第几秒 ***************/
	public int getSecondCurrentItem()
	{
		return second.getCurrentItem();
	}

	/************ 获得上午或下午 ***************/
	public int getAmCurrentItem()
	{
		return am.getCurrentItem();
	}

	/************ 设置字体大小 ***************/
	public void setTextSizes(int size)
	{
		this.textSize = size;
	}

	/************** 同步修改时间 *********************/
	void updateAms()
	{
		if (now_hour != getHourCurrentItem())
		{

			int cur = 0;
			if (getHourCurrentItem() < 12)
			{
				cur = 0;
			}
			else
				cur = 1;
			if (now_am != cur)
			{
				now_am = cur;
			}
			am.setAdapter(new ArrayWheelAdapter<String>(ams, 2));
			am.setCurrentItem(cur);
			now_hour = getHourCurrentItem();
		}
		if (now_am != getAmCurrentItem())
		{
			hour.setAdapter(new NumericWheelAdapter(0, 23));
			if (getAmCurrentItem() == 0)
			{
				hour.setCurrentItem(0);
			}
			else
			{
				hour.setCurrentItem(12);
			}
			now_am = getAmCurrentItem();
		}
	}

	/************ 设置上下午视图是否可见 ***************/
	public void setAm(boolean show_am)
	{
		this.show_am = show_am;
	}

	/************ 设置上下午视图、秒视图是否可见 ***************/
	public void setAmSecond(boolean show_am_second)
	{
		this.show_am_second = show_am_second;
	}

	/************ 设置秒视图是否可见 ***************/
	public void setSecond(boolean show_second)
	{
		this.show_second = show_second;
	}

	/************ 获得当前时间(时——分——秒)串 ***************/
	public String getHourMinuteSecondString(int style)
	{
		String times_string;
		if (style == 0)
		{
			times_string = ((getHourCurrentItem() < 10) ? "0" + getHourCurrentItem() : getHourCurrentItem()) + ":" + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem() : getMinuteCurrentItem()) + ":" + ((getSecondCurrentItem() < 10) ? "0" + getSecondCurrentItem() : getSecondCurrentItem());
		}
		else
		{
			times_string = ((getHourCurrentItem() < 10) ? "0" + getHourCurrentItem() : getHourCurrentItem()) + ":" + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem() : getMinuteCurrentItem()) + ":" + ((getSecondCurrentItem() < 10) ? "0" + getSecondCurrentItem() : getSecondCurrentItem()) + ":";
		}
		return times_string;
	}

	/************ 获得当前时间(时——分)串 ***************/
	public String getHourMinuteString(int style)
	{
		String times_string;
		if (style == 0)
		{
			times_string = ((getHourCurrentItem() < 10) ? "0" + getHourCurrentItem() : getHourCurrentItem()) + ":" + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem() : getMinuteCurrentItem());
		}
		else
		{
			times_string = ((getHourCurrentItem() < 10) ? "0" + getHourCurrentItem() : getHourCurrentItem()) + ":" + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem() : getMinuteCurrentItem());
		}
		return times_string;
	}

	public void finish()
	{
		dialog.dismiss();
	}

}