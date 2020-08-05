package com.muke.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    public static Date formDate(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    //格式化规则
        String strDate= sdf.format(date); //格式化成yyyy-MM-dd格式的时间字符串
        Date newDate =sdf.parse(strDate);
        java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
        return resultDate;
    }

    public static Date getToday(Date date) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return formDate(calendar.getTime());
    }

    public static Date getYestoday(Date date) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return formDate(calendar.getTime());
    }

    public static Date getTomorrow(Date date) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return formDate(calendar.getTime());
    }

    public static Date getWeekAgo(Date date) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return formDate(calendar.getTime());
    }

    public static Date getMonthAgo(Date date) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -30);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return formDate(calendar.getTime());
    }

    public static String getTimeFormatText(Long date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    public static String formatDate1(String dateStr) {
        String[] aStrings = dateStr.split(" ");
        // 5
        if (aStrings[1].equals("Jan")) {
            aStrings[1] = "01";
        }
        if (aStrings[1].equals("Feb")) {
            aStrings[1] = "02";
        }
        if (aStrings[1].equals("Mar")) {
            aStrings[1] = "03";
        }
        if (aStrings[1].equals("Apr")) {
            aStrings[1] = "04";
        }
        if (aStrings[1].equals("May")) {
            aStrings[1] = "05";
        }
        if (aStrings[1].equals("Jun")) {
            aStrings[1] = "06";
        }
        if (aStrings[1].equals("Jul")) {
            aStrings[1] = "07";
        }
        if (aStrings[1].equals("Aug")) {
            aStrings[1] = "08";
        }
        if (aStrings[1].equals("Sep")) {
            aStrings[1] = "09";
        }
        if (aStrings[1].equals("Oct")) {
            aStrings[1] = "10";
        }
        if (aStrings[1].equals("Nov")) {
            aStrings[1] = "11";
        }
        if (aStrings[1].equals("Dec")) {
            aStrings[1] = "12";
        }
        return aStrings[5] + "-" + aStrings[1] + "-" + aStrings[2] + " " + aStrings[3];
    }

    public static void main(String[] args) throws ParseException {
        Date date = new Date();
//        System.out.println(getTimeFormatText(Long.parseLong("1550505608078")));
        System.out.println(DateUtil.getToday(date));
        System.out.println( DateUtil.getTomorrow(date));
        System.out.println( DateUtil.getWeekAgo(date));
        System.out.println( DateUtil.getMonthAgo(date));

    }
}
