package com.nemo.cloudedittext.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nemo.cloudedittext.CloudEditText;
import com.nemo.cloudedittext.R;
import com.nemo.cloudedittext.bean.ContactBean;

public class MainActivity extends Activity {
	private CloudEditText ct;
	private Button btn;
	private TextView tv;
	//
	private static final int REQUEST_CODE = 0; // 请求码

	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ct.getAddedDates() != null) {
					if (ct.getAddedDates().size() == 0) {
						Toast.makeText(MainActivity.this, "邮件收件人不能为空",
								Toast.LENGTH_SHORT).show();
					} else {
						String s = "";
						for (int i = 0; i < ct.getAddedDates().size(); i++) {
							s = s + "name:"
									+ ct.getAddedDates().get(i).getName()
									+ ",email:"
									+ ct.getAddedDates().get(i).getEmail()
									+ "\n";
						}
						tv.setText(s);
					}
				} else {
					Toast.makeText(MainActivity.this, "输入框内的邮件地址有错",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		tv = (TextView) findViewById(R.id.tv);
		//
		ct = (CloudEditText) findViewById(R.id.ct);
		ArrayList<ContactBean> list = new ArrayList<ContactBean>();
		list.add(new ContactBean("张三", "1@qq.com"));
		list.add(new ContactBean("李四", "2@qq.com"));
		list.add(new ContactBean("32434234234@qq.com", "32434234234@qq.com"));
		list.add(new ContactBean("231231423452@qq.com", "231231423452@qq.com"));
		ct.initAutoCompleteTextViewDate(list);
		// for (int i = 0; i < 12; i++) {
		// TextView textview = new TextView(this);
		// textview.setText(strs[i]);
		// ct.addView(textview);
		// }
		// btn = (Button) findViewById(R.id.btn);
		// btn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// ContactBean bean = new ContactBean();
		// bean.setEmail("aaaa@qq.com");
		// bean.setName("阿三");
		// ct.addItem(bean);
		// }
		// });
	}
}
