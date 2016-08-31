package com.lxb.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lxb.view.MyAlertDialog;

/**
 * Created by Administrator on 2016/3/2.
 */

public class BaseActivity extends Activity {
    public ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //设置字体大小不随手机设置而变
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    public boolean showWaitDialog(Context context, String title, String message, boolean cancelable, boolean canceledOnTouchOutside) {
        try {
            if (waitDialog == null) {
                waitDialog = ProgressDialog.show(context, title, message);
                if (cancelable) {
                    waitDialog.setCancelable(true);
                } else {
                    waitDialog.setCancelable(false);
                }
                if (canceledOnTouchOutside) {
                    waitDialog.setCanceledOnTouchOutside(true);
                } else {
                    waitDialog.setCanceledOnTouchOutside(false);
                }
                return true;
            } else {
                // Toast.makeText(this, "请勿重复操作", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    public void dismissWaitDialog() {
        try {
            if (waitDialog != null) {
                waitDialog.dismiss();
                waitDialog = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void backWithDialog(String title, String message) {
        try {
            final MyAlertDialog dialog = new MyAlertDialog(this);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setNegativeButton("取消", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton("确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // android.os.Process.killProcess(android.os.Process.myPid());
                    // System.exit(0);
                    dialog.dismiss();
                    finish();
                    overridePendingTransition(0, 0);
                }
            });

            WindowManager m = getWindowManager();
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            Window dialogWindow = dialog.getWindow();
            p.width = (int) (d.getWidth() * 0.80); // 宽度设置为屏幕的0.8
            dialogWindow.setAttributes(p);
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
