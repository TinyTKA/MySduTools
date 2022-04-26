package xyz.taouvw.mysdutools.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.taouvw.mysdutools.Adapter.ClassRoomAdapter;
import xyz.taouvw.mysdutools.Bean.ClassRoomDetail;
import xyz.taouvw.mysdutools.R;
import xyz.taouvw.mysdutools.utils.ClassroomUtils;

public class SearchFreeRoomActivity extends AppCompatActivity {
    private static final String[] TermList = new String[]{"2021-2022-2"};
    private static final String[] xqList = new String[]{"青岛校区"};
    private static final String[] xqListCode = new String[]{"07"};
    private static final String[] jxlList = new String[]{"振声苑N","振声苑W","振声苑E", "会文南楼", "会文北楼"};
    private static final String[] dayList = new String[]{"一", "二", "三", "四", "五", "六", "日"};
    private static final String url = "https://bkzhjx.wh.sdu.edu.cn/jiaowu/kxjsgl/kxjsgl.do?method=queryKxxxByJs_sddxFP&typewhere=jwyx";

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    LinearLayout searchDialog;
    NumberPicker choseTerm;
    NumberPicker choseXq;
    NumberPicker choseJxl;
    NumberPicker choseWeek;
    NumberPicker choseWhichDay;

    AppCompatButton search_btn;

    Toolbar tb;
    List<ClassRoomDetail> classRoomDetailList = new ArrayList<>();
    //不同状态显示不同的布局
    LinearLayout haveClassroom;
    ConstraintLayout emptyClassroom;
    boolean isEmpty = true;

    LinearLayout mainView;

    ClassRoomAdapter classRoomAdapter;
    RecyclerView roomList;
    Document document;

    String day;
    String xq;
    String week;
    String term;
    String where;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = (LinearLayout) LayoutInflater.from(SearchFreeRoomActivity.this).inflate(R.layout.activity_search_free_room, null);

        //加载布局
        searchDialog = (LinearLayout) LayoutInflater.from(SearchFreeRoomActivity.this).inflate(R.layout.search_room_dialog, null);
        haveClassroom = (LinearLayout) LayoutInflater.from(SearchFreeRoomActivity.this).inflate(R.layout.haveclassroom, null);
        emptyClassroom = (ConstraintLayout) LayoutInflater.from(SearchFreeRoomActivity.this).inflate(R.layout.emptyclassroom, null);
        mainView.addView(emptyClassroom, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        setContentView(mainView);
        initSearchDialog();
        init();
        handler = new Handler(new Handler.Callback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean handleMessage(@NonNull Message message) {
                switch (message.what) {
                    case 0x11: {
                        Log.e("handle", "handleMessage: 收到信息");
                        classRoomDetailList = ClassroomUtils.parseHtmlForData(document, day, where);
                        Log.e("TAG", "handleMessage: " + classRoomDetailList.size());
                        classRoomAdapter.AddAllData(classRoomDetailList);
                    }
                    default:
                        break;
                }
                return true;
            }

        });
    }

    void init() {

        tb = this.findViewById(R.id.tbOfSearchRoom);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.wave_scale, R.anim.fade);
            }
        });
        search_btn = this.findViewById(R.id.research_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

//        for (int i = 0; i < 6; i++) {
//            ClassRoomDetail classRoomDetail = new ClassRoomDetail();
//            classRoomDetail.setName("青岛校区振声苑E111");
//            classRoomDetail.setFreeTime(new int[]{1, 1, 0, 0, 1});
//            classRoomDetail.setOccupiedTime(new int[]{1, 1, 0, 0, 1});
//            classRoomDetailList.add(classRoomDetail);
//        }

        roomList = haveClassroom.findViewById(R.id.roomList);
        classRoomAdapter = new ClassRoomAdapter(classRoomDetailList);
        roomList.setAdapter(classRoomAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchFreeRoomActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        roomList.setLayoutManager(layoutManager);
    }

    void initSearchDialog() {
        choseTerm = searchDialog.findViewById(R.id.choseTerm);
        choseTerm.setDisplayedValues(TermList);
        choseTerm.setValue(1);
        choseTerm.setMinValue(1);
        choseTerm.setMaxValue(1);
        choseTerm.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        choseXq = searchDialog.findViewById(R.id.choseXq);
        choseXq.setDisplayedValues(xqList);
        choseXq.setValue(1);

        choseXq.setMinValue(1);
        choseXq.setMaxValue(1);
        choseXq.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        choseJxl = searchDialog.findViewById(R.id.choseJxl);
        choseJxl.setDisplayedValues(jxlList);
        choseJxl.setValue(1);
        choseJxl.setMinValue(1);
        choseJxl.setMaxValue(jxlList.length);
        choseJxl.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        choseWeek = searchDialog.findViewById(R.id.choseWhichWeek);
        choseWeek.setValue(1);
        choseWeek.setMinValue(1);
        choseWeek.setMaxValue(22);
        choseWeek.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        choseWhichDay = searchDialog.findViewById(R.id.choseWhichDay);
        choseWhichDay.setDisplayedValues(dayList);
        choseWhichDay.setMinValue(1);
        choseWhichDay.setMaxValue(7);
        choseWhichDay.setValue(1);
        choseWhichDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        builder = new AlertDialog.Builder(SearchFreeRoomActivity.this);
        builder.setView(searchDialog);
        searchDialog.setBackgroundResource(R.drawable.search_dialog_background);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                classRoomDetailList.clear();
                classRoomAdapter.removeAll();
                term = TermList[choseTerm.getValue() - 1];
                xq = xqListCode[choseXq.getValue() - 1];
                int value = choseWeek.getValue();
                week = String.valueOf(value);
                where = jxlList[choseJxl.getValue() - 1];
                day = String.valueOf(choseWhichDay.getValue());
                tb.setSubtitle("第" + week + "周，周" + dayList[choseWhichDay.getValue() - 1]);
                Log.e("选择了", "onClick: " + term + "  " + xq + "  " + week + "   " + where + "    " + day);
                searchForData(term, where, week, day);
                if (isEmpty) {
                    mainView.removeView(emptyClassroom);
                    isEmpty = false;
                    mainView.addView(haveClassroom);
                }
                Toast.makeText(SearchFreeRoomActivity.this, "查询中，请稍后", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("重置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!isEmpty) {
                    isEmpty = true;
                    mainView.removeView(haveClassroom);
                    mainView.addView(emptyClassroom);
                }
                classRoomDetailList.clear();
                classRoomAdapter.removeAll();
            }
        });
        alertDialog = builder.create();
    }


    public void searchForData(String whichTerm, String where, String whichWeek, String whichDay) {
        Connection connect = Jsoup.connect(url);
        connect.data("gnq_mh", "",
                "jsmc_mh", "",
                "typewhere", "jwyx",
                "xqbh", "07",
                "xnxqh", whichTerm,
                "jxlbh", "",
                "zc", whichWeek,
                "zc2", whichWeek,
                "xq", whichDay,
                "xq2", whichDay);
        connect.method(Connection.Method.POST);
        connect.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36 Edg/100.0.1185.50");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    document = connect.timeout(5000).get();
                    Log.e("发送请求", "run: ");
                    Message message = new Message();
                    message.what = 0x11;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}