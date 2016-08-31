package com.zhy.imageloader;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lxb.framework.R;
import com.zhy.bean.ImageFloder;
import com.zhy.utils.BasePopupWindowForListView;
import com.zhy.utils.CommonAdapter;
import com.zhy.utils.ImageLoader;
import com.zhy.utils.ViewHolder;

import java.util.List;

public class ListImageDirPopupWindow extends
        BasePopupWindowForListView<ImageFloder> {
    private ListView mListDir;

    private CommonAdapter adapter;

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFloder> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        adapter = new CommonAdapter<ImageFloder>(context, mDatas,
                R.layout.local_list_image_list_dir_item) {
            @Override
            public void convert(ViewHolder helper, ImageFloder item) {
                helper.setText(R.id.id_dir_item_name, item.getDir());
                helper.setImageByUrl(R.id.id_dir_item_image,
                        item.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
                // 遍历目录，看该目录下释放有文件被选中
                try {
                    boolean is_dir_selected = false;
                    if (MyAdapter.mSelectedImage != null) {
                        for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) {

                            int lastIndexOf = MyAdapter.mSelectedImage.get(i)
                                    .lastIndexOf("/");
                            String image_name = MyAdapter.mSelectedImage.get(i)
                                    .substring(
                                            lastIndexOf + 1,
                                            MyAdapter.mSelectedImage.get(i)
                                                    .length());//
                            // System.out
                            // .println(item.getDir() + "/" + image_name);
                            if (MyAdapter.mSelectedImage.get(i).equals(
                                    item.getDir() + "/" + image_name)) {
                                is_dir_selected = true;
                                break;
                            }
                            // if (MyAdapter.mSelectedImage.get(i).startsWith(
                            // item.getDir())) {
                            // is_dir_selected = true;
                            // }
                        }
                        if (is_dir_selected) {
                            helper.setVISIBLE(R.id.img_dir_selected);
                        } else {
                            helper.setINVISIBLE(R.id.img_dir_selected);
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        mListDir.setAdapter(adapter);
        // mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
        // R.layout.local_list_image_list_dir_item) {
        // @Override
        // public void convert(ViewHolder helper, ImageFloder item) {
        // System.out.println("item.getDir():" + item.getDir());
        // helper.setText(R.id.id_dir_item_name, item.getDir());
        // helper.setImageByUrl(R.id.id_dir_item_image,
        // item.getFirstImagePath());
        // helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
        // try {
        // boolean is_dir_selected = false;
        // if (MyAdapter.mSelectedImage != null
        // && MyAdapter.mSelectedImage.size() > 0) {
        // for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) {
        // if (MyAdapter.mSelectedImage.get(i).startsWith(
        // item.getDir())) {
        // is_dir_selected = true;
        // }
        // }
        // if (is_dir_selected) {
        // helper.setVISIBLE(R.id.img_dir_selected);
        // } else {
        // helper.setINVISIBLE(R.id.img_dir_selected);
        // }
        // }
        // } catch (Exception e) {
        // // TODO: handle exception
        // }
        // }
        // });

    }

    public void notifyDataSetChanged() {
        try {
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public interface OnImageDirSelected {
        void selected(ImageFloder floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDatas.get(position));
                }
            }
        });
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
        // TODO Auto-generated method stub
    }

}
