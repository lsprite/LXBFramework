package com.lxb.slidedetailslayout_demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    /**
     * 得到每个页面
     */
    @Override
    public Fragment getItem(int arg0) {
        return (fragmentList == null || fragmentList.size() == 0) ? null
                : fragmentList.get(arg0);
    }

    /**
     * 每个页面的title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}

