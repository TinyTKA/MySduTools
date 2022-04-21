package xyz.taouvw.mysdutools.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassDetail {
    String name = "";
    String teacher = "";
    String week = "";
    String room = "";
    int whichDay;
    int whichjie;
    String[] WeekRan;
    String[] range;
    String ClassCode = "";

    private static final String dot1 = "'";
    private static final String dot2 = "',";

    List<String> weekRange = new ArrayList<String>();
    private String pattern;

    public int getWhichDay() {
        return whichDay;
    }

    public void setWhichDay(int whichDay) {
        this.whichDay = whichDay;
    }

    public int getWhichjie() {
        return whichjie;
    }

    public void setWhichjie(int whichjie) {
        this.whichjie = whichjie;
    }

    public ClassDetail() {
        pattern = "(\\d+)";
    }

    public String[] getWeekRan() {
        return WeekRan;
    }

    public void setWeekRan(String[] weekRan) {
        WeekRan = weekRan;
    }

    public String[] getRange() {
        return range;
    }

    public void setRange(String[] range) {
        this.range = range;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public void setClassCode(String classCode) {
        ClassCode = classCode;
    }

    @Override
    public String toString() {
        return "课程名：" + name + '\n' +
                "教师名称：" + teacher + '\n' +
                "上课周：" + week + '\n' +
                "上课教师：" + room + '\n' +
                "上课时间:周" + whichDay +
                "第" + whichjie +
                "节\n 上课周：" + Arrays.toString(WeekRan) +
                "\n, 上课时间：" + Arrays.toString(range);
    }

    public String generateSQL() {
        return "'" + name + dot2 +
                dot1 + room + dot2 +
                dot1 + teacher + dot2 +
                dot1 + week + dot2 +
                dot1 + whichDay + dot2 +
                dot1 + whichjie + dot2 +
                dot1 + Arrays.toString(WeekRan) + dot2 +
                dot1 + Arrays.toString(range) + dot2 +
                dot1 + ClassCode + "')";
    }

    public ClassDetail(String name, String teacher, String week, String room) {
        this.name = name;
        this.teacher = teacher;
        this.week = week;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(week);
        while (m.find()) {
            weekRange.add(m.group());
        }
        processWeek();
        this.week = week;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    private void processWeek() {
        int i = 0;
        i = weekRange.size();
        range = new String[]{weekRange.get(i - 2), weekRange.get(i - 1)};
        if (i >= 4) {
            WeekRan = new String[i - 2];
            i = 0;
            while (i < (weekRange.size() - 2)) {
                WeekRan[i] = weekRange.get(i);
                i++;
            }
        }
    }
}
