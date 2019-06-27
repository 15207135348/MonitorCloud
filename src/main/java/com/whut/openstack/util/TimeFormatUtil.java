package com.whut.openstack.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YY on 2018-03-25.
 */
public class TimeFormatUtil {

    public static String getCurrentMsTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }
    public static String getCurrentSTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToTime(long ts,String format){
        return new SimpleDateFormat(format).format( new Date(ts));
    }
    /*
     * Date 转换为时间
     */
    public static String dateToTime(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static long utcStr2Stamp(String dateTime,String format)
    {
        dateTime = dateTime.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat f = new SimpleDateFormat(format);//注意格式化的表达式
        Date d = null;
        try {
            d = f.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert d != null;
        return d.getTime();
    }
    public static long getExpires(String issued,String expires,long current)
    {
        long during = utcStr2Stamp(expires,"yyyy-MM-dd'T'HH:mm:ss.SSS Z") -
                utcStr2Stamp(issued,"yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        return current+during;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的时间戳
     * @param format
     * @return
     */
    public static String timestamp2Date(long seconds,String format) {

        if(format == null || format.isEmpty()){
            format = "yyyy/MM/dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return 10位时间戳
     */
    public static long date2Timestamp(String date,String format){

        if(format == null || format.isEmpty()){
            format = "yyyy/MM/dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime()/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDuration(long start, long end) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = end - start;
        if (diff < 0) return null;
        // 计算差多少天
        long day = diff / nd;
        if (day > 0) return day + "天";
        // 计算差多少小时
        long hour = diff / nh;
        if (hour > 0) return hour + "小时";
        // 计算差多少分钟
        long min = diff / nm;
        if (min > 0) return min + "分钟";
        // 计算差多少秒//输出结果
        long sec = diff / ns;
        if (sec > 0) return sec + "秒";
        return diff + "毫秒";
    }
}
