package com.nemo.glidedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 首页主目录
 */
public class MainAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> list;

    //
    public MainAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int paramInt) {
        // TODO Auto-generated method stub
        return list.get(paramInt);
    }

    @Override
    public long getItemId(int paramInt) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup paramViewGroup) {
        // TODO Auto-generated method stub
        View v = null;
        try {
            if (convertView != null) {
                v = convertView;
            } else {
                LayoutInflater inflater = LayoutInflater.from(context);
                v = inflater.inflate(R.layout.item_main, null);
                ItemControls ic = new ItemControls();
                ic.img = (ImageView) v.findViewById(R.id.img);
                v.setTag(ic);
            }
            ItemControls ic = (ItemControls) v.getTag();
            GlideUtil.displayImage(context,list.get(position),ic.img);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return v;
    }

    class ItemControls {
        ImageView img;

    }

    @SuppressWarnings("unchecked")
    public void setList(ArrayList<String> list) {
        if (list != null) {
            this.list = (ArrayList<String>) list.clone();
            notifyDataSetChanged();
        }
    }
}
