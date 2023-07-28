package com.lgy.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 处理日期工具类
 */
public class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取当前时间
     * @param format 时间格式，默认是yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowTime(String format) {
        SimpleDateFormat dateFormat = null;
        String time = null;
        try {
            dateFormat = new SimpleDateFormat((format!=null)?format:"yyyy-MM-dd HH:mm:ss");
            time = dateFormat.format(new Date()).toString();
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dateFormat = null;
            time = null;
        }
        return null;
    }

    /**
     * 获取当前日期所在月份的1号
     * @param date 日期
     * @return
     */
    public static String getFirstDay(Date date){
        // 获取当月第一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    /**
     * 获取当前日期所在月份的1号
     * @param date 日期
     * @return
     */
    public static String getFirstDay(String date){

        // 获取当月第一天
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return getFirstDay(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期所在月份的最后一天的日期
     * @param date 日期
     * @return
     */
    public static String getLastDay(Date date){
        // 获取当月最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 1);// 加一个月
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为该月的第一天的前一天，如果cale.set(Calendar.DAY_OF_MONTH, 1)就是设置该月的第一天，所有0即为上一个月的最后一天
        return format.format(cale.getTime());
    }

    /**
     * 获取指定日期所在月份的最后一天的日期
     * @param date 日期
     * @return
     */
    public static String getLastDay(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return getLastDay(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将指定日期转为字符串
     * @param date 待转为字符串的日期
     * @param format 日期格式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeString(final Date date, String format) {
        if (null == date || "".equals(date)) return null;
        SimpleDateFormat sf = null;
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            if (!TextUtils.isEmpty(format)) {
                pattern = format;
            }
            sf = new SimpleDateFormat();
            sf.applyPattern(pattern);
            if (null != sf) {
                return sf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得当天0点时间
     * @return
     */
    public static Date getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得当天24点时间
     * @return
     */
    public static Date getTimesNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 对比两个日期的大小
     * @param DATE1
     * @param DATE2
     * @param dateFormatString 默认比较日期格式 "yyyy-MM-dd hh:mm"
     * @return 2.代表dt1在dt2前；1."dt1在dt2后";0.代表时间相等;-1表示比较过程出错了
     */
    public static int compareDate(String DATE1, String DATE2,String dateFormatString) {

        DateFormat df = new SimpleDateFormat((dateFormatString!=null&&!"".equals(dateFormatString))?dateFormatString:"yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return 2;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取当前日期所在的周的周一的日期
     * @param format
     * @return
     */
    public static String getWeekFirstDay(String format) {
        String weekBegin = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TextUtils.isEmpty(format) ? "yyyy-MM-dd" : format);
            Calendar cal = Calendar.getInstance();
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            // 获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayWeek == 1) {
                dayWeek = 8;
            }
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            Date mondayDate = cal.getTime();
            weekBegin = sdf.format(mondayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekBegin;
    }

    /**
     * 获取当前日期所在的周的周日的日期
     * @param format
     * @return
     */
    public static String getWeekLastDay(String format) {
        String weekEnd = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TextUtils.isEmpty(format) ? "yyyy-MM-dd" : format);
            Calendar cal = Calendar.getInstance();
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            // 获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayWeek == 1) {
                dayWeek = 8;
            }
            cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
            Date sundayDate = cal.getTime();
            weekEnd = sdf.format(sundayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekEnd;
    }

    /**
     * 返回currentDay增加num天之后的日期
     * @param currentDay 日期,必现需包含年月日，即yyyy-MM-dd
     * @param num
     * @param returnFormat 返回数据格式
     * @return
     */
    public static String getAddDay(String currentDay, int num, String returnFormat) {
        String result = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf2 = new SimpleDateFormat(TextUtils.isEmpty(returnFormat) ? "yyyy-MM-dd" : returnFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sf.parse(currentDay));
//            calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//            calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
            calendar.add(calendar.DATE, num);//把日期往后增加一天.整数往后推,负数往前移动
//            calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
            result = sf2.format(calendar.getTime());   //这个时间就是日期往后推一天的结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
