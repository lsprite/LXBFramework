package com.lxb.slidedetailslayout_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * https://toutiao.io/posts/z4cmti/preview
 * duration：动画时长，默认为300ms；
 * percent：切换的阈值百分比，如0.2表示滑动具体为屏幕高度的20%时切换；
 * default_panel：默认展示的面板，仅接受两个enum值：front、behind
 * SlideDetailsLayout支持代码动态调用smoothOpen()来开启第二个面板，smoothClose来关闭第二个面板，默认情况，面板是关闭状态。
 * SlideDetailsLayout的animator.addListener可添加监听实现第一次载入底部时再加载数据(现在暂时是全部加载好，只是让底部setVisibility(VISIBLE);)
 */
public class MainActivity extends FragmentActivity implements
        PullToRefreshView.OnHeaderRefreshListener {
    private SlideDetailsLayout mSlideDetailsLayout;
    //
    private Fragment_A fragment_A;
    private Fragment_B fragment_B;
    FragmentManager fm;
    private Button btn_a, btn_b;
    private int selected = 1;
    private PullToRefreshView pull_refresh_view;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        fm = getSupportFragmentManager();
        showFragment(selected);
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
        hideFragments(ft);

        switch (index) {
            case 1:
                if (fragment_A != null)
                    ft.show(fragment_A);
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

    // 当fragment已被实例化，就隐藏起�?
    public void hideFragments(FragmentTransaction ft) {
        if (fragment_A != null)
            ft.hide(fragment_A);
        if (fragment_B != null)
            ft.hide(fragment_B);
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
                convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.layout_list_item, null);
            }
            final TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return convertView;
        }
    }
}
