package com.buddy.connectToServer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 2017/11/30/10:59.
 *  做时间转换，将13位和19位时间戳转换为yy-MM-dd:hh-mm-ss
 *  获取请求时间，请求当前时间的前一天、一周、一个月
 * @author Songling
 * @version V 1.0.0
 */

public class RequestTime {

    //可以用localdate更便捷
    public static String getNowTime() throws ParseException {
        Date nowTime = new java.util.Date();
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime1 = dateFormat.format(nowTime);
        return dateToStamp(nowTime1);
    }

    //dur个月
    public static String getMonth(int dur) throws ParseException {
        Date nowTime = new java.util.Date();
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime1 = dateFormat.format(nowTime);
        System.out.print("nowTime = "+nowTime1 );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.add(Calendar.MONTH,-dur);//当前时间往前推dur个月
        Date date = calendar.getTime();
        String getTime = dateFormat.format(date);
        System.out.println("   precious Monthtime = "+getTime);
        return dateToStamp(getTime);
    }
    //
    public static String getWeek(int dur) throws ParseException {
        Date nowTime = new java.util.Date();
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime1 = dateFormat.format(nowTime);
        System.out.print("nowTime = "+nowTime1 );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.add(Calendar.DAY_OF_MONTH,-(7*dur));//当前时间往前推7*dur天，即dur周
        Date date = calendar.getTime();
        String getTime = dateFormat.format(date);
        System.out.println("    precious Weektime = "+getTime);
        return dateToStamp(getTime);
    }

    public static String getDay(int dur) throws ParseException {
        Date nowTime = new java.util.Date();

        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime1 = dateFormat.format(nowTime);
        System.out.print("nowTime = "+nowTime1 );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);

        calendar.add(Calendar.DAY_OF_MONTH,-dur );//当前时间往前推dur天
        Date date = calendar.getTime();
        String getTime = dateFormat.format(date);
        System.out.println("   precious Daytime = "+getTime);
        return dateToStamp(getTime);
    }

    public static String stampToMonth(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDay(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToMinute(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToSecond(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String longToMonth(long ReversedTime) {

        String tsStr = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            tsStr = sdf.format(Long.MAX_VALUE - ReversedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    public static Integer longToDay(long ReversedTime) {

        String tsStr = null;
        DateFormat sdf = new SimpleDateFormat("dd");
        try {
            tsStr = sdf.format(Long.MAX_VALUE - ReversedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(tsStr);
    }

    public static String timeFrag(int arg) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String str;
        if(arg%2 == 0) {
            int dd = (arg-2)/2;
            str = dd+":30";
        }else {
            int dd = (arg-1)/2;
            str = dd+":00";
        }
        Date dt=sdf.parse(str);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MINUTE,30);//加30分钟
        Date dt1=rightNow.getTime();
        String dt2=dt1.toString().substring(11,16);
        //System.out.println(str+"~"+dt2);
        return str+"~"+dt2;
    }
    public static float getClock(int d1){
        int clock;
        int d = Math.abs(d1);
        clock = d / 60;
        float min = d - 60 * clock;
        return (float)clock+min/60;
    }
    public static Float getDurTime(int dur){
        Float d = Math.abs((float)dur)/1000;
        Float minSum = d / 60;
        return minSum/60;
    }


}
