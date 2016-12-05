package com.lxb.view.fileselectlibrary.photo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lxb.view.fileselectlibrary.R;
import com.lxb.view.fileselectlibrary.util.FileUtil;
import com.lxb.view.fileselectlibrary.util.ImageCompression;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 这个是进入相册显示所有图片的界面
 */
public class AlbumActivity extends Activity {
    //显示手机里的所有图片的列表控件
    private GridView gridView;
    //当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    //完成按钮
    private Button okButton;
    // 返回按钮
    private Button back;
    // 取消按钮
//	private Button cancel;
    private Intent intent;
    // 预览按钮
    private Button preview;
    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;
    ArrayList<String> listPath = new ArrayList<String>();
    private int selectedFileSize = 0;
    private boolean isAdd = false;
    private boolean isDel = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(Res.getLayoutID("plugin_camera_album"));
        setContentView(R.layout.plugin_camera_album);
//		PublicWay.activityList.add(this);
        mContext = this;
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
//        bitmap = BitmapFactory.decodeResource(getResources(), Res.getDrawableID("plugin_camera_no_pictures"));
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);
        init();
        initListener();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
        selectedFileSize = Bimp.tempSelectBitmap.size();
        Log.e("aa", "本地添加照片数selectedFileSize == " + selectedFileSize);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub  
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    // 预览按钮的监听
    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
            if (Bimp.tempSelectBitmap.size() > 0) {
                intent.putExtra("position", "1");
                intent.setClass(AlbumActivity.this, GalleryActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
//			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, NormalFlowParamActivity.class);
//			startActivity(intent);
            setResult();
        }

    }

    // 返回按钮监听
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
            setResult();
        }
    }

    // 取消按钮的监听
    private class CancelListener implements OnClickListener {
        public void onClick(View v) {
//			Bimp.tempSelectBitmap.clear();
//			intent.setClass(mContext, NormalFlowParamActivity.class);
//			startActivity(intent);
            finish();
        }
    }


    // 初始化，给一些对象赋值
    private void init() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new BackListener());
        preview = (Button) findViewById(R.id.preview);
        preview.setOnClickListener(new PreviewListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
                Bimp.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(R.id.myText);
        gridView.setEmptyView(tv);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size()
                + "/" + PublicWay.num + ")");
    }

    private void initListener() {

        gridImageAdapter
                .setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, boolean isChecked, Button chooseBt) {
                        int size = Bimp.tempSelectBitmap.size();
                        if (size >= PublicWay.num) {
                            toggleButton.setChecked(false);
                            chooseBt.setVisibility(View.GONE);
                            if (!removeOneData(dataList.get(position))) {
                                Toast.makeText(AlbumActivity.this, "超出可选图片张数",
                                        Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if (isChecked) {
                            chooseBt.setVisibility(View.VISIBLE);
                            ImageItem item = dataList.get(position);
                            Bimp.tempSelectBitmap.add(item);
                            String path = item.getImagePath();
                            listPath.add(path);

                            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size()
                                    + "/" + PublicWay.num + ")");
                        } else {
                            ImageItem ii = dataList.get(position);
                            ii.getBitmap().recycle();
                            String path = ii.getImagePath();
                            if (listPath.contains(path)) {
                                listPath.remove(listPath);
                            } else {
                                listPath.add(path);
                            }

                            Bimp.tempSelectBitmap.remove(ii);
//							Bimp.tempSelectBitmap.remove(dataList.get(position));
                            chooseBt.setVisibility(View.GONE);
                            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
                        }
                        isShowOkBt();
                    }
                });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private void setResult() {
        String tmpSdPath = Environment.getExternalStorageDirectory() + File.separator + "tmp_office";
        File tmpFolders = new File(tmpSdPath);
        if (!tmpFolders.exists()) {
            tmpFolders.mkdirs();
        }
        int size = Bimp.tempSelectBitmap.size();
        long fileSize = 0;
        if (selectedFileSize < size) {
            isAdd = true;
            isDel = false;
            try {
                for (String path : listPath) {
                    File file = new File(path);
                    String tmpFilePath = tmpSdPath + File.separator + file.getName();
                    File tmpFile = new File(tmpFilePath);
                    FileUtil.copyFile(file, tmpFile);
                    Bitmap bm = Bimp.revitionImageSize(tmpFilePath);
                    ImageCompression.getInstance().qualityPress(getApplicationContext(), bm, tmpFilePath, 85);
                    for (ImageItem item : Bimp.tempSelectBitmap) {
                        String filePath = item.getImagePath();
                        if (path.equals(filePath)) {
                            item.setImagePath(tmpFilePath);
                            break;
                        }
                    }
                    fileSize += tmpFile.length();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            isAdd = false;
            isDel = true;
            try {
                for (String path : listPath) {
                    File file = new File(path);

                    String tmpFilePath = tmpSdPath + File.separator + file.getName();
                    File tmpFile = new File(tmpFilePath);
                    FileUtil.copyFile(file, tmpFile);
                    Bitmap bm = Bimp.revitionImageSize(tmpFilePath);
                    ImageCompression.getInstance().qualityPress(getApplicationContext(), bm, tmpFilePath, 85);

                    for (ImageItem item : Bimp.tempSelectBitmap) {
                        String filePath = item.getImagePath();
                        if (path.equals(filePath)) {
                            item.setImagePath(tmpFilePath);
                            break;
                        }
                    }

                    fileSize += tmpFile.length();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Intent data = new Intent();
        data.putExtra("fileSize", fileSize);
        data.putExtra("isAdd", isAdd);
        data.putExtra("isDel", isDel);
        setResult(RESULT_OK, data);
        finish();
    }

    private boolean removeOneData(ImageItem imageItem) {
        if (Bimp.tempSelectBitmap.contains(imageItem)) {
            Bimp.tempSelectBitmap.remove(imageItem);
            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (Bimp.tempSelectBitmap.size() > 0) {
            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
            setResult();
        }
        return true;

    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if (null != broadcastReceiver) {
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }
}
