package xyz.taouvw.mysdutools.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    private static DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");

    /*
    dateinfo[0]:当前周
    dateinfo[1]:当前周几
     */
    public static int[] getDayAndWeek(String beginDate) {
        int[] dateinfo = new int[]{0, 0};

        Date bd = null;
        try {
            bd = df.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        long time = date.getTime();
        assert bd != null;
        long deltaTime = time - bd.getTime();
        dateinfo[0] = (int) (deltaTime / 1000 / 60 / 60 / 24 / 7) + 1;
        dateinfo[1] = (int) (deltaTime / 1000 / 60 / 60 / 24 % 7);
        if (dateinfo[1] == 0) {
            dateinfo[1] = 7;
            dateinfo[0] -= 1;
        }
        return dateinfo;
    }

    /**
     * 根据学期开始时间和当前周获取本周七天对应的公历日期
     *
     * @param beginDate   ：学期开始时间
     * @param nowWeek：当前周
     * @return ：数组末位保存本周第一天在几月
     */
    public static int[] getDayOfSpecialWeek(String beginDate, int nowWeek) {
        int[] DayOfSpecialWeek = new int[8];
        Calendar calendar = Calendar.getInstance();
        Date parse = null;
        try {
            parse = df.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        calendar.add(Calendar.WEEK_OF_YEAR, nowWeek);
        for (int i = 0; i < 7; i++) {
            DayOfSpecialWeek[i] = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        DayOfSpecialWeek[7] = calendar.get(Calendar.MONTH) + 1;
        return DayOfSpecialWeek;
    }
}
