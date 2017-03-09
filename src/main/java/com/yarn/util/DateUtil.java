package com.yarn.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhangdi on 2016/12/30.
 */
public class DateUtil {

    /***
     * 获取指定格式的当前日期
     *
     * @param strFormat
     * @return
     */
    public static String getNowDateFormat(String strFormat)
    {
        Date tempDate = new Date(Calendar.getInstance().getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        return dateFormat.format(tempDate);
    }
}
