package com.lxb.view.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.ArrayWheelAdapter;
import com.lxb.view.wheel.util.GetWeek;
import com.lxb.view.wheel.util.NumericWheelAdapter;
import com.lxb.view.wheel.util.OnWheelChangedListener;
import com.lxb.view.wheel.util.WheelView;

import java.util.Calendar;

/**
 * @COMPANY:sunnyTech
 * @CLASS:DateWheelView
 * @DESCRIPTION:年/月/日/星期
 * @AUTHOR:Sunny
 * @VERSION:v1.0
 * @DATE:2014-8-22 下午4:00:57
 */
public class DateWheelView
{
	// 标题
	private TextView titleTextView;
	private String titleString;
	// 当前日期
	private int yearDate;
	private int monthDate;
	private int dayDate;
	// 年系数设置
	private int yearLength = 20;
	private int startYear;

	private Context context;
	// 日期控件对象
	private WheelView month;
	private WheelView year;
	private WheelView day;
	private WheelView week;

	/**
	 * 星期是否可见
	 */
	private boolean weekShow = true;

	/**
	 * 月是否显示
	 */
	private boolean monthShow = true;

	/**
	 * 年是否可见
	 */
	private boolean yearShow = true;

	private AlertDialog dialog;

	GetWeek weeks;
	String weekTexts[] = new String[]
	{ "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };
	private int textSize = 24;
	private boolean weekSynchronization = false;
	public View okButton, cancelButton;

	public DateWheelView(Context context)
	{
		final Calendar c = Calendar.getInstance();
		this.yearDate = c.get(Calendar.YEAR);
		// 月份一定要加1
		this.monthDate = c.get(Calendar.MONTH) + 1;
		this.dayDate = c.get(Calendar.DAY_OF_MONTH) + 1;
		// this.weekDate=c.get(CalendarOA.DAY_OF_WEEK);
		this.context = context;
		startYear = this.yearDate;

		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);
	}

	public DateWheelView(Context context, String _titleString)
	{
		final Calendar c = Calendar.getInstance();
		this.yearDate = c.get(Calendar.YEAR);
		this.monthDate = c.get(Calendar.MONTH) + 1;
		this.dayDate = c.get(Calendar.DAY_OF_MONTH) + 1;
		// this.weekDate=c.get(CalendarOA.DAY_OF_WEEK);
		this.context = context;
		startYear = this.yearDate;
		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);

		titleString = _titleString;

	}

	/************ 设置日期 ****************/
	public void setDateTime(int yearsDate, int monthsDate, int daysDate)
	{
		this.yearDate = yearsDate;
		this.monthDate = monthsDate - 1;
		this.dayDate = daysDate;
	}

	/********* 创建日期对话框 ******************/
	public void createDateDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogDate = inflater.inflate(R.layout.wheel_date_layout, null);
		year = (WheelView) dialogDate.findViewById(R.id.year);
		month = (WheelView) dialogDate.findViewById(R.id.month);
		day = (WheelView) dialogDate.findViewById(R.id.day);
		week = (WheelView) dialogDate.findViewById(R.id.week);
		if (titleString != null)
		{
			titleTextView = (TextView) dialogDate.findViewById(R.id.Title);
			titleTextView.setText(titleString);
		}

		OnWheelChangedListener listener = new OnWheelChangedListener()
		{
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				updateDate(year, month, day, week);
			}
		};

		// year
		year.setAdapter(new NumericWheelAdapter(startYear, startYear + yearLength));
		year.setCyclic(true);
		year.setCurrentItem(this.yearDate - startYear);
		year.TEXT_SIZE = this.textSize;
		year.setLabel(context.getString(R.string.Year));
		year.addChangingListener(listener);
		if (yearShow)
		{
			year.setVisibility(View.VISIBLE);
		}
		else
		{
			year.setVisibility(View.GONE);
		}

		month.setAdapter(new NumericWheelAdapter(0, 12, "%1$,02d"));
		month.setCurrentItem(monthDate);
		month.setCyclic(true);
		month.setLabel(context.getString(R.string.Month));
		month.addChangingListener(listener);
		month.TEXT_SIZE = this.textSize;
		if (monthShow)
		{
			month.setVisibility(View.VISIBLE);
		}
		else
		{
			month.setVisibility(View.GONE);
		}

		// day
		updateDate(year, month, day, week);
		day.setCurrentItem(this.dayDate - 1);
		day.setCyclic(true);
		day.TEXT_SIZE = this.textSize;
		// 如果隐藏了月,年,则单位应该为天,不用日,更加贴切
		if (!monthShow && !yearShow)
		{
			day.setLabel(context.getString(R.string.Day));
		}
		else
		{
			day.setLabel(context.getString(R.string.Day));
		}
		day.addChangingListener(listener);

		weeks = new GetWeek();
		int curWeek = weeks.getWeek(this.yearDate, this.monthDate + 1, this.dayDate);
		curWeek--;

		week.setAdapter(new ArrayWheelAdapter<String>(weekTexts, 7));
		week.setCurrentItem(curWeek);
		week.setCyclic(true);
		week.setEnabled(false);
		week.TEXT_SIZE = this.textSize;
		if (getWeekShow())
		{
			week.setVisibility(View.VISIBLE);
		}
		else
		{
			week.setVisibility(View.GONE);
		}

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);

		dialog.show();
		dialog.getWindow().setContentView(dialogDate);
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
		cancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});

	}

	/************** 同步修改日期 *********************/
	void updateDate(WheelView year, WheelView month, WheelView day, WheelView week)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, startYear + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		day.setAdapter(new NumericWheelAdapter(0, maxDays, "%1$,02d"));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		if (weekSynchronization)
		{
			weeks = new GetWeek();
			int newWeek = weeks.getWeek(getYearCurrentItem(), getMonthCurrentItem(), getDayCurrentItem());
			newWeek--;
			week.setAdapter(new ArrayWheelAdapter<String>(weekTexts, 7));
			week.setCurrentItem(newWeek);
		}
		else
			weekSynchronization = true;
	}

	/**************** 设置年的长度 ****************/
	public void setYearLength(int length)
	{
		this.yearLength = length;
	}

	public void setStartYear(int years)
	{
		startYear = years;
	}

	/************ 获得年限长度 ***************/
	public int getYearLength()
	{
		return this.yearLength;
	}

	/************ 获得哪一年 ***************/
	public int getYearCurrentItem()
	{
		return year.getCurrentItem() + startYear;
	}

	/************ 获得哪一月 ***************/
	public int getMonthCurrentItem()
	{
		// return month.getCurrentItem() + 1;
		return month.getCurrentItem();
	}

	/************ 获得哪一天 ***************/
	public int getDayCurrentItem()
	{
		// return day.getCurrentItem() + 1;
		return day.getCurrentItem();
	}

	/************ 设置字体大小 ***************/
	public void setTextSizes(int size)
	{
		this.textSize = size;
	}

	/************ 获得星期几 ***************/
	public int getWeekCurrentItem()
	{
		return week.getCurrentItem() + 1;
	}

	/************ 设置星期视图是否可见 ***************/
	public void setWeekShow(boolean weekShow)
	{
		this.weekShow = weekShow;
	}

	/************ 获得星期视图是否可见 ***************/
	public boolean getWeekShow()
	{
		return this.weekShow;
	}

	public boolean isYearShow()
	{
		return yearShow;
	}

	public void setYearShow(boolean yearShow)
	{
		this.yearShow = yearShow;
	}

	public boolean isMonthShow()
	{
		return monthShow;
	}

	public void setMonthShow(boolean monthShow)
	{
		this.monthShow = monthShow;
	}

	/**
	 * @description:获得当前日期串
	 * @author:ctrlsoft
	 * @return:String
	 * @param style
	 *            ,0:年-月-日,1:月-日,2:日,其它:****年**月**日
	 * @return
	 */
	public String getDateString(int style)
	{
		String dates_string;
		if (style == 0)
		{
			dates_string = getYearCurrentItem() + "-" + ((getMonthCurrentItem() < 10) ? "0" + getMonthCurrentItem() : getMonthCurrentItem()) + "-" + ((getDayCurrentItem() < 10) ? "0" + getDayCurrentItem() : getDayCurrentItem());
		}
		else if (style == 1)
		{
			// 月-日
			dates_string = ((getMonthCurrentItem() < 10) ? "0" + getMonthCurrentItem() : getMonthCurrentItem()) + "-" + ((getDayCurrentItem() < 10) ? "0" + getDayCurrentItem() : getDayCurrentItem());
		}
		else if (style == 2)
		{
			// 日
			if (getDayCurrentItem() < 10)
			{
				dates_string = "0" + (getDayCurrentItem() - 0);
			}
			else
			{
				dates_string = "" + (getDayCurrentItem() - 0);
			}
		}
		else
		{
			dates_string = getYearCurrentItem() + "年" + ((getMonthCurrentItem() < 10) ? "0" + getMonthCurrentItem() : getMonthCurrentItem()) + "月" + ((getDayCurrentItem() < 10) ? "0" + getDayCurrentItem() : getDayCurrentItem()) + "日";
		}
		return dates_string;
	}

	public void finish()
	{
		dialog.dismiss();
	}
}
