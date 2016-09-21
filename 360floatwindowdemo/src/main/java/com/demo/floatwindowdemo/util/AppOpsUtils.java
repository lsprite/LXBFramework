package com.demo.floatwindowdemo.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * http://blog.csdn.net/mzm489321926/article/details/50542065
 * http://www.cnblogs.com/anni-qianqian/p/5775394.html
 */

public class AppOpsUtils {
    public static final int OP_NONE = -1;
    public static final int OP_COARSE_LOCATION = 0;
    public static final int OP_FINE_LOCATION = 1;
    public static final int OP_GPS = 2;
    public static final int OP_VIBRATE = 3;
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_WRITE_CONTACTS = 5;
    public static final int OP_READ_CALL_LOG = 6;
    public static final int OP_WRITE_CALL_LOG = 7;
    public static final int OP_READ_CALENDAR = 8;
    public static final int OP_WRITE_CALENDAR = 9;
    public static final int OP_WIFI_SCAN = 10;
    public static final int OP_POST_NOTIFICATION = 11;
    public static final int OP_NEIGHBORING_CELLS = 12;
    public static final int OP_CALL_PHONE = 13;
    public static final int OP_READ_SMS = 14;
    public static final int OP_WRITE_SMS = 15;
    public static final int OP_RECEIVE_SMS = 16;
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    public static final int OP_RECEIVE_MMS = 18;
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_SEND_SMS = 20;
    public static final int OP_READ_ICC_SMS = 21;
    public static final int OP_WRITE_ICC_SMS = 22;
    public static final int OP_WRITE_SETTINGS = 23;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    public static final int OP_CAMERA = 26;
    public static final int OP_RECORD_AUDIO = 27;
    public static final int OP_PLAY_AUDIO = 28;
    public static final int OP_READ_CLIPBOARD = 29;
    public static final int OP_WRITE_CLIPBOARD = 30;
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    public static final int OP_AUDIO_RING_VOLUME = 35;
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int OP_WAKE_LOCK = 40;
    public static final int OP_MONITOR_LOCATION = 41;
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int OP_GET_USAGE_STATS = 43;
    public static final int OP_MUTE_MICROPHONE = 44;
    public static final int OP_TOAST_WINDOW = 45;
    public static final int OP_PROJECT_MEDIA = 46;
    public static final int OP_ACTIVATE_VPN = 47;
    public static final int OP_WRITE_WALLPAPER = 48;
    public static final int OP_ASSIST_STRUCTURE = 49;
    public static final int OP_ASSIST_SCREENSHOT = 50;
    public static final int OP_READ_PHONE_STATE = 51;
    public static final int OP_ADD_VOICEMAIL = 52;
    public static final int OP_USE_SIP = 53;
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int OP_USE_FINGERPRINT = 55;
    public static final int OP_BODY_SENSORS = 56;
    public static final int OP_READ_CELL_BROADCASTS = 57;
    public static final int OP_MOCK_LOCATION = 58;
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int OP_TURN_SCREEN_ON = 61;

    public static boolean isAllowed(Context context, int op) {
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        String packageName = context.getApplicationContext().getPackageName();
        AppOpsManager aom = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class<?>[] types = new Class[]{int.class, int.class, String.class};
        Object[] args = new Object[]{op, Binder.getCallingUid(), packageName};
        try {
            Method method = aom.getClass().getDeclaredMethod("checkOpNoThrow", types);
            Object mode = method.invoke(aom, args);
            if ((mode instanceof Integer) && ((Integer) mode == AppOpsManager.MODE_ALLOWED)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //通过反射实现
    public static boolean getAppOps(Context context, int op_code) {
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        try {
//            Object object = context.getSystemService("appops");
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);

            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(op_code);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }
}
