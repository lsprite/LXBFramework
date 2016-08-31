package com.zhy.imageloader;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxb.framework.R;
import com.lxb.util.MemInfo;
import com.zhy.bean.ImageFloder;
import com.zhy.imageloader.ListImageDirPopupWindow.OnImageDirSelected;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LocalListImageActivity extends Activity implements
        OnImageDirSelected {
    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;

    private GridView mGirdView;
    private MyAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    private RelativeLayout mBottomLy;

    private TextView mChooseDir;
    private TextView mImageCount;
    int totalCount = 0;

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private TextView tv_hint;// 提示用户点击从文件夹选
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(getApplicationContext(), mImgs,
                R.layout.local_list_image_grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(totalCount + "张");
    }

    ;

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.local_list_image_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_list_image);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            if (MemInfo.getmem_UNUSED(LocalListImageActivity.this) < 1024 * 210) {// 小于1g
//                Toast.makeText(this, "内存不足，无法使用，请尝试清理手机内存或重启再使用", Toast.LENGTH_SHORT).show();
//                finish();
//                overridePendingTransition(0, 0);
//            }
        } else {
            Toast.makeText(LocalListImageActivity.this, "sd卡不存在,请插入sd卡", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, 0);
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        initView();
        getImages();
        initEvent();
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        final ObjectAnimator animator = tada(tv_hint);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                animator.end();
                tv_hint.setVisibility(View.INVISIBLE);

            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mAdapter != null && mAdapter.mSelectedImage != null) {
            mAdapter.mSelectedImage.clear();
        }
        MyAdapter.MAXNUM = -1;
        super.onDestroy();
    }

    /**
     * 确定和取消按钮的监听
     */

    public void myClick(View view) {
        Intent it = null;
        if (view.getId() == R.id.btn_cannel) {
            it = new Intent();
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        } else if (view.getId() == R.id.btn_ok) {
            // if (mAdapter != null && mAdapter.mSelectedImage != null) {
            // for (int i = 0; i < mAdapter.mSelectedImage.size(); i++) {
            // System.out.println(mAdapter.mSelectedImage.get(i));
            // }
            // }
            if (mAdapter != null && mAdapter.mSelectedImage != null) {
                if (mAdapter.mSelectedImage.size() == 0) {
                    Toast.makeText(LocalListImageActivity.this, "您还未选择一张图片",
                            Toast.LENGTH_SHORT).show();
                } else {
                    it = new Intent();
                    setResult(Activity.RESULT_OK, it);
                    ArrayList<String> list = new ArrayList<String>(
                            mAdapter.mSelectedImage);
                    it.putStringArrayListExtra("selectedImages", list);
                    finish();
                }
            } else {
                it = new Intent();
                setResult(Activity.RESULT_CANCELED, it);
                finish();
            }
        }
//        switch (view.getId()) {
//            case R.id.btn_cannel:
//                // Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
//                it = new Intent();
//                setResult(Activity.RESULT_CANCELED, it);
//                finish();
//                break;
//            case R.id.btn_ok:
//                // Toast.makeText(this, "确定", Toast.LENGTH_SHORT).show();
//                // if (mAdapter != null && mAdapter.mSelectedImage != null) {
//                // for (int i = 0; i < mAdapter.mSelectedImage.size(); i++) {
//                // System.out.println(mAdapter.mSelectedImage.get(i));
//                // }
//                // }
//                if (mAdapter != null && mAdapter.mSelectedImage != null) {
//                    if (mAdapter.mSelectedImage.size() == 0) {
//                        Toast.makeText(LocalListImageActivity.this, "您还未选择一张图片",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
//                        it = new Intent();
//                        setResult(Activity.RESULT_OK, it);
//                        ArrayList<String> list = new ArrayList<String>(
//                                mAdapter.mSelectedImage);
//                        it.putStringArrayListExtra("selectedImages", list);
//                        finish();
//                    }
//
//                } else {
//                    it = new Intent();
//                    setResult(Activity.RESULT_CANCELED, it);
//                    finish();
//                }
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = LocalListImageActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                // Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    // Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg")
                                    || filename.endsWith(".JPG")
                                    || filename.endsWith(".PNG")
                                    || filename.endsWith(".JPEG"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    /**
     * 初始化View
     */
    private void initView() {
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);

        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);

    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                mListImageDirPopupWindow.notifyDataSetChanged();
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(ImageFloder floder) {

        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".JPG")
                        || filename.endsWith(".PNG")
                        || filename.endsWith(".JPEG"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(getApplicationContext(), mImgs,
                R.layout.local_list_image_grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();

    }

    // View抖动
    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                View.SCALE_X, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                View.SCALE_Y, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(
                View.ROTATION, Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0));

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,
                pvhScaleY, pvhRotate).setDuration(1000);
    }

//	public static ObjectAnimator nope(View view) {
//		int delta = view.getResources().getDimensionPixelOffset(14);
//
//		PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(
//				View.TRANSLATION_X, Keyframe.ofFloat(0f, 0),
//				Keyframe.ofFloat(.10f, -delta), Keyframe.ofFloat(.26f, delta),
//				Keyframe.ofFloat(.42f, -delta), Keyframe.ofFloat(.58f, delta),
//				Keyframe.ofFloat(.74f, -delta), Keyframe.ofFloat(.90f, delta),
//				Keyframe.ofFloat(1f, 0f));
//
//		return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX)
//				.setDuration(500);
//	}
}
