package com.lxb.util;

import java.text.DecimalFormat;

public class ToChinaNum {

    /**
     * 大写数字
     */
    private static final String[] NUMBERS = {"零", "一", "二", "三", "四", "五",
            "六", "七", "八", "九"};
    /**
     * 整数部分的单位
     */
    private static final String[] IUNIT = {"点", "十", "百", "千", "万", "十", "百",
            "千", "亿", "十", "百", "千", "万", "十", "百", "千"};

    /**
     * 小数部分的单位
     */
    // private static final String[] DUNIT = { "角", "分", "厘" };
    private static final String[] DUNIT = {"", "", ""};

    public static String toChinese(Double amount) {
        String res = toChinese(amount, "1");
        if (res.endsWith(IUNIT[0])) {
            return res.substring(0, res.length() - 1);
        } else {
            return res;
        }

    }

    public static String toChinese(Double amount, String currency) {
        DecimalFormat df = new DecimalFormat("###,##0.00");
        String str = df.format(amount);
        return strToChinese(str, currency);
    }

    public static String getCurrencySymbol(String currency) {
        if (currency.equalsIgnoreCase("1")) {
            return "￥";
        } else if (currency.equalsIgnoreCase("2")) {
            return "＄";
        } else if (currency.equalsIgnoreCase("3")) {
            return "€";
        } else if (currency.equalsIgnoreCase("4")) {
            return "￡";
        } else {
            return "￥";
        }
    }

    public static String getCurrencyUnit(String unit, String currency) {
        if (unit.equalsIgnoreCase(IUNIT[0])) {
            if (currency.equalsIgnoreCase("4")) {
                return "镑";
            } else {
                return IUNIT[0];
            }
        } else {
            return unit;
        }
    }

    /**
     * 得到大写金额。
     */
    public static String strToChinese(String str) {
        return strToChinese(str, "1");
    }

    /**
     * 得到不同币种的大写金额。
     */
    public static String strToChinese(String str, String currency) {
        DecimalFormat df = new DecimalFormat("00.00");
        str = str.replaceAll(",", "");// 去掉","
        str = df.format(Double.valueOf(str));
        String integerStr;// 整数部分数字
        String decimalStr;// 小数部分数字

        // 初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        } // integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
        if (!integerStr.equals("")) {
            integerStr = Long.toString(Long.parseLong(integerStr));
            if (integerStr.equals("0")) {
                integerStr = "";
            }
        } // overflow超出处理能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            System.out.println(str + ":超出处理能力");
            return str;
        }

        int[] integers = toArray(integerStr);// 整数部分数字
        boolean isMust5 = isMust5(integerStr);// 设置万单位
        int[] decimals = toArray(decimalStr);// 小数部分数字
        return getChineseInteger(integers, isMust5, currency)
                + getChineseDecimal(decimals);
    }

    /**
     * 整数部分和小数部分转换为数组，从高位至低位
     */
    private static int[] toArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    /**
     * 得到中文金额的整数部分。
     */
    private static String getChineseInteger(int[] integers, boolean isMust5,
                                            String currency) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        for (int i = 0; i < length; i++) { // 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
            // // 特殊情况：10(拾元、壹拾元、壹拾万元、拾万元)
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13)// 万(亿)(必填)
                    key = IUNIT[4];
                else if ((length - i) == 9)// 亿(必填)
                    key = IUNIT[8];
                else if ((length - i) == 5 && isMust5)// 万(不必填)
                    key = IUNIT[4];
                else if ((length - i) == 1) {// 元(必填)
                    key = getCurrencyUnit(IUNIT[0], currency);
                } // 0遇非0时补零，不包含最后一位
                if ((length - i) > 1 && integers[i + 1] != 0)
                    key += NUMBERS[0];
            }
            chineseInteger.append(integers[i] == 0 ? key
                    : (NUMBERS[integers[i]] + getCurrencyUnit(IUNIT[length - i
                    - 1], currency)));
        }
        return chineseInteger.toString();
    }

    private static String getChineseInteger(int[] integers, boolean isMust5) {
        return getChineseInteger(integers, isMust5, "1");
    }

    /**
     * 得到中文金额的小数部分。
     */
    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for (int i = 0; i < decimals.length; i++) { // 舍去3位小数之后的
            if (i == 3)
                break;
            chineseDecimal.append(decimals[i] == 0 ? ""
                    : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    /**
     * 判断第5位数字的单位"万"是否应加。
     */
    private static boolean isMust5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) { // 取得从低位数，第5到第8位的字串
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

//	public static void main(String[] args) {
//		System.out.println(toChinese(201.15));
//	}
}
