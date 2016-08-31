package com.example.fragmentdemo;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
		// 将这一行注释掉，阻止保存fragment的状态
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
