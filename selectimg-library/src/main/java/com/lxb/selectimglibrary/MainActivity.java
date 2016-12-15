package com.lxb.selectimglibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.lxb.selectimglibrary.adapter.PictureAdapter;
import com.lxb.selectimglibrary.model.ImageBean;
import com.lxb.selectimglibrary.popwindow.SelectPicPopupWindow;
import com.lxb.selectimglibrary.utils.Bimp;
import com.lxb.selectimglibrary.utils.BitmapUtils;
import com.lxb.selectimglibrary.utils.FileUtils;
import com.lxb.selectimglibrary.view.NoScrollGridView;

import java.io.File;

public class MainActivity extends Activity {

    /*不滚动的GridView*/
    private NoScrollGridView noScrollGridView;
    /*图片适配器*/
    private PictureAdapter adapter;
    private SelectPicPopupWindow menuWindow;
    private MainActivity instence;
    private String filepath;
    /*公共静态Bitmap*/
    public static Bitmap bimap;

    private static final int TAKE_PICTURE = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instence = this;
        setContentView(R.layout.main);
        initViews();
    }

    private void initViews() {
        noScrollGridView = (NoScrollGridView) findViewById(R.id.noScrollgridview);
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new PictureAdapter(this);

        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Bimp.getTempSelectBitmap().size()) {
                    selectImgs();
                } else {
                    Intent intent = new Intent(instence,
                            GalleryActivity.class);
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
        noScrollGridView.setAdapter(adapter);
    }

    private void selectImgs() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(instence.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            if (v.getId() == R.id.item_popupwindows_camera) {
                goCamera();
            } else if (v.getId() == R.id.item_popupwindows_photo) {
                Intent intent = new Intent(instence,
                        AlbumActivity.class);
                startActivity(intent);
            }
        }

    };

    private void goCamera() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < Bimp.MAX  && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = BitmapUtils.getCompressedBitmap(instence, filepath);
                    FileUtils.saveBitmap(bm, fileName);
                    ImageBean takePhoto = new ImageBean();
                    takePhoto.setBitmap(bm);
                    takePhoto.setPath(filepath);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
