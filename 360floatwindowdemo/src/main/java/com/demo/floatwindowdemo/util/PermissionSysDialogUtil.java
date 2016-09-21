package com.demo.floatwindowdemo.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/9/21.
 */

public class PermissionSysDialogUtil {
    public static void setCallDialogPermission(final Context context) {
        try {
            if (isFirstApp(context)) {
                if (OSUtils.isMIUI()) {
                    String miuiVersion = getMiuiVersion();
                    if ("V5".equals(miuiVersion)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        builder.setTitle("提示");
                        builder.setMessage("系统需要手动开启“悬浮窗”，才可以正常显示来电弹窗。");
                        builder.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                showInstalledAppDetails(context, context.getPackageName());
                            }
                        })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Toast.makeText(context, "您可以通过“" + getApplicationName(context) + "”的【应用-设置-权限设置-显示悬浮窗】进行快速设置。", Toast.LENGTH_LONG).show();
                                    }
                                });
                        builder.setCancelable(false);// 点击对话框外不会消失
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        builder.setTitle("提示");
                        builder.setMessage("系统需要手动开启“悬浮窗”，才可以正常显示来电弹窗。");
                        builder.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                setMiuiPermission(context);
                                Toast.makeText(context, "下滑至“显示悬浮窗”，点击选择“允许”", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(context, "您可以通过“" + getApplicationName(context) + "”的【设置-权限设置-显示悬浮窗】进行快速设置。", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setCancelable(false);// 点击对话框外不会消失
                        builder.show();

                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setTitle("提示");
                    builder.setMessage("系统需要手动开启“悬浮窗”，才可以正常显示来电弹窗。");
                    builder.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            showInstalledAppDetails(context, context.getPackageName());
                        }
                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Toast.makeText(context, "您可以通过“" + getApplicationName(context) + "”的【应用-设置-权限设置-显示悬浮窗】进行快速设置。", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.setCancelable(false);// 点击对话框外不会消失
                    builder.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @Description: 跳转到应用信息页
     * @Author Zheng Yuyun
     * @Created 2015年6月16日 下午3:17:11
     */
    private static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            Uri packageURI = Uri.parse("package:" + packageName);
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);

        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }
        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
//            LogUtil.log(context, "intent is not available!");
        }
    }

    /**
     * 判断是否有可以接受的Activity
     *
     * @param context
     * @param action
     * @return
     */
    private static boolean isIntentAvailable(Context context, Intent intent) {
        if (intent == null)
            return false;
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_ACTIVITIES).size() > 0;
    }

    /**
     * @param miuiVersion
     * @Description: 打开MIUI权限管理界面(MIUI v5, v6)
     * @Author Zheng Yuyun
     * @Created 2015年6月16日 下午12:31:27
     */
    @SuppressLint("DefaultLocale")
    private static void setMiuiPermission(Context context) {
        String miuiVersion = getMiuiVersion();
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            if ("V5".equals(miuiVersion)) {
                intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
                intent.putExtra("extra_package_uid", info.applicationInfo.uid);
                context.startActivity(intent);
            } else
//            if ("V6".equals(miuiVersion))
            {
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取MIUI版本
     */
    private static String getMiuiVersion() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            return properties.getProperty("ro.miui.ui.version.name");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        try {
            String applicationName =
                    (String) packageManager.getApplicationLabel(applicationInfo);
            return applicationName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private static boolean isFirstApp(Context context) {
        boolean isFirstIn = false;
        SharedPreferences preferences = context.getSharedPreferences("permission_system_dialog_first_pref",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        return isFirstIn;
    }
}
