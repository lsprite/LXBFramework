package com.lxb.view.wheel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lxb.framework.R;
import com.lxb.view.wheel.util.ArrayWheelAdapter;
import com.lxb.view.wheel.util.GetWeek;
import com.lxb.view.wheel.util.NumericWheelAdapter;
import com.lxb.view.wheel.util.OnWheelChangedListener;
import com.lxb.view.wheel.util.WheelView;

import java.util.Calendar;

/**
 * @COMPANY:sunnyTech
 * @CLASS:DateTimeWheelView
 * @DESCRIPTION:日/时/分
 * @AUTHOR:Sunny
 * @VERSION:v1.0
 * @DATE:2014-8-22 下午3:58:35
 */
public class DateTimeWheelView {
    /**
     * 当前年
     */
    private int year;

    /**
     * 当前月
     */
    private int month;

    /**
     * 当前日
     */
    private int day;

    /**
     * 当前时
     */
    private int hour;

    /**
     * 当前分
     */
    private int minute;

    /**
     * 当前秒
     */
    private int second;

    // 年长度,100就表示前后100年
    private int yearLength = 100;

    // 开始年
    private int startYear;

    private Context context;

    private Activity activity;

    // 日期时间控件对象
    private WheelView yearWheelView;
    private WheelView monthWheelView;
    private WheelView dayWheelView;
    private WheelView weekWheelView;
    private WheelView hourWheelView;
    private WheelView minuteWheelView;
    private WheelView secondWheelView;

    // 控件字体大小
    private int textSize = 16;
    GetWeek weeks;
    String weekTexts[] = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五",
            "星期六", "星期日"};
    private boolean weekSynchronization = false;
    private AlertDialog dialog;
    public View okButton, cancelButton;

    /**
     * 年是否可见
     */
    private boolean isShowYear = true;
    /**
     * 月是否显示
     */
    private boolean isShowMonth = true;

    /**
     * 日是否显示
     */
    private boolean isShowDay = true;

    /**
     * 星期是否显示
     */
    private boolean isShowWeek = false;

    /**
     * 时是否显示
     */
    private boolean isShowHour = true;

    /**
     * 分钟是否显示
     */
    private boolean isShowMinute = true;

    /**
     * 秒是否显示
     */
    private boolean isShowSecond = false;

    public DateTimeWheelView(Context context) {
        final Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DATE);
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
        this.second = c.get(Calendar.SECOND);
        this.context = context;
        startYear = this.year;
        Log.i("DateTimeWheelView", "year:" + year + ",month:" + month + ",day:"
                + day + ",hour:" + hour + ",minute:" + minute + ",second:"
                + second);
        textSize = context.getResources().getDimensionPixelOffset(
                R.dimen.wheel_text_size);
    }

    public Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(getYearCurrentItem(), getMonthCurrentItem() - 1,
                getDayCurrentItem(), getHourCurrentItem(),
                getMinuteCurrentItem(), getSecondCurrentItem());
        return c;
    }

    // public DateTimeWheelView(Activity context)
    // {
    // final Calendar c = Calendar.getInstance();
    // this.year = c.get(Calendar.YEAR);
    // this.month = c.get(Calendar.MONTH);
    // this.day = c.get(Calendar.DATE);
    // this.hour = c.get(Calendar.HOUR_OF_DAY);
    // this.minute = c.get(Calendar.MINUTE);
    // this.second = c.get(Calendar.SECOND);
    // this.activity = context;
    // startYear = this.year;
    // Log.i("DateTimeWheelView", "year:" + year + ",month:" + month + ",day:" +
    // day + ",hour:" + hour + ",minute:" + minute + ",second:" + second);
    // textSize =
    // context.getResources().getDimensionPixelOffset(R.dimen.textsize18);
    // }

    /***************
     * 设置默认时间
     *************/
    public void setDefaultTime(Calendar calendar) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DATE);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);
    }

    /*********
     * 创建时间对话框
     ******************/
    public void createTimeDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogDateTime = inflater.inflate(R.layout.wheel_datetime_layout,
                null);
        yearWheelView = (WheelView) dialogDateTime.findViewById(R.id.year);
        monthWheelView = (WheelView) dialogDateTime.findViewById(R.id.month);
        dayWheelView = (WheelView) dialogDateTime.findViewById(R.id.day);
        weekWheelView = (WheelView) dialogDateTime.findViewById(R.id.week);
        hourWheelView = (WheelView) dialogDateTime.findViewById(R.id.hour);
        minuteWheelView = (WheelView) dialogDateTime.findViewById(R.id.minute);
        secondWheelView = (WheelView) dialogDateTime.findViewById(R.id.second);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDate(yearWheelView, monthWheelView, dayWheelView,
                        weekWheelView);
                // System.out.println("改变了==========");
            }
        };
        // 年
        yearWheelView.setAdapter(new NumericWheelAdapter(
                startYear - yearLength, startYear + yearLength));
        yearWheelView.setCyclic(true);
        yearWheelView.setCurrentItem(this.year - startYear - yearLength - 1);
        yearWheelView.TEXT_SIZE = this.textSize;
        yearWheelView.setLabel(context.getString(R.string.Year));
        yearWheelView.addChangingListener(listener);
        if (isShowYear) {
            yearWheelView.setVisibility(View.VISIBLE);
        } else {
            yearWheelView.setVisibility(View.GONE);
        }

        // 月
        monthWheelView.setAdapter(new NumericWheelAdapter(1, 12, "%1$,02d"));
        monthWheelView.setCurrentItem(month);
        monthWheelView.setCyclic(true);
        monthWheelView.setLabel(context.getString(R.string.Month));
        monthWheelView.addChangingListener(listener);
        monthWheelView.TEXT_SIZE = this.textSize;
        if (isShowMonth) {
            monthWheelView.setVisibility(View.VISIBLE);
        } else {
            monthWheelView.setVisibility(View.GONE);
        }

        // 日
        int maxDays = getCalendar().getActualMaximum(Calendar.DAY_OF_MONTH);
        dayWheelView.setAdapter(new NumericWheelAdapter(1, maxDays, "%1$,02d"));
        dayWheelView.setCurrentItem(this.day - 1);
        dayWheelView.setCyclic(true);
        dayWheelView.TEXT_SIZE = this.textSize;
        dayWheelView.setLabel("日");
        if (isShowDay) {
            dayWheelView.setVisibility(View.VISIBLE);
        } else {
            dayWheelView.setVisibility(View.GONE);
        }

        // 星期
        weeks = new GetWeek();
        int curWeek = weeks.getWeek(this.year, this.month + 1, this.day);
        curWeek--;

        weekWheelView.setAdapter(new ArrayWheelAdapter<String>(weekTexts, 7));
        weekWheelView.setCurrentItem(curWeek);
        weekWheelView.setCyclic(true);
        weekWheelView.setEnabled(false);
        weekWheelView.TEXT_SIZE = this.textSize;
        if (isShowWeek) {
            weekWheelView.setVisibility(View.VISIBLE);
        } else {
            weekWheelView.setVisibility(View.GONE);
        }

        // 小时
        hourWheelView.setAdapter(new NumericWheelAdapter(0, 23, "%1$,02d"));
        hourWheelView.setCurrentItem(this.hour);
        hourWheelView.setCyclic(true);
        hourWheelView.TEXT_SIZE = this.textSize;
        hourWheelView.setLabel("时");
        if (isShowHour) {
            hourWheelView.setVisibility(View.VISIBLE);
        } else {
            hourWheelView.setVisibility(View.GONE);
        }

        // 分钟
        minuteWheelView.setAdapter(new NumericWheelAdapter(0, 59, "%1$,02d"));
        minuteWheelView.setCurrentItem(this.minute);
        minuteWheelView.setCyclic(true);
        minuteWheelView.TEXT_SIZE = this.textSize;
        minuteWheelView.setLabel("分");
        if (isShowMinute) {
            minuteWheelView.setVisibility(View.VISIBLE);
        } else {
            minuteWheelView.setVisibility(View.GONE);
        }

        // 秒
        secondWheelView.setAdapter(new NumericWheelAdapter(0, 59, "%1$,02d"));
        secondWheelView.setCurrentItem(this.second);
        secondWheelView.setCyclic(true);
        secondWheelView.TEXT_SIZE = this.textSize;
        secondWheelView.setLabel(context.getString(R.string.Second));
        if (isShowSecond) {
            secondWheelView.setVisibility(View.VISIBLE);
        } else {
            secondWheelView.setVisibility(View.GONE);

        }
        // 创建对话框
        dialog = new AlertDialog.Builder(context).create();

        dialog.show();
        dialog.getWindow().setContentView(dialogDateTime);
        okButton = dialog.getWindow().findViewById(R.id.OkButton);
        cancelButton = dialog.getWindow().findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param week
     * @description:更改星期时,同步修改日期,需要再详细测试.
     * @author:Sunny
     * @return:void
     */
    void updateDate(WheelView year, WheelView month, WheelView day,
                    WheelView week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                startYear - (yearLength - year.getCurrentItem()));
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        System.out.println("year:"
                + (startYear - (yearLength - year.getCurrentItem())));
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        day.setAdapter(new NumericWheelAdapter(1, maxDays, "%1$,02d"));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        if (weekSynchronization) {
            weeks = new GetWeek();
            int newWeek = weeks.getWeek(getYearCurrentItem(),
                    getMonthCurrentItem(), getDayCurrentItem());
            newWeek--;
            week.setAdapter(new ArrayWheelAdapter<String>(weekTexts, 7));
            week.setCurrentItem(newWeek);
        } else
            weekSynchronization = true;
    }

    /****************
     * 设置年的长度
     ****************/
    public void setYearLength(int length) {
        this.yearLength = length;
    }

    public void setStartYear(int years) {
        startYear = years;
    }

    /************
     * 获得年限长度
     ***************/
    public int getYearLength() {
        return this.yearLength;
    }

    /************
     * 获得哪一年
     ***************/
    public int getYearCurrentItem() {
        // Log.i(ILog.tagString, "getYearCurrentItem:" +
        // yearWheelView.getCurrentItem());
        return yearWheelView.getCurrentItem() + startYear - yearLength;
    }

    /************
     * 获得哪一月
     ***************/
    public int getMonthCurrentItem() {
        // Log.i(ILog.tagString, "getMonthCurrentItem:" +
        // monthWheelView.getCurrentItem());
        return monthWheelView.getCurrentItem() + 1;
    }

    /************
     * 获得第几天
     ***************/
    public int getDayCurrentItem() {
        // Log.i(ILog.tagString, "getDayCurrentItem:" +
        // dayWheelView.getCurrentItem());
        return dayWheelView.getCurrentItem() + 1;
    }

    /************
     * 获得第几小时
     ***************/
    public int getHourCurrentItem() {
        // Log.i(ILog.tagString, "getHourCurrentItem:" +
        // hourWheelView.getCurrentItem());
        return hourWheelView.getCurrentItem();
    }

    /************
     * 获得第几分钟
     ***************/
    public int getMinuteCurrentItem() {
        // Log.i(ILog.tagString, "getMinuteCurrentItem:" +
        // minuteWheelView.getCurrentItem());
        return minuteWheelView.getCurrentItem();
    }

    /************
     * 获得第几秒
     ***************/
    public int getSecondCurrentItem() {
        // Log.i(ILog.tagString, "getSecondCurrentItem:" +
        // secondWheelView.getCurrentItem());
        return secondWheelView.getCurrentItem();
    }

    /************
     * 获得星期几
     ***************/
    public int getWeekCurrentItem() {
        // Log.i(ILog.tagString, "getWeekCurrentItem:" +
        // weekWheelView.getCurrentItem());
        return weekWheelView.getCurrentItem() + 1;
    }

    /************
     * 设置字体大小
     ***************/
    public void setTextSizes(int size) {
        this.textSize = size;
    }

    /************
     * 获得当前时间(日——时——分)串
     ***************/
    public String getDayHourMinuteString() {
        String times_string = ""
                + getDayCurrentItem()
                + "-"
                + getHourCurrentItem()
                + ":"
                + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem()
                : getMinuteCurrentItem());
        return times_string;
    }

    /**
     * @return
     * @description:获得当前选中的日期时间(yyyy-MM-dd HH:mm:ss)
     * @author:Sunny
     * @return:String
     */
    public String getDateTimeString() {
        String ret = getYearCurrentItem()
                + "-"
                + ((getMonthCurrentItem() < 10) ? "0" + getMonthCurrentItem()
                : getMonthCurrentItem())
                + "-"
                + ((getDayCurrentItem() < 10) ? "0" + getDayCurrentItem()
                : getDayCurrentItem())
                + " "
                + ((getHourCurrentItem() < 10) ? "0" + getHourCurrentItem()
                : getHourCurrentItem())
                + ":"
                + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem()
                : getMinuteCurrentItem())
                + ":"
                + ((getSecondCurrentItem() < 10) ? "0" + getSecondCurrentItem()
                : getSecondCurrentItem());
        return ret;
    }

    public String getDateTimeString2() {
        String ret = getYearCurrentItem()
                + "-"
                + ((getMonthCurrentItem() < 10) ? "0" + getMonthCurrentItem()
                : getMonthCurrentItem())
                + "-"
                + ((getDayCurrentItem() < 10) ? "0" + getDayCurrentItem()
                : getDayCurrentItem())
                + " "
                + ((getHourCurrentItem() < 10) ? "0" + getHourCurrentItem()
                : getHourCurrentItem())
                + ":"
                + ((getMinuteCurrentItem() < 10) ? "0" + getMinuteCurrentItem()
                : getMinuteCurrentItem());
        return ret;
    }

    /**
     * @return
     * @description:获得当前选中的日期时间(yyyy-MM-dd)
     * @author:Sunny
     * @return:String
     */
    public String getDateString() {
        String ret = getYearCurrentItem()
                + "-"
                + ((getMonthCurrentItem() < 10) ? "0" + getMonthCurrentItem()
                : getMonthCurrentItem())
                + "-"
                + ((getDayCurrentItem() < 10) ? "0" + getDayCurrentItem()
                : getDayCurrentItem());
        return ret;
    }

    public boolean isShowYear() {
        return isShowYear;
    }

    public void setShowYear(boolean isShowYear) {
        this.isShowYear = isShowYear;
    }

    public boolean isShowMonth() {
        return isShowMonth;
    }

    public void setShowMonth(boolean isShowMonth) {
        this.isShowMonth = isShowMonth;
    }

    public boolean isShowDay() {
        return isShowDay;
    }

    public void setShowDay(boolean isShowDay) {
        this.isShowDay = isShowDay;
    }

    public boolean isShowWeek() {
        return isShowWeek;
    }

    public void setShowWeek(boolean isShowWeek) {
        this.isShowWeek = isShowWeek;
    }

    public boolean isShowHour() {
        return isShowHour;
    }

    public void setShowHour(boolean isShowHour) {
        this.isShowHour = isShowHour;
    }

    public boolean isShowMinute() {
        return isShowMinute;
    }

    public void setShowMinute(boolean isShowMinute) {
        this.isShowMinute = isShowMinute;
    }

    public boolean isShowSecond() {
        return isShowSecond;
    }

    public void setShowSecond(boolean isShowSecond) {
        this.isShowSecond = isShowSecond;
    }

    public void finish() {
        dialog.dismiss();
    }

    public int get2Days(int year)// 计算某一年2月份有多少天
    {
        System.out.println("计算某一年2月份有多少天:" + year);
        Calendar c = Calendar.getInstance();
        c.set(year, 2, 1);// 0-11->1-12 将日期设置为某一年的3月1号
        c.add(Calendar.DAY_OF_MONTH, -1);// 将日期减去一天，即日期变成2月的最后一天
        return c.get(Calendar.DAY_OF_MONTH);// 返回二月最后一天的具体值
    }
}
