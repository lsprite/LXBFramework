package com.example.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linj.camera.activity.CameraAty;
import com.lxb.util.UILUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/8.
 */
public class MainActivity extends Activity {
    @InjectView(R.id.btn)
    Button btn;
    @InjectView(R.id.tv)
    TextView tv;
    @InjectView(R.id.img)
    ImageView img;
    //
    private final int RESULT_CAMERA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_CAMERA) {
                tv.setText(data.getStringExtra("path"));
                UILUtils.displayLocalImage(data.getStringExtra("path"), img);
            }
        }
    }

    @OnClick(R.id.btn)
    public void onClick() {
        Intent intent = new Intent(MainActivity.this, CameraAty.class);
        startActivityForResult(intent, RESULT_CAMERA);
    }
}
