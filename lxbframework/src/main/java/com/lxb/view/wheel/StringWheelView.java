package com.lxb.view.wheel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.ArrayWheelAdapter;
import com.lxb.view.wheel.util.GetWeek;
import com.lxb.view.wheel.util.WheelView;

import java.util.List;

/**
 * @COMPANY:sunnyTech
 * @CLASS:StringWheelView
 * @DESCRIPTION:字符串滚轮
 * @AUTHOR:Sunny
 * @VERSION:v1.0
 * @DATE:2014-8-21 下午5:58:15
 */
public class StringWheelView {
	/**
	 * 标题控件
	 */
	private TextView titleTextView;

	/**
	 * 标题
	 */
	private String titleString;

	/**
	 * 参数的单位
	 */
	private String labelString;

	// 当前日期
	private int yearDate;
	private int monthDate;
	private int dayDate;

	private Context context;

	/**
	 * 字符串滑动滚轮控件对象
	 */
	private WheelView stringWheelView;

	int maxValue, minValue, defaultValue;

	private AlertDialog dialog;

	GetWeek weeks;

	/**
	 * 传入的数据源:字符串数组 用List<String>应该也可以,以后再试试.
	 */
	String stringS[] = new String[] {};
	List<String> strList;

	/**
	 * 字体大小
	 */
	private int textSize = 24;

	public View okButton, cancelButton;

	/**
	 * 
	 * create a instance StringWheelView.
	 * 
	 * @param context
	 * @param _titleString
	 * @param _labelString
	 * @param _stringS
	 */
	public StringWheelView(Context context, String _titleString,
			String _labelString, String _stringS[]) {
		this.context = context;
		textSize = context.getResources().getDimensionPixelOffset(
				R.dimen.sdafwerwe);
		titleString = _titleString;
		labelString = _labelString;
		stringS = _stringS;
	}

	/**
	 * @description:创建日期对话框！
	 * @author:Sunny
	 * @return:void
	 */
	public void createDateDialog() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogDate = inflater.inflate(R.layout.wheel_date_layout,
				null);
		stringWheelView = (WheelView) dialogDate.findViewById(R.id.week);
		if (titleString != null) {
			titleTextView = (TextView) dialogDate.findViewById(R.id.Title);
			titleTextView.setText(titleString);
		}
		stringWheelView.setAdapter(new ArrayWheelAdapter<String>(stringS,
				stringS.length));
		stringWheelView.setCurrentItem(2);
		stringWheelView.setCyclic(true);
		stringWheelView.setEnabled(false);
		stringWheelView.TEXT_SIZE = this.textSize;

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(dialogDate);
		// 必须点击对话框按钮,对话框才能消失.
		dialog.setCancelable(false);
		okButton = dialog.getWindow().findViewById(R.id.OkButton);
		cancelButton = dialog.getWindow().findViewById(R.id.CancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}

	/**
	 * @description:设置字体大小
	 * @author:Sunny
	 * @return:void
	 * @param size
	 */
	public void setTextSizes(int size) {
		this.textSize = size;
	}

	/**
	 * @description:获得当前选中项的值
	 * @author:Sunny
	 * @return:int
	 * @return
	 */
	public int getWeekCurrentItem() {
		return stringWheelView.getCurrentItem() + 1;
	}

	public void finish() {
		dialog.dismiss();
	}
}
