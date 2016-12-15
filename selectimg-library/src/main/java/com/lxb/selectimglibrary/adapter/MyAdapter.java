package com.lxb.selectimglibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.lxb.selectimglibrary.AlbumActivity;
import com.lxb.selectimglibrary.R;
import com.lxb.selectimglibrary.model.ImageBean;
import com.lxb.selectimglibrary.utils.Bimp;
import com.lxb.selectimglibrary.utils.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MyAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private Context context;

    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.context = context;
    }

    @Override
    public void convert(final ViewHolder helper, final String item) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {

                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                    List<ImageBean> delete = new ArrayList<ImageBean>();
                    for (ImageBean im : Bimp.tempSelectBitmap) {
                        if (im.getPath().equals(mDirPath + "/" + item)) {
                            delete.add(im);
                        }
                    }
                    Bimp.tempSelectBitmap.removeAll(delete);
                    Message msg = new Message();
                    msg.what = 0;
                    AlbumActivity.handler.sendMessage(msg);
                } else
                // 未选择该图片
                {
                    if (Bimp.tempSelectBitmap.size() > Bimp.MAX - 1) {
                        Toast.makeText(context, "超出可选图片数", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                        ImageBean imageBean = new ImageBean();
                        imageBean.setPath(mDirPath + "/" + item);
                        try {
                            imageBean.setBitmap(Bimp.revitionImageSize(mDirPath + "/" + item));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bimp.tempSelectBitmap.add(imageBean);
                        Message msg = new Message();
                        msg.what = 0;
                        AlbumActivity.handler.sendMessage(msg);
                    }

                }

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        } else {
            mSelect.setImageResource(R.drawable.picture_unselected);
            mImageView.setColorFilter(null);
        }

    }
}
