package xyz.taouvw.mysdutools.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.taouvw.mysdutools.Adapter.ClassRoomAdapter;
import xyz.taouvw.mysdutools.Bean.ClassRoomDetail;
import xyz.taouvw.mysdutools.R;

public class SearchFreeRoomActivity extends AppCompatActivity {
    private static final String[] TermList = new String[]{"2021-2022-2"};
    private static final String[] xqList = new String[]{"青岛校区"};
    private static final String[] xqListCode = new String[]{"07"};
    private static final String[] jxlList = new String[]{"振声苑", "会文南楼", "会文北楼"};
    private static final String[] dayList = new String[]{"一", "二", "三", "四", "五", "六", "日"};
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    LinearLayout searchDialog;
    NumberPicker choseTerm;
    NumberPicker choseXq;
    NumberPicker choseJxl;
    NumberPicker choseWeek;
    NumberPicker choseWhichDay;

    List<ClassRoomDetail> classRoomDetailList = new ArrayList<>();

    RecyclerView roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_free_room);
        searchDialog = (LinearLayout) LayoutInflater.from(SearchFreeRoomActivity.this).inflate(R.layout.search_room_dialog, null);
        initSearchDialog();
        init();
    }

    void init() {
        builder=new AlertDialog.Builder(SearchFreeRoomActivity.this);
        builder.setTitle("查询");
        builder.setView(searchDialog);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog = builder.create();
        alertDialog.show();

        for (int i = 0; i < 6; i++) {
            ClassRoomDetail classRoomDetail = new ClassRoomDetail();
            classRoomDetail.setName("青岛校区振声苑E111");
            classRoomDetail.setFreeTime(new int[]{1, 1, 0, 0, 1});
            classRoomDetail.setOccupiedTime(new int[]{1, 1, 0, 0, 1});
            classRoomDetailList.add(classRoomDetail);
        }

        roomList = this.findViewById(R.id.roomList);
        ClassRoomAdapter classRoomAdapter = new ClassRoomAdapter(classRoomDetailList);
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
    }
}