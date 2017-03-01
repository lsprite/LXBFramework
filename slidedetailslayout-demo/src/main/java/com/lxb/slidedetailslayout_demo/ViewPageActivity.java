package com.lxb.slidedetailslayout_demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ViewPageActivity extends FragmentActivity {
    private ViewPager viewpager;
    private List<Fragment> fragments;
    private int selected = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);
        initView();
    }
    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment_A());
        fragments.add(new Fragment_B());
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragments));
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new ViewPageActivity.MyOnPageChangeListener());
    }

    public void myClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_a://
                if (selected != 0) {
                    //
                    selected = 0;
                    viewpager.setCurrentItem(0);
                }
                break;
            case R.id.btn_b://
                if (selected != 1) {
                    //
                    selected = 1;
                    viewpager.setCurrentItem(1);
                }
                break;
            default:
                break;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {

            switch (index) {
                case 0:
                    selected = 0;
                    break;
                case 1:
                    selected = 1;
                    break;
            }
        }
    }

}
