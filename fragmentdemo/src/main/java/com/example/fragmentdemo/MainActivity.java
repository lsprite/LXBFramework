package com.example.fragmentdemo;

import com.example.fragmentdemo.util.AppInfoUtil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * 我的
 */
public class MainActivity extends FragmentActivity {
	private Fragment_A fragment_A;
	private Fragment_B fragment_B;
	FragmentManager fm;
	private Button btn_a, btn_b;
	private int selected = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 避免第一次打开造成启动的activity按home后重新实例化
		if (!MyApplication.getInstance().isStartApp()) {
			finish();
		}
		setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();
		initView();
		showFragment(selected);
		// AppInfoUtil.getAppInfo(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// Log.v("LH", "onSaveInstanceState"+outState);
		// super.onSaveInstanceState(outState);
		// 将这一行注释掉，阻止activity保存fragment的状态
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_a = (Button) findViewById(R.id.btn_a);
		btn_b = (Button) findViewById(R.id.btn_b);
	}

	public void myClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_a://
			if (selected != 1) {
				//
				showFragment(1);
				selected = 1;
			}
			break;
		case R.id.btn_b://
			if (selected != 2) {
				//
				showFragment(2);
				selected = 2;
			}
			break;
		default:
			break;
		}
	}

	public void showFragment(int index) {
		FragmentTransaction ft = fm.beginTransaction();
		// 想要显示某个fragment,先隐藏所有fragment，防止重叠
		hideFragments(ft);

		switch (index) {
		case 1:
			// 如果fragment1已经存在则将其显示出来
			if (fragment_A != null)
				ft.show(fragment_A);
			// 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
			else {
				fragment_A = new Fragment_A();
				ft.add(R.id.fragment, fragment_A);
			}
			break;
		case 2:
			if (fragment_B != null)
				ft.show(fragment_B);
			else {
				fragment_B = new Fragment_B();
				ft.add(R.id.fragment, fragment_B);
			}
			break;

		}
		ft.commitAllowingStateLoss();
	}

	// 当fragment已被实例化，就隐藏起来
	public void hideFragments(FragmentTransaction ft) {
		if (fragment_A != null)
			ft.hide(fragment_A);
		if (fragment_B != null)
			ft.hide(fragment_B);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(0, 0);
			return true;
		}
		return false;
	}

}
