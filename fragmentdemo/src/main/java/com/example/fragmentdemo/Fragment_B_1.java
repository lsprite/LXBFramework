package com.example.fragmentdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Fragment_B_1 extends BaseFragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_b_1, container, false);
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Fragment_B_1.initView",
				Toast.LENGTH_SHORT).show();
	}

}
