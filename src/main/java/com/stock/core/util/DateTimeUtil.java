package com.stock.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化工具类
 */
public class DateTimeUtil {

    /**
     * 格式 yyyy年MM月dd日 HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static String getDateTimeDisplayString(LocalDateTime dateTime) {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        String strDate2 = dtf2.format(dateTime);

        return strDate2;
    }

    /**
     * 获取某一天第一秒
     * @param date
     * @return
     */
    public static long getFirstSecondOfDay(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.SECOND, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.MILLISECOND, 0);
        return lastDate.getTimeInMillis();
    }

    /**
     * 获取某一天最后一秒
     * @param date
     * @return
     */
    public static long getLastSecondOfDay(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.set(Calendar.HOUR_OF_DAY, 23);
        lastDate.set(Calendar.SECOND, 59);
        lastDate.set(Calendar.MINUTE, 59);
        lastDate.set(Calendar.MILLISECOND, 0);
        return lastDate.getTimeInMillis();
    }
}
