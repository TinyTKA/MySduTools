package xyz.taouvw.mysdutools.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

public class DateUtils {
    /*
    dateinfo[0]:当前周
    dateinfo[1]:当前周几
     */
    public static int[] getDayAndWeek(String beginDate) {
        int[] dateinfo = new int[]{0, 0};
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date bd = null;
        try {
            bd = df.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        long time = date.getTime();
        long deltaTime = time - bd.getTime();
        dateinfo[0] = (int) (deltaTime / 1000 / 60 / 60 / 24 / 7) + 1;
        dateinfo[1] = (int) (deltaTime / 1000 / 60 / 60 / 24 % 7);
        if (dateinfo[1] == 0) {
            dateinfo[1] = 7;
            dateinfo[0] -= 1;
        }
        return dateinfo;
    }
}
