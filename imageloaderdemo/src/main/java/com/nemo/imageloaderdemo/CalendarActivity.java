package com.nemo.imageloaderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lxb.view.calendar.CalendarUtil;
import com.lxb.view.calendar.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends Activity {
	private CalendarView calendar;
	private Button btn;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		calendar = (CalendarView) findViewById(R.id.calendar);
		calendar.setOnCalendarViewListener(new CalendarView.OnCalendarViewListener() {

			@Override
			public void onCalendarItemClick(CalendarView view, Date date) {
				// TODO Auto-generated method stub
				// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				// tv.setText(format.format(date));
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				tv.setText(CalendarUtil.getCurrentDay(cal));
			}
		});
		tv = (TextView) findViewById(R.id.tv);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				// tv.setText(format.format(date));
				Calendar cal = Calendar.getInstance();
				tv.setText(CalendarUtil.getCurrentDay(cal));
			}
		});
	}
}
