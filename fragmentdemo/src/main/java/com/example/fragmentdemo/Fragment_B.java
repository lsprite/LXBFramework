package com.example.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Fragment_B extends BaseFragment implements OnClickListener {
	private View view;
	private Fragment_B_1 fragment_B_1;
	private Fragment_B_2 fragment_B_2;
	FragmentManager fm;
	private Button btn_b1, btn_b2;
	private int selected = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_b, container, false);
		initView();
		showFragment(selected);
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		fm = getChildFragmentManager();
		btn_b1 = (Button) view.findViewById(R.id.btn_b1);
		btn_b2 = (Button) view.findViewById(R.id.btn_b2);
		btn_b1.setOnClickListener(this);
		btn_b2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_b1://
			System.out.println("btn_b1");
			if (selected != 1) {
				//
				showFragment(1);
				selected = 1;
			}
			break;
		case R.id.btn_b2://
			System.out.println("btn_b2");
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
		// 想要显示�?个fragment,先隐藏所有fragment，防止重�?
		hideFragments(ft);

		switch (index) {
		case 1:
			// 如果fragment1已经存在则将其显示出�?
			if (fragment_B_1 != null)
				ft.show(fragment_B_1);
			// 否则是第�?次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
			else {
				fragment_B_1 = new Fragment_B_1();
				ft.add(R.id.fragment, fragment_B_1);
			}
			break;
		case 2:
			if (fragment_B_2 != null)
				ft.show(fragment_B_2);
			else {
				fragment_B_2 = new Fragment_B_2();
				ft.add(R.id.fragment, fragment_B_2);
			}
			break;

		}
		ft.commitAllowingStateLoss();
	}

	// 当fragment已被实例化，就隐藏起�?
	public void hideFragments(FragmentTransaction ft) {
		if (fragment_B_1 != null) {
			ft.hide(fragment_B_1);
		}
		if (fragment_B_2 != null) {
			ft.hide(fragment_B_2);
		}
	}

}
