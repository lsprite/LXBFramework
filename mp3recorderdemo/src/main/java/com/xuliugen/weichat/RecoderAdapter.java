package com.xuliugen.weichat;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xuliugen.weichat.MainActivity.Recorder;

import java.util.List;

public class RecoderAdapter extends ArrayAdapter<Recorder> {

	private Context mContext;
	private List<Recorder> mDatas;

	private int mMinItemWidth; // 最小的item宽度
	private int mMaxItemWidth; // 最大的item宽度
	private LayoutInflater mInflater;

	public RecoderAdapter(Context context, List<Recorder> datas) {
		super(context, -1, datas);

		mContext = context;
		mDatas = datas;

		// 获取屏幕的宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
		mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);

		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 定义一个ViewHolder
	 */
	private class ViewHolder {
		TextView seconds; // 时间
		View length; // 长度
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_recoder, parent,
					false);
			holder = new ViewHolder();
			holder.seconds = (TextView) convertView
					.findViewById(R.id.id_recoder_time);
			holder.length = convertView.findViewById(R.id.id_recoder_lenght);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.seconds.setText(Math.round(getItem(position).time) + "\"");
		ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
		lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f)
				* getItem(position).time);
		return convertView;
	}
}
