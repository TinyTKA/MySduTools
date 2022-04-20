package xyz.taouvw.mysdutools;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getDayOfSpecialWeek() {
        String beginDate = "2022年02月20日";
        int nowWeek = 2;
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
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
            System.out.println(DayOfSpecialWeek[i]);
        }
        DayOfSpecialWeek[7] = calendar.get(Calendar.MONTH) + 1;
        System.out.println(DayOfSpecialWeek[7]);
    }
}