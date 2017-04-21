package com.nemo.cloudedittext;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nemo.cloudedittext.bean.ContactBean;

public class CloudEditText extends ViewGroup {
	private final String NO_EMAIL = "邮件格式不对";
	private Context mContext;
	private AutoCompleteTextView input;
	private ArrayList<ContactBean> allDates = new ArrayList<ContactBean>();
	private ArrayList<ContactBean> addedDates = new ArrayList<ContactBean>();
	private CloudEditTextAdapter adapter;
	private final float TEXT_SIZE = 18;// 字体大小
	private Drawable rightDrawable;// 删除图标
	private int drawablePadding;// 删除图标离字的距离
	private final String END_STR = ",";// 输入时候的结束符

	// 获取输入结果

	public ArrayList<ContactBean> getAddedDates() {
		if (input.getText().toString().equals("")) {
			return addedDates;
		} else {

			if (isEmail(input.getText().toString())) {
				ContactBean bean = new ContactBean(input.getText().toString(),
						input.getText().toString());
				if (!isAdded(bean)) {
					addedDates.add(bean);
				}
				return addedDates;
			} else {
				// Toast.makeText(mContext, NO_EMAIL,
				// Toast.LENGTH_SHORT).show();
				return null;
			}
		}
	}

	private void removeAdded(ContactBean bean) {
		// System.out.println(bean.getEmail());
		// System.out.println("before removeAdded:" + addedDates.size());
		for (int i = 0; i < addedDates.size(); i++) {
			if (bean.getEmail().equals(addedDates.get(i).getEmail())) {
				addedDates.remove(i);
			}
		}
//		System.out.println("after removeAdded:" + addedDates.size());
	}

	//
	public CloudEditText(Context context) {
		super(context);
		init(context);
	}

	public CloudEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CloudEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
		rightDrawable = getResources().getDrawable(
				R.drawable.cloud_edittext_delete);
		drawablePadding = dip2px(getContext(), 1);
		//
		input = new AutoCompleteTextView(mContext);
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		input.setDropDownWidth(width);// 设置自动不全宽与屏幕跨度相同
		input.setDropDownBackgroundDrawable(getResources().getDrawable(
				R.drawable.cloud_edittext_background));
		input.setBackground(null);
		input.setTextSize(TEXT_SIZE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		input.setMinWidth(100);
		addView(input, params);
		// input.setFocusable(true);
		// input.setFocusableInTouchMode(true);
		input.setOnKeyListener(new OnKeyListener() {// 删除字符到第一个时，删除之前的textview

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_UP) {
					if (keyCode == KeyEvent.KEYCODE_DEL) {
						if (input.getText().toString().equals("")) {
							if (getChildCount() > 1) {
								System.out.println("++++");
								System.err.println("++++"
										+ getChildAt(getChildCount() - 2)
												.getTag());
								removeAdded((ContactBean) getChildAt(
										getChildCount() - 2).getTag());
								removeViewAt(getChildCount() - 2);
							}
						}
					}
				}
				return false;
			}
		});
		input.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ContactBean obj = (ContactBean) parent
						.getItemAtPosition(position);
				addItem(obj);
				input.setText("");
			}
		});
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().endsWith(END_STR)) {
					if (isEmail(s.toString().replace(END_STR, ""))) {
						ContactBean bean = new ContactBean();
						bean.setName(s.toString().replace(END_STR, ""));
						bean.setEmail(s.toString().replace(END_STR, ""));
						addItem(bean);
						input.setText("");
					} else {
						Toast.makeText(mContext, NO_EMAIL, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
	}

	public void initAutoCompleteTextViewDate(ArrayList<ContactBean> dates) {
		allDates = (ArrayList<ContactBean>) dates.clone();
		adapter = new CloudEditTextAdapter(mContext, allDates);
		input.setAdapter(adapter);
		input.setThreshold(1);
	}

	public boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	public void addItem(ContactBean bean) {
		if (!isAdded(bean)) {
			final TextView textview = new TextView(mContext);
			textview.setBackgroundResource(R.drawable.cloud_edittext_common_mentions_background);
			textview.setText(bean.getName());
			textview.setTag(bean);
			addedDates.add(bean);
			textview.setTextSize(TEXT_SIZE);
			textview.setCompoundDrawablesWithIntrinsicBounds(null, null,
					rightDrawable, null);
			textview.setCompoundDrawablePadding(drawablePadding);
			LayoutParams lp = textview.getLayoutParams();
			textview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("+++");
					System.err.println("+++" + v.getTag());
					removeAdded((ContactBean) v.getTag());
					removeView(textview);
				}
			});
			addView(textview, getChildCount() - 1);
		}
	}

	// 判断该邮件是否添加过
	private boolean isAdded(ContactBean bean) {
		for (int i = 0; i < addedDates.size(); i++) {
			if (bean.getEmail().equals(addedDates.get(i).getEmail())) {
				return true;
			}
		}
		return false;
	}

	//
	private static final int PADDING_HOR = 20;// 水平方向padding
	private static final int PADDING_VERTICAL = 5;// 垂直方向padding

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int autualWidth = r - l;
		int x = 0;// 横坐标开始
		int y = 0;// 纵坐标开始
		int rows = 1;
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			// view.setBackgroundColor(Color.GREEN);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width;
			if (x > autualWidth) {
				x = width;
				rows++;
			}
			y = rows * (height);
			if (i == 0) {
				view.layout(x - width, y - height, x, y);
			} else {
				view.layout(x - width, y - height, x, y);
			}
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth;// 实际宽度
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR,
					PADDING_VERTICAL);
			// ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)
			// child
			// .getLayoutParams();
			// p.setMargins(MARGIN_HOR, MARGIN_VERTICAL, MARGIN_HOR,
			// MARGIN_VERTICAL);
			// child.setLayoutParams(p);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width;
			if (x > actualWidth) {// 换行
				x = width;
				rows++;
			}
			y = rows * (height);
		}
		setMeasuredDimension(actualWidth, y);
	}

	/** dip转换px */
	public static int dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}
}
