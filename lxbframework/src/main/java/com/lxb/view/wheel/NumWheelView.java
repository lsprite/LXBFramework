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
import com.lxb.view.wheel.util.WheelView;

import java.util.Calendar;

public class NumWheelView
{
	// 标题
	private TextView titleTextView;
	private String titleString;
	// 单位
	private String labelString;
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

	int maxValue, minValue, defaultValue;

	/**
	 * 星期是否可见
	 */
	private boolean weekShow = false;

	/**
	 * 月是否显示
	 */
	private boolean monthShow = false;

	/**
	 * 年是否可见
	 */
	private boolean yearShow = false;

	private AlertDialog dialog;

	GetWeek weeks;
	String weekTexts[] = new String[]
	{ "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };
	private int textSize = 24;
	private boolean weekSynchronization = false;
	public View okButton, cancelButton;

	/**
	 * 
	 * create a instance NumWheelView.
	 * 
	 * @param context
	 * @param _titleString
	 * @param _labelString
	 * @param _minValue
	 * @param _maxValue
	 * @param _defaultValue
	 */
	public NumWheelView(Context context, String _titleString, String _labelString, int _minValue, int _maxValue, int _defaultValue)
	{
		final Calendar c = Calendar.getInstance();
		this.yearDate = c.get(Calendar.YEAR);
		this.monthDate = c.get(Calendar.MONTH);
		this.dayDate = c.get(Calendar.DAY_OF_MONTH);
		// this.weekDate=c.get(CalendarOA.DAY_OF_WEEK);
		this.context = context;
		startYear = this.yearDate;
		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);

		titleString = _titleString;
		labelString = _labelString;
		maxValue = _maxValue;
		minValue = _minValue;
		defaultValue = _defaultValue;

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
		month = (WheelView) dialogDate.findViewById(R.id.month);
		year = (WheelView) dialogDate.findViewById(R.id.year);
		day = (WheelView) dialogDate.findViewById(R.id.day);
		week = (WheelView) dialogDate.findViewById(R.id.week);
		if (titleString != null)
		{
			titleTextView = (TextView) dialogDate.findViewById(R.id.Title);
			titleTextView.setText(titleString);
		}

		month.setAdapter(new NumericWheelAdapter(1, 12, "%1$,02d"));
		month.setCurrentItem(monthDate);
		month.setCyclic(true);
		month.setLabel(context.getString(R.string.Month));
		month.TEXT_SIZE = this.textSize;
		if (monthShow)
		{
			month.setVisibility(View.VISIBLE);
		}
		else
		{
			month.setVisibility(View.GONE);
		}

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

		// year
		year.setAdapter(new NumericWheelAdapter(startYear, startYear + yearLength));
		year.setCyclic(true);
		year.setCurrentItem(this.yearDate - startYear);
		year.TEXT_SIZE = this.textSize;
		year.setLabel(context.getString(R.string.Year));
		if (yearShow)
		{
			year.setVisibility(View.VISIBLE);
		}
		else
		{
			year.setVisibility(View.GONE);
		}
		// day
		day.setAdapter(new NumericWheelAdapter(minValue, maxValue));
		day.setCurrentItem(defaultValue);
		day.setCyclic(true);
		day.TEXT_SIZE = this.textSize;
		day.setLabel(labelString);

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(dialogDate);
		okButton = dialog.getWindow().findViewById(R.id.OkButton);
		cancelButton = dialog.getWindow().findViewById(R.id.CancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});

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
		return month.getCurrentItem() + 1;
	}

	/************ 获得哪一天 ***************/
	public int getDayCurrentItem()
	{
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
			dates_string = "" + getDayCurrentItem();
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
