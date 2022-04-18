package xyz.taouvw.mysdutools.utils;

import android.util.Log;
import android.webkit.JavascriptInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class InJavaScriptLocalObj {
    //一定也要加上这个注解,否则没有用

    @JavascriptInterface
    public void onHtml(String value) {
        Document document = Jsoup.parseBodyFragment(value);
        Elements allElements = document.getAllElements();
        for (Element e:allElements
             ) {
            String text = e.text();
            Log.e("Elem:",text);
        }
    }
}
