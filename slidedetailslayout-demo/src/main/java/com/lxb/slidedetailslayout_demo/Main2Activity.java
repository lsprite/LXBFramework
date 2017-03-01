package com.lxb.slidedetailslayout_demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lxb.view.pulltorefreshview.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.bleu.widget.slidedetails.SlideDetailsLayout;

/**
 * 下面菜单添加左右滑动
 */
public class Main2Activity extends FragmentActivity implements
        PullToRefreshView.OnHeaderRefreshListener {
    private SlideDetailsLayout mSlideDetailsLayout;
    //
    private Button btn_a, btn_b;
    private DecoratorViewPager viewpager;
    private List<Fragment> fragments;
    private int selected = 0;
    private PullToRefreshView pull_refresh_view;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mSlideDetailsLayout = (SlideDetailsLayout) findViewById(R.id.slidedetails);
        initView();
    }

    private void initView() {
        //上面
        final ListView listView = (ListView) findViewById(android.R.id.list);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("data: " + i);
        }
        listView.setAdapter(new Adapter(datas));
        //下面
        btn_a = (Button) findViewById(R.id.btn_a);
        btn_b = (Button) findViewById(R.id.btn_b);
        viewpager = (DecoratorViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment_A());
        fragments.add(new Fragment_B());
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragments));
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
        //
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnLoad(false);
        pull_refresh_view.setOnRefresh(true);
        pull_refresh_view.setOnHeaderRefreshListener(this);
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


    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        mSlideDetailsLayout.smoothClose(true);
    }

    //==========================
    private class Adapter extends BaseAdapter {

        private List<String> datas;

        Adapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return null == datas ? 0 : datas.size();
        }

        @Override
        public String getItem(int position) {
            return null == datas ? null : datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = Main2Activity.this.getLayoutInflater().inflate(R.layout.layout_list_item, null);
            }
            final TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return convertView;
        }
    }
}
