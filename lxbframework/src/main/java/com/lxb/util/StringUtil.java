package com.lxb.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/3/3.
 */
public class StringUtil {
    public static String notNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    public static String notNullWithDef(String str, String def) {
        if (str == null || str.equals("")) {
            return def;
        } else {
            return str;
        }
    }

    // 转换成int
    public static String toInt(String str) {
        try {
            if (str == null || str.equals("")) {
                return "0";
            } else {
                BigDecimal bd = new BigDecimal(str.replace("￥", ""));
                bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
                return bd.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return "0";
        }
    }

    // 转换成int
    public static int parseInt(String str) {
        try {
            if (str == null || str.equals("")) {
                return 0;
            } else {
                BigDecimal bd = new BigDecimal(str);
                bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
                return Integer.parseInt(bd.toString());
            }
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }


    // 转换成价格1.00
    public static String toPrice(String str) {
        try {
            if (str == null || str.equals("")) {
                return "0";
            } else {
                BigDecimal bd = new BigDecimal(str);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                return bd.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "0";
        }

    }

    // 转换成价格1.00
    public static double parsePrice(String str) {
        try {
            if (str == null || str.equals("")) {
                return 0;
            } else {
                BigDecimal bd = new BigDecimal(str);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                return Double.parseDouble(bd.toString());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }
}