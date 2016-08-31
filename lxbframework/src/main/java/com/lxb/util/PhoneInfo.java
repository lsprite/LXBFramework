package com.lxb.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/3/2.
 */
public class PhoneInfo {
    // ----------------------系统信息-----------------------------

    /**
     * @description:当前运行Activity的名称
     * @author:Sunny
     * @return:String
     * @return
     */
    public static String getRunningActivityName(Context context)
    {
        String contextString = context.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@")) + "：";
    }

    /**
     * 获取系统版本号
     *
     * @return
     * @throws Exception
     */
    public static int getVersionCode(Context context)
    {
        try
        {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packInfo.versionCode;
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * 获取软件版本号
     */
    public static String getVersionName(Context context, Class cls)
    {

        try
        {

            ComponentName comp = new ComponentName(context, cls);
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
            return pinfo.versionName;
        }
        catch (android.content.pm.PackageManager.NameNotFoundException e)
        {
            return "";
        }
    }

    // ----------------------屏幕-----------------------------

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    public static int getDisplayWidthMetrics(Context context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    public static int getDisplayHeightMetrics(Context context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }




}
