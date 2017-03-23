package com.lxb.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 显示week的布局adapter
 */
public class WeekGridAdapter extends BaseAdapter {

	final String[] titles = new String[] { "日", "一", "二", "三", "四", "五", "六" };
	private Context mContext;

	public WeekGridAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView week = new TextView(mContext);
		LayoutParams week_params = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		week.setLayoutParams(week_params);
		week.setPadding(0, ViewUtil.dip2px(mContext, 3), 0,
				ViewUtil.dip2px(mContext, 3));
		week.setGravity(Gravity.CENTER);
		week.setFocusable(false);
		week.setBackgroundColor(Color.TRANSPARENT);

		if (position == 6) { // 周六
			week.setBackgroundColor(Color.argb(0xff, 0x52, 0x9b, 0xd0));
			week.setTextColor(Color.WHITE);
		} else if (position == 0) { // 周日
			week.setBackgroundColor(Color.argb(0xff, 0xbc, 0x44, 0x45));
			week.setTextColor(Color.WHITE);
		} else {
			week.setTextColor(Color.BLACK);
		}
		week.setText(getItem(position) + "");
		return week;
	}
}
