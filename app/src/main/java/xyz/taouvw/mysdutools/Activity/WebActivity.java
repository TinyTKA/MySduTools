package xyz.taouvw.mysdutools.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;

import xyz.taouvw.mysdutools.Bean.ClassDetail;
import xyz.taouvw.mysdutools.R;
import xyz.taouvw.mysdutools.utils.InJavaScriptLocalObj;
import xyz.taouvw.mysdutools.utils.ParseHtml;
import xyz.taouvw.mysdutools.utils.SQLUtils;

public class WebActivity extends AppCompatActivity {
    //    public static final String LOGIN_URL = "https://pass.sdu.edu.cn/cas/login?service=http%3A%2F%2Fbkzhjx.wh.sdu.edu.cn%2Fsso.jsp";
//    public static final String USER_AGENT = "User-Agent";
//    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36 Edg/100.0.1185.36";
//    Map<String, String> params = new LinkedHashMap<>();
//    Map<String, String> cookies;
//    Map<String, String> requestHeaders = new LinkedHashMap<>();
    FloatingActionButton submit;
    WebView webView;
    EditText passwd_editText;
    EditText user_editText;
    private SQLUtils sqlUtils;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0x11: {
                    passwd_editText.setEnabled(true);
                    user_editText.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "获取服务器信息成功，请继续登录操作", Toast.LENGTH_LONG).show();
                }
                break;
                case 0x01: {
                    webView.evaluateJavascript("document.getElementsByClassName(\"content_box\")[0].innerHTML", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            if (!s.equals("null")) {
                                String s3 = ParseHtml.unicodeToString(s);
                                Document doc = Jsoup.parse(s3);
                                List<ClassDetail> parse = ParseHtml.parse(doc);
                                if (parse.size() == 0) {
                                    Toast.makeText(getApplicationContext(), "解析失败，请重试", Toast.LENGTH_SHORT).show();
                                    return;
                                }
//                                for (ClassDetail c : parse) {
////                                    Log.e("Class", "onReceiveValue: " + c.toString());
//                                    Log.e("Class", "onReceiveValue: " + c.generateSQL());
//                                }
                                // 将课程表保存到数据库中
                                boolean b = sqlUtils.addClassesInfo(parse, getApplicationContext());
                                if (b) {
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "再点击一次以完成解析", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            return true;
        }
    });
    private static final String loadUrl = "https://pass.sdu.edu.cn/cas/login?service=http%3A%2F%2Fbkzhjx.wh.sdu.edu.cn%2Fsso.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        init();
    }

    public void init() {
        //初始化参数

        submit = this.findViewById(R.id.submit_web);
        webView = this.findViewById(R.id.myweb);
        sqlUtils = new SQLUtils(WebActivity.this, "class.db", null, 1);
//        passwd_editText = this.findViewById(R.id.UserPasswd);
//        user_editText = this.findViewById(R.id.UserCode);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://bkzhjx.wh.sdu.edu.cn/jsxsd/xskb/xskb_list.do");
                Message message = new Message();
                message.what = 0x01;
                handler.sendMessage(message);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(loadUrl);
    }
//        webView.loadUrl("file:///android_asset/web/my.html");
//        //通过JSoup发起请求，解析登录需要的参数
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //发起连接
//                    Connection mozilla = Jsoup.connect(loadUrl).header(USER_AGENT, USER_AGENT_VALUE);
//                    // 获得返回html文档
////                    Document doc = mozilla.get();
//                    Connection.Response res = mozilla.execute();
//                    cookies = res.cookies();
//                    Set<String> strings = cookies.keySet();
//                    for (String s :
//                            strings) {
//                        Log.e("cookies:", "key: " + s);
//                    }
//                    Collection<String> values = cookies.values();
//                    for (String s :
//                            values) {
//                        Log.e("cookies", "value: " + s);
//                    }
//                    doc = Jsoup.parse(res.body());
//
//                    //获得第一个参数lt
//                    Element lt = doc.getElementById("lt");
//
//                    lt_source = lt.attr("value");
//                    //获得第二个参数execution
//                    Elements execution = doc.getElementsByAttributeValue("name", "execution");
//                    for (Element e : execution
//                    ) {
//                        execution_source = e.attr("value");
//                    }
//                    // 获得第三个参数eventID
//                    Elements eventID = doc.getElementsByAttributeValue("name", "_eventId");
//                    for (Element e : eventID
//                    ) {
//                        event_source = e.attr("value");
//                    }
//                    Log.e("发起请求", "init: " + lt_source + "  exe:" + execution_source + "  event:" + event_source);
//                    Message message = new Message();
//                    message.what = 0x11;
//                    handler.sendMessage(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pswd = passwd_editText.getText().toString();
//                us = user_editText.getText().toString();
//                if (pswd == null || us == null || lt_source == null) {
//                    Toast.makeText(getApplicationContext(), "请确认输入是否正确", Toast.LENGTH_SHORT).show();
//                } else {
//                    encodeNumber(pswd, us);
//
//                }
//            }
//        });
//
//    }
//
//    public void encodeNumber(String pswd, String us) {
//
//        webView.post(new Runnable() {
//            @Override
//            public void run() {
//                String method = "strEnc('" + us + pswd + lt_source + "','1','2','3')";
//                Log.e("执行方法", "run: " + method);
//                webView.evaluateJavascript(method, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String s) {
//                        Log.e("返回的数据", "onReceiveValue: " + s);
//                        rsa = s;
//                        Message message = new Message();
//                        message.what = 0x01;
//                        handler.sendMessage(message);
//                    }
//                });
//            }
//        });
//    }
//
//    public void getHtml() {
//        params.put("rsa", rsa);
//        params.put("ul", us.length() + "");
//        params.put("pl", pswd.length() + "");
//        params.put("lt", lt_source);
//        params.put("execution", execution_source);
//        params.put("_eventId", event_source);
//        Log.e("请求参数", "getHtml: " + params.toString());
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Connection firstLogin = Jsoup.connect(LOGIN_URL)
//                            .data(params)
//                            .method(Connection.Method.POST)
//                            .headers(requestHeaders)
//                            .timeout(30000);
//
//                    Connection.Response html = firstLogin.execute();
////                    if (html.statusCode() == 500) {
////                        Connection.Response execute = firstLogin.execute();
////                        Map<String, String> cookies = html.cookies();
////                        for (String s : cookies.keySet()
////                        ) {
////                            Log.e("key", ": " + s);
////                        }
////                        for (String s :
////                                cookies.values()) {
////                            Log.e("value", ": " + s);
////                        }
////                        Log.e("获取html", "getHtml: " + execute.body());
////                    }
//
//                    Log.e("获取html", "getHtml: " + html.body());
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//
//    }
//
//    private void initHeaders() {
//        requestHeaders.put("Connection", "keep-alive");
//        requestHeaders.put("Content-Length", "368");
//        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
//        requestHeaders.put("Host", "pass.sdu.edu.cn");
//        requestHeaders.put("Origin", "https://pass.sdu.edu.cn");
//        requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");
//        requestHeaders.put("Cookies", "JSESSIONID=" + cookies.get("JESSIONID") + "; Language=zh_CN; cookie-adx=" + cookies.get("cookie-adx"));
//    }
}