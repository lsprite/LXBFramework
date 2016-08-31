package com.lxb.view.wheel.util;

import java.util.Calendar;

public class GetWeek
{
	public GetWeek()
	{
	}

	public int getWeek(int year, int month, int day)
	{
		Calendar calendar1 = Calendar.getInstance();
		calendar1.clear();
		calendar1.set(year, month - 1, day);
		int date = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		if (date == 0)
			date = 7;
		return date;
	}
}
