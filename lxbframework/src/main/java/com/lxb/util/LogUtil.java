package com.lxb.util;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/2.
 */
public class LogUtil {
    public static boolean LOG = true;

    public static void log(String tag, String msg) {
        if (msg == null)
            msg = "null";
        if (LOG) {
            Log.v(tag, msg);
        }
    }

    public static void log(String tag, String msg, Throwable tr) {
        if (msg == null)
            msg = "null";
        if (LOG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void log(String tag, String msg, int type) {
        if (msg == null)
            msg = "null";
        if (LOG) {
            switch (type) {
                case Log.VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.INFO:
                    Log.i(tag, msg);
                    break;
                case Log.WARN:
                    Log.w(tag, msg);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg);
                    break;
                default:
                    break;
            }

        }
    }

    public static void log(String tag, String msg, Throwable tr, int type) {
        if (msg == null)
            msg = "null";
        if (LOG) {
            switch (type) {
                case Log.VERBOSE:
                    Log.v(tag, msg, tr);
                    break;
                case Log.DEBUG:
                    Log.d(tag, msg, tr);
                    break;
                case Log.INFO:
                    Log.i(tag, msg, tr);
                    break;
                case Log.WARN:
                    Log.w(tag, msg, tr);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg, tr);
                    break;
                default:
                    break;
            }

        }
    }
}
