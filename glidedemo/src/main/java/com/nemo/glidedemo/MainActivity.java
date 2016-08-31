package com.nemo.glidedemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 */
public class MainActivity extends Activity {
    private ListView listview;
    private MainAdapter adapter;
    private ArrayList<String> dates = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        adapter = new MainAdapter(MainActivity.this);
        listview.setAdapter(adapter);
        for (int i = 0; i < 20; i++) {
            dates.add("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg");
        }
        adapter.setList(dates);
//        GlideUtil.displayImage(MainActivity.this, "http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg", img1);
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }
}
