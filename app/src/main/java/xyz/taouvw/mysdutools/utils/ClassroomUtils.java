package xyz.taouvw.mysdutools.utils;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.taouvw.mysdutools.Bean.ClassRoomDetail;

public class ClassroomUtils {
    private static final String url = "https://bkzhjx.wh.sdu.edu.cn/jiaowu/kxjsgl/kxjsgl.do?method=queryKxxxByJs_sddxFP&typewhere=jwyx";

    public static List<ClassRoomDetail> parseHtmlForData(Document document, String whichday, String where) {
        Pattern pattern = Pattern.compile(where);
        Matcher matcher;
        List<ClassRoomDetail> classRoomDetails = new ArrayList<>();
        Element kbtable = document.getElementById("kbtable");
        Log.e("TAG", "parseHtmlForData: " + kbtable.text());
        Elements tbody = kbtable.getElementsByTag("tbody");
        Elements trs = tbody.get(0).getElementsByTag("tr");
        for (int i = 0; i < trs.size(); i++) {
            String text2 = "";
            Element element = trs.get(i);
            Elements td = element.getElementsByTag("td");
            Element element1 = td.get(0);
            String text = element1.text();
            if (text.substring(0, 8).equals("青岛实验教学中心")) {
                continue;
            } else if (text.substring(0, 4).equals("青岛校区")) {
                text2 = text.replaceAll("青岛校区", "");
            }
            matcher = pattern.matcher(text2.toUpperCase(Locale.ROOT));
            if (!matcher.find()) {
                continue;
            }
            String s = text2.replaceAll(" ", "");
            ClassRoomDetail c = new ClassRoomDetail();
            c.setName(s.substring(0, s.lastIndexOf("(")));
            for (int j = 1; j <= 5; j++) {
                Element element2 = td.get(j);
                Elements font = element2.getElementsByTag("font");
                if (font.size() == 1) {
                    c.addoccupiedTime(j);
                } else {
                    c.addfreeTime(j);
                }
            }
            c.setSearchDay(Integer.parseInt(whichday));
            classRoomDetails.add(c);
            Log.e("班级讯息", "parseHtmlForData: " + c.toString());
        }
        return classRoomDetails;

    }


}
