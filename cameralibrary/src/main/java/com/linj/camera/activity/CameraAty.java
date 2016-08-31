package com.linj.camera.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.linj.camera.view.CameraContainer;
import com.linj.camera.view.CameraContainer.TakePictureListener;
import com.linj.camera.view.CameraView.FlashMode;
import com.linj.cameralibrary.R;

public class CameraAty extends Activity implements View.OnClickListener,
        TakePictureListener {
    public final static String TAG = "CameraAty";
    private boolean mIsRecordMode = false;
    private String mSaveRoot;
    private CameraContainer mContainer;
    private ImageButton mCameraShutterButton;
    private ImageView mFlashView;
    private View mHeaderBar;
    private boolean isRecording = false;
    private String path = null;// 图片原始路径
    private Button use;// 使用照片
    private Button cannel;// 取消
    private Button repic;// 重拍

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera);

        mHeaderBar = findViewById(R.id.camera_header_bar);
        mContainer = (CameraContainer) findViewById(R.id.container);
        mContainer.setFlashMode(FlashMode.AUTO);
        // mThumbView = (FilterImageView) findViewById(R.id.btn_thumbnail);
        // mVideoIconView = (ImageView) findViewById(R.id.videoicon);
        mCameraShutterButton = (ImageButton) findViewById(R.id.btn_shutter_camera);
        // mRecordShutterButton = (ImageButton)
        // findViewById(R.id.btn_shutter_record);
        // mSwitchCameraView = (ImageView) findViewById(R.id.btn_switch_camera);
        mFlashView = (ImageView) findViewById(R.id.btn_flash_mode);
        // mSwitchModeButton = (ImageButton) findViewById(R.id.btn_switch_mode);
        // mSettingView = (ImageView) findViewById(R.id.btn_other_setting);

        // mThumbView.setOnClickListener(this);
        mCameraShutterButton.setOnClickListener(this);
        // mRecordShutterButton.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        // mSwitchModeButton.setOnClickListener(this);
        // mSwitchCameraView.setOnClickListener(this);
        // mSettingView.setOnClickListener(this);

        mSaveRoot = "camera";
        mContainer.setRootPath(mSaveRoot);
        // initThumbnail();
        // System.out.println(FileOperateUtil.getFolderPath(this,
        // FileOperateUtil.TYPE_IMAGE, mSaveRoot));
        use = (Button) findViewById(R.id.use);
        use.setOnClickListener(this);
        cannel = (Button) findViewById(R.id.cannel);
        cannel.setOnClickListener(this);
        repic = (Button) findViewById(R.id.repic);
        repic.setOnClickListener(this);
        // result = (ImageView) findViewById(R.id.result);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Intent it = null;
        if (view.getId() == R.id.btn_shutter_camera) {
            mCameraShutterButton.setClickable(false);
            mContainer.takePicture(this);
            cannel.setVisibility(View.GONE);
            repic.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.btn_flash_mode) {
            if (mContainer.getFlashMode() == FlashMode.ON) {
                mContainer.setFlashMode(FlashMode.OFF);
                mFlashView.setImageResource(R.drawable.btn_flash_off);
            } else if (mContainer.getFlashMode() == FlashMode.OFF) {
                mContainer.setFlashMode(FlashMode.AUTO);
                mFlashView.setImageResource(R.drawable.btn_flash_auto);
            } else if (mContainer.getFlashMode() == FlashMode.AUTO) {
                mContainer.setFlashMode(FlashMode.TORCH);
                mFlashView.setImageResource(R.drawable.btn_flash_torch);
            } else if (mContainer.getFlashMode() == FlashMode.TORCH) {
                mContainer.setFlashMode(FlashMode.ON);
                mFlashView.setImageResource(R.drawable.btn_flash_on);
            }
        } else if (view.getId() == R.id.use) {
            it = new Intent();
            it.putExtra("path", path);
            it.putExtra("tag", getIntent().getIntExtra("tag", 0));
            setResult(Activity.RESULT_OK, it);
            finish();
        } else if (view.getId() == R.id.repic) {
            mContainer.startPreview();
            mCameraShutterButton.setClickable(true);
            use.setVisibility(View.INVISIBLE);
            cannel.setVisibility(View.VISIBLE);
            repic.setVisibility(View.GONE);
        } else if (view.getId() == R.id.cannel) {
            it = new Intent();
            it.putExtra("path", "");
            it.putExtra("tag", getIntent().getIntExtra("tag", 0));
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent it = new Intent();
            it.putExtra("path", "");
            it.putExtra("tag", getIntent().getIntExtra("tag", 0));
            setResult(Activity.RESULT_CANCELED, it);
            finish();
        }
        return false;
    }

    // private void stopRecord() {
    // mContainer.stopRecord(this);
    // isRecording = false;
    // mRecordShutterButton
    // .setBackgroundResource(R.drawable.btn_shutter_record);
    // }

    @Override
    public void onTakePictureEnd(Bitmap bitmap, String path
    ) {
        // Toast.makeText(CameraAty.this, path, Toast.LENGTH_SHORT).show();
        use.setVisibility(View.VISIBLE);
        this.path = path;
    }

//    @Override
//    public void onAnimtionEnd(Bitmap bm, boolean isVideo) {
    // if (bm != null) {
    // // ��������?
    // Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);
    // mThumbView.setImageBitmap(thumbnail);
    // if (isVideo)
    // mVideoIconView.setVisibility(View.VISIBLE);
    // else {
    // mVideoIconView.setVisibility(View.GONE);
    // }
    // }
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}