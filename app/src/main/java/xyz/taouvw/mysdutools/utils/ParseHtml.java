package xyz.taouvw.mysdutools.utils;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.taouvw.mysdutools.Bean.ClassDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHtml {

    public static List<ClassDetail> parse(Document Doc) {
        List<ClassDetail> classes = new ArrayList<>();
        int i = 0;
        int j = 0;
        // 获取整个课程表
        Elements tbody = Doc.getElementsByTag("tbody");
        Element element = tbody.get(0);
        if (element != null) {
            // 获取tbody下的所有行
            Elements tr = element.getElementsByTag("tr");
            // 遍历所有行，同时i作为计数，表示第几节
            for (Element e : tr) {
                // 第一行和最后一行跳过
                if (i == 0 || i == 6) {
                    i++;
                    continue;
                } else {
                    j = 0;
                    // 获取单行的每个td，表示每一列
                    Elements td = e.getElementsByTag("td");
                    // 遍历所有列，j作为周几
                    for (Element sub_td : td) {
                        j++;
                        // 获取每列下的"kbcontent"
                        Elements kbcontent = sub_td.getElementsByClass("kbcontent");
                        for (Element kb : kbcontent) {
                            //跳过没课
                            if (kb.text().equals("")) {
                                continue;
                            }
                            ClassDetail myclass = getEveryClass(kb);
                            myclass.setWhichDay(j);
                            myclass.setWhichjie(i);
                            classes.add(myclass);
                        }
                    }
                    i++;
                }
            }
            System.out.println();
        }


        return classes;
    }

    public static ClassDetail getEveryClass(Element kb) {
        ClassDetail classDetail = new ClassDetail();
        String s = kb.text();
        if (s.length() != 0) {

            Elements teacher = kb.getElementsByAttributeValue("title", "教师");
            if (teacher.isEmpty()) {
                classDetail.setTeacher("空");
            } else {
                classDetail.setTeacher(teacher.get(0).text());
            }

            Elements week = kb.getElementsByAttributeValue("title", "周次(节次)");
            if (week.isEmpty()) {
                classDetail.setWeek("空");
            } else {
                classDetail.setWeek(week.get(0).text());
            }
            Elements room = kb.getElementsByAttributeValue("title", "教室");
            if (room.isEmpty()) {
                classDetail.setRoom("空");
            } else {
                classDetail.setRoom(room.get(0).text().substring(7));
            }

            String[] s1 = s.split("\\) ");
            if (s1.length >= 2) {
                int i = s1[0].lastIndexOf("(");
                classDetail.setName(s1[0].substring(0, i));
            }
        }
        return classDetail;
    }

    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }

        Pattern pattern1 = Pattern.compile("\\\\\"");
        Matcher matcher1 = pattern1.matcher(str);
        String s = matcher1.replaceAll("\"");
        Log.e("c", s.charAt(1) + "" + s.charAt(2));
        Pattern pattern2 = Pattern.compile("(\\n)");
        Matcher matcher2 = pattern2.matcher(s);
        s.charAt(0);
        s.replace('\'', ' ');
        return s;
    }
}
