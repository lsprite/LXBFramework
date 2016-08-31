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

public class StrWheelView
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
	private WheelView monthWheelView;
	private WheelView yearWheelView;
	private WheelView dayWheelView;
	private WheelView weekWheelView;

	int maxValue, minValue, defaultValue;

	/**
	 * 年是否可见
	 */
	private boolean isShowYear = false;

	/**
	 * 月是否显示
	 */
	private boolean isShowMonth = false;

	/**
	 * 天是否显示
	 */
	boolean isShowDay = false;

	/**
	 * 星期是否可见
	 */
	private boolean isShowWeek = true;

	private AlertDialog dialog;

	GetWeek weeks;
	String weekTexts[] = new String[]
	{};
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
	public StrWheelView(Context context, String _titleString, String _labelString, int _minValue, int _maxValue, int _defaultValue, String _weekTexts[])
	{
		final Calendar c = Calendar.getInstance();
		this.yearDate = c.get(Calendar.YEAR);
		this.monthDate = c.get(Calendar.MONTH);
		this.dayDate = c.get(Calendar.DAY_OF_MONTH);
		this.context = context;
		startYear = this.yearDate;
		textSize = context.getResources().getDimensionPixelOffset(R.dimen.sdafwerwe);

		titleString = _titleString;
		labelString = _labelString;
		maxValue = _maxValue;
		minValue = _minValue;
		defaultValue = _defaultValue;
		weekTexts = _weekTexts;

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
		monthWheelView = (WheelView) dialogDate.findViewById(R.id.month);
		yearWheelView = (WheelView) dialogDate.findViewById(R.id.year);
		dayWheelView = (WheelView) dialogDate.findViewById(R.id.day);
		weekWheelView = (WheelView) dialogDate.findViewById(R.id.week);
		if (titleString != null)
		{
			titleTextView = (TextView) dialogDate.findViewById(R.id.Title);
			titleTextView.setText(titleString);
		}

		monthWheelView.setAdapter(new NumericWheelAdapter(1, 12, "%1$,02d"));
		monthWheelView.setCurrentItem(monthDate);
		monthWheelView.setCyclic(true);
		monthWheelView.setLabel(context.getString(R.string.Month));
		monthWheelView.TEXT_SIZE = this.textSize;
		if (isShowMonth)
		{
			monthWheelView.setVisibility(View.VISIBLE);
		}
		else
		{
			monthWheelView.setVisibility(View.GONE);
		}

		weeks = new GetWeek();
		int curWeek = weeks.getWeek(this.yearDate, this.monthDate + 1, this.dayDate);
		curWeek--;

		weekWheelView.setAdapter(new ArrayWheelAdapter<String>(weekTexts, weekTexts.length));
		weekWheelView.setCurrentItem(0);
		weekWheelView.setCyclic(true);
		weekWheelView.setEnabled(false);
		weekWheelView.TEXT_SIZE = this.textSize;
		if (getWeekShow())
		{
			weekWheelView.setVisibility(View.VISIBLE);
		}
		else
		{
			weekWheelView.setVisibility(View.GONE);
		}

		// yearWheelView
		yearWheelView.setAdapter(new NumericWheelAdapter(startYear, startYear + yearLength));
		yearWheelView.setCyclic(true);
		yearWheelView.setCurrentItem(this.yearDate - startYear);
		yearWheelView.TEXT_SIZE = this.textSize;
		yearWheelView.setLabel(context.getString(R.string.Year));
		if (isShowYear)
		{
			yearWheelView.setVisibility(View.VISIBLE);
		}
		else
		{
			yearWheelView.setVisibility(View.GONE);
		}
		// dayWheelView
		dayWheelView.setAdapter(new NumericWheelAdapter(minValue, maxValue));
		dayWheelView.setCurrentItem(defaultValue);
		dayWheelView.setCyclic(true);
		dayWheelView.TEXT_SIZE = this.textSize;
		dayWheelView.setLabel(labelString);
		if (isShowDay)
		{
			dayWheelView.setVisibility(View.VISIBLE);
		}
		else
		{
			dayWheelView.setVisibility(View.GONE);

		}

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(dialogDate);
		// 必须点击对话框按钮,对话框才能消失.
		dialog.setCancelable(false);
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
		return yearWheelView.getCurrentItem() + startYear;
	}

	/************ 获得哪一月 ***************/
	public int getMonthCurrentItem()
	{
		return monthWheelView.getCurrentItem() + 1;
	}

	/************ 获得哪一天 ***************/
	public int getDayCurrentItem()
	{
		return dayWheelView.getCurrentItem();
	}

	/************ 设置字体大小 ***************/
	public void setTextSizes(int size)
	{
		this.textSize = size;
	}

	/************ 获得数组索引 ***************/
	public int getWeekCurrentItemIndex()
	{
		return weekWheelView.getCurrentItem();
	}

	/************ 获得数组索引名称 ***************/
	public String getWeekCurrentItemValue()
	{
		return weekWheelView.getLabel() + "," + weekWheelView.getContentDescription();
	}

	/************ 设置星期视图是否可见 ***************/
	public void setWeekShow(boolean weekShow)
	{
		this.isShowWeek = weekShow;
	}

	/************ 获得星期视图是否可见 ***************/
	public boolean getWeekShow()
	{
		return this.isShowWeek;
	}

	public boolean isYearShow()
	{
		return isShowYear;
	}

	public void setYearShow(boolean yearShow)
	{
		this.isShowYear = yearShow;
	}

	public boolean isMonthShow()
	{
		return isShowMonth;
	}

	public void setMonthShow(boolean monthShow)
	{
		this.isShowMonth = monthShow;
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
