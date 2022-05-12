package xyz.taouvw.mysdutools.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import xyz.taouvw.mysdutools.Adapter.OptionsAdapter;
import xyz.taouvw.mysdutools.Adapter.WeekAdapter;
import xyz.taouvw.mysdutools.Bean.ClassDetail;
import xyz.taouvw.mysdutools.MyView.ShapeTextView;
import xyz.taouvw.mysdutools.R;
import xyz.taouvw.mysdutools.utils.DateUtils;
import xyz.taouvw.mysdutools.utils.PropertiesUtils;
import xyz.taouvw.mysdutools.utils.SQLUtils;
import xyz.taouvw.mysdutools.utils.SharedPreferenceUtils;

public class MainActivity extends AppCompatActivity {
    Toolbar tb;
    DrawerLayout mdrawlayout;
    private static final int[] color = new int[]{R.color.choseweekbackgroundcolor,
            R.color.mediumturquoise,
            R.color.khaki,
            R.color.paleturquoise,
            R.color.darkseagreen,
            R.color.lightskyblue,
            R.color.lightseagreen,
            R.color.deepskyblue,
            R.color.rosybrown,
            R.color.tan
    };
    TableLayout kb_layout;
    RecyclerView weekRecycle;
    RecyclerView OptionsRecycle;
    ImageView week_arrow;
    Boolean weekSelectorIsShow = true;


    ShapeTextView LastshapeTextView;
    ShapeTextView nowShapeTextView;
    int LastChosenPosition;
    int NowChosenPosition;

    List<ClassDetail> classDetailList = new ArrayList<>();
    TableRow[] tableRows = new TableRow[6];

    Resources resources;
    int screenHeight;
    int screenWidth;
    DisplayMetrics dm;

    int[] dateInfo = new int[2];
    String startOfStudy = "2022年02月20日";
    SharedPreferenceUtils preferenceUtils;

    // 课程详情对话框布局
    LinearLayout classinfoDetailDialog;
    TextView classinfoDetail_name;
    TextView classinfoDetail_teacher;
    TextView classinfoDetail_room;
    TextView classinfoDetail_week;
    // 课程详情对话框
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    SQLUtils sqlUtils;
    PropertiesUtils propertiesUtils = PropertiesUtils.getInstance(MainActivity.this, "values.properties");
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();
            switch (resultCode) {
                case 0: {
                    Toast.makeText(MainActivity.this, "取消添加", Toast.LENGTH_SHORT).show();
                }
                break;
                case 1: {
                    ClassDetail classDetail = new ClassDetail();
                    classDetail.setClassCode("self");
                    classDetail.setName(data.getStringExtra("classname"));
                    classDetail.setRoom(data.getStringExtra("classroom"));
                    classDetail.setTeacher(data.getStringExtra("classteacher"));
                    String weekrange = data.getStringExtra("weekrange");
                    classDetail.setWeekRan(weekrange.substring(0, weekrange.length() - 1).split(","));
                    classDetail.setWhichDay(Integer.parseInt(data.getStringExtra("whichday")));
                    classDetail.setWhichjie(Integer.parseInt(data.getStringExtra("whichjie")));
                    Log.e("TAG", "onActivityResult: " + classDetail.toString());

                    SQLUtils sqlUtils = new SQLUtils(MainActivity.this, "class.db", null, propertiesUtils.readInt("DATABASE_VERSION", 1));
                    sqlUtils.addMyOwnClass(classDetail);
                    reShowKb();
                }
                break;
                case 2: {
                    reShowKb();
                }
                break;
            }
        }
    });

    private static final String[] weekDays = {"周一\n", "周二\n", "周三\n", "周四\n", "周五\n", "周六\n", "周日\n"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        init();
    }

    void init() {
        //获取屏幕像素信息
        resources = this.getResources();
        dm = resources.getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;

        // toolbar
        tb = this.findViewById(R.id.tb);
        mdrawlayout = this.findViewById(R.id.LeftDrag);
        // 周选择器
        weekRecycle = this.findViewById(R.id.week_selector);
        // 选项选择器
        OptionsRecycle = this.findViewById(R.id.OptionsList);
        week_arrow = this.findViewById(R.id.arrow_week);
        kb_layout = this.findViewById(R.id.kb_layout);


        // 课程详情对话框初始化
        classinfoDetailDialog = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.classinfo_detail, null);
        classinfoDetail_name = classinfoDetailDialog.findViewById(R.id.classinfo_detail_name);
        classinfoDetail_teacher = classinfoDetailDialog.findViewById(R.id.classinfo_detail_teacher);
        classinfoDetail_room = classinfoDetailDialog.findViewById(R.id.classinfo_detail_room);
        classinfoDetail_week = classinfoDetailDialog.findViewById(R.id.classinfo_detail_week);

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(classinfoDetailDialog);

        alertDialog = builder.create();
        classinfoDetailDialog.findViewById(R.id.class_detail_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        classinfoDetailDialog.findViewById(R.id.class_detail_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        //读取开学日期信息
        preferenceUtils = new SharedPreferenceUtils(MainActivity.this, "values");
        //读取学期开始
        propertiesUtils.init();
        // 初始化数据库操作
        sqlUtils = new SQLUtils(this, "class.db", null, propertiesUtils.readInt("DATABASE_VERSION", 1));

        //获取当前日期
        String[] nowDate = this.getNowDate();
        //如果没有设置学期开始日期那么自动设置当前日期为学期开始日期
        startOfStudy = preferenceUtils.read("startOfStudyYear", nowDate[0]) + "年" + preferenceUtils.read("startOfStudyMonth", nowDate[1]) + "月" + preferenceUtils.read("startOfStudyDay", nowDate[2]) + "日";
        preferenceUtils.commit();
        //获取学期信息
        dateInfo = DateUtils.getDayAndWeek(startOfStudy);
        // 给toolbar添加menu以及监听
        tb.inflateMenu(R.menu.toolbarmenu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.refresh_kb: {
                        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                        launcher.launch(intent);
                    }
                    break;
                    case R.id.StartDay: {
                        DatePickerDialog datePicker = null;
                        Calendar calendar = Calendar.getInstance();
                        StringBuilder stringBuilder = new StringBuilder();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    monthOfYear += 1;
                                    stringBuilder.append(year);
                                    stringBuilder.append("年");
                                    if (monthOfYear <= 9) {
                                        stringBuilder.append("0");
                                    }
                                    stringBuilder.append(monthOfYear);
                                    stringBuilder.append("月");
                                    if (dayOfMonth <= 9) {
                                        stringBuilder.append("0");
                                    }
                                    stringBuilder.append(dayOfMonth);
                                    stringBuilder.append("日");
                                    startOfStudy = stringBuilder.toString();
                                    //保存开学日期信息
                                    preferenceUtils.save("startOfStudyYear", year + "");
                                    preferenceUtils.save("startOfStudyMonth", monthOfYear + "");
                                    preferenceUtils.save("startOfStudyDay", dayOfMonth + "");
                                    preferenceUtils.commit();
                                    dateInfo = DateUtils.getDayAndWeek(startOfStudy);
                                    //重新渲染课表
                                    InitKbList(dateInfo[0]);
                                    putClassesIn(classDetailList, dateInfo[0]);
                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        }
                        if (datePicker == null) {
                            Toast.makeText(MainActivity.this, "不兼容使用，请升级后重试", Toast.LENGTH_SHORT).show();
                        }
                        datePicker.show();
                    }
                    break;
                    case R.id.remove_weekend: {
                        kb_layout.setColumnCollapsed(6, true);
                        kb_layout.setColumnCollapsed(7, true);
                    }
                    break;
                    case R.id.show_weekend: {
                        kb_layout.setColumnCollapsed(6, false);
                        kb_layout.setColumnCollapsed(7, false);
                    }
                    break;
                    default:
                        break;
                }
                return true;
            }
        });
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdrawlayout.openDrawer(Gravity.LEFT);
            }
        });
        // toolbar图标状态
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weekSelectorIsShow) {
                    weekRecycle.setVisibility(View.GONE);
                    weekSelectorIsShow = !weekSelectorIsShow;
                    week_arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                } else {
                    weekRecycle.setVisibility(View.VISIBLE);
                    weekSelectorIsShow = !weekSelectorIsShow;
                    week_arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }

            }
        });


        //选项选择器渲染
        OptionsAdapter optionsAdapter = new OptionsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        OptionsRecycle.setLayoutManager(linearLayoutManager);
        OptionsRecycle.setAdapter(optionsAdapter);
        OptionsRecycle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 课表格式
        tableRows[0] = this.findViewById(R.id.Class);
        tableRows[1] = this.findViewById(R.id.FirstClass);
        tableRows[2] = this.findViewById(R.id.SecondClass);
        tableRows[3] = this.findViewById(R.id.ThirdClass);
        tableRows[4] = this.findViewById(R.id.FourthClass);
        tableRows[5] = this.findViewById(R.id.FifthClass);
        initKb();
        InitKbList(dateInfo[0]);
        getDayOfWeek(dateInfo[0]);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //如果已经存在数据了就不必再重新渲染课表了
        if (classDetailList.size() != 0) {
            return;
        }
        reShowKb();
    }

    @SuppressLint("Range")
    private void reShowKb() {
        classDetailList.clear();
//        SQLiteDatabase db = sqlUtils.getReadableDatabase();
        if (sqlUtils.db == null) {
            Toast.makeText(this, "当前暂无课程表，请点击右上角刷新后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor classinfo = sqlUtils.queryInfo("SELECT * FROM CLASSINFO", null);
        int count = classinfo.getCount();
        if (count == 0) {
            Toast.makeText(this, "当前暂无课程表，请点击右上角刷新后重试", Toast.LENGTH_SHORT).show();
        }
        Log.e("count", "onResume: " + count);
        for (int i = 0; i < count; i++) {
            if (classinfo.moveToPosition(i)) {
                ClassDetail classDetail = new ClassDetail();
                classDetail.setName(classinfo.getString(classinfo.getColumnIndex("classname")));
                classDetail.setRoom(classinfo.getString(classinfo.getColumnIndex("classroom")));
                classDetail.setTeacher(classinfo.getString(classinfo.getColumnIndex("teacher")));
                classDetail.setWeek(classinfo.getString(classinfo.getColumnIndex("weekR")));
                classDetail.setWhichDay(Integer.parseInt(classinfo.getString(classinfo.getColumnIndex("whichday"))));
                classDetail.setWhichjie(Integer.parseInt(classinfo.getString(classinfo.getColumnIndex("whichjie"))));
                //保存到数据库里的是字符串，需要做分割
                classDetail.setWeekRan(classinfo.getString(classinfo.getColumnIndex("weekrange")).split(","));
                classDetail.setRange(classinfo.getString(classinfo.getColumnIndex("classTime")).split(","));
                classDetailList.add(classDetail);
            }
        }
        classinfo.close();
        //渲染信息到课表页面
        putClassesIn(classDetailList, dateInfo[0]);
    }

    /**
     * 渲染课表
     * 思路：
     * 先查询数据库获得所有课表信息，然后再获取上课时间，根据上课时间获取到对应的课表格子之后，如果是连续上课，weekRan的长度小于等于2，再判断当前周是否在上课范围内
     * 如果不在，不进行渲染，如果在，则添加信息，如果是间隔上课，
     *
     * @param list
     * @param mNowWeek
     */
    private void putClassesIn(List<ClassDetail> list, int mNowWeek) {
        initKb();
        clearKb();
        Random random = new Random();
        // 获取哪天上课和第几节
        int whichDay;
        int whichjie;
        ShapeTextView virtualChildAt;
        StringBuilder sb = new StringBuilder();
        ClassDetail classDetail;
        /**
         * 进入循环，开始遍历所有课程
         */
        for (int i = 0; i < list.size(); i++) {
            classDetail = list.get(i);
//            Log.e("TAG", "putClassesIn: " + classDetail.toString());
            // 通过Day和jie这两个变量来选择需要渲染的格子

            whichDay = classDetail.getWhichDay();
            whichjie = classDetail.getWhichjie();
            virtualChildAt = (ShapeTextView) tableRows[whichjie].getVirtualChildAt(whichDay);
            virtualChildAt.setMaxHeight(screenHeight / 5);
            virtualChildAt.setMaxWidth(screenWidth / 8);
            String[] weekRan = classDetail.getWeekRan();
            // 连续上课情况
//            Log.e("", "putClassesIn: " + weekRan.length);
            // 替换掉所有的中括号以及空格，防止解析错误
            int n = Integer.parseInt(weekRan[0].replaceAll("(\\[|\\]|\\s*)", ""));
            int b = Integer.parseInt(weekRan[weekRan.length - 1].replaceAll("(\\[|\\]|\\s*)", ""));
//            Log.e("TAG", "putClassesIn: " + n + "    " + b + "  now" + mNowWeek);
            if (n <= mNowWeek && b >= mNowWeek) {
                if (weekRan.length <= 2) {
                    virtualChildAt.setBackgroundResource(color[random.nextInt(9)]);
                    sb.append(classDetail.getName());
                    sb.append("\n");
                    sb.append("@");
                    sb.append(classDetail.getRoom());
                    virtualChildAt.setText(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    // 隔周上课的情况
                    for (int j = 0; j < weekRan.length; j++) {
                        int m = Integer.parseInt(weekRan[j].replaceAll("(\\[|\\]|\\s*)", ""));
                        if (mNowWeek == m) {
                            virtualChildAt.setBackgroundResource(color[random.nextInt(9)]);
                            sb.append(classDetail.getName());
                            sb.append("\n");
                            sb.append("@");
                            sb.append(classDetail.getRoom());
                            virtualChildAt.setText(sb.toString());
                            sb.delete(0, sb.length());
                            break;
                        }
                    }
                }
//                ShapeTextView finalVirtualChildAt = virtualChildAt;
//                virtualChildAt.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("Range")
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle("详情");
//                        CharSequence text = finalVirtualChildAt.getText();
//                        String[] split = text.toString().split("@");
//                        Log.e("课程详情", "onClick: " + split[0]);
//                        Cursor cursor = sqlUtils.queryInfo("SELECT * FROM CLASSINFO WHERE classname='" + split[0] + "';", null);
//                        int count = cursor.getCount();
//                        if (count == 0) {
//                            return;
//                        } else {
//                            for (int j = 0; j < count; j++) {
//                                if (cursor.moveToPosition(j)) {
//                                    Log.e("TAG", "onClick: " + cursor.getString(cursor.getColumnIndex("classname")));
//                                    Log.e("TAG", "onClick: " + cursor.getString(cursor.getColumnIndex("classname")));
//
//                                }
//                            }
//
//                        }
//
//                        builder.setMessage(text);
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    }
//                });
            }
        }


    }


    //设置课表格式,并添加侦听事件
    private void initKb() {
        for (int i = 0; i < 8; i++) {
            ShapeTextView weekend = (ShapeTextView) tableRows[0].getVirtualChildAt(i);
            weekend.setMaxWidth(screenWidth / 8);
            weekend.setMinWidth(screenWidth / 8);
        }
        for (int i = 1; i <= 5; i++) {
            ShapeTextView weekend = (ShapeTextView) tableRows[i].getVirtualChildAt(0);
            weekend.setHeight(screenHeight / 8);
        }


        // 添加长按触发事件
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < 7; j++) {
                ShapeTextView grid = (ShapeTextView) tableRows[i].getVirtualChildAt(j);
                int finalJ = j;
                int finalI = i;
                // 添加点击事件

                grid.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("Range")
                    @Override
                    public void onClick(View view) {

                        CharSequence text = grid.getText();
                        if (text.length() == 0) {
                            Toast.makeText(MainActivity.this, "当前节没有课，长按添加", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] split = text.toString().split("\n@");
                        Log.e("课程详情", "onClick: " + split[0]);
                        Cursor cursor = sqlUtils.queryInfo("SELECT * FROM CLASSINFO WHERE classname='" + split[0] + "';", null);
                        int count = cursor.getCount();
                        if (count == 0) {
                            return;
                        } else {
                            for (int j = 0; j < count; j++) {
                                if (cursor.moveToPosition(j)) {
                                    classinfoDetail_name.setText(cursor.getString(cursor.getColumnIndex("classname")));
                                    classinfoDetail_room.setText(cursor.getString(cursor.getColumnIndex("classroom")));
                                    classinfoDetail_teacher.setText(cursor.getString(cursor.getColumnIndex("teacher")));
                                    classinfoDetail_week.setText(cursor.getString(cursor.getColumnIndex("weekR")));
                                    Log.e("TAG", "onClick: " + cursor.getString(cursor.getColumnIndex("classname")));
                                    Log.e("TAG", "onClick: " + cursor.getString(cursor.getColumnIndex("classroom")));
                                }
                            }
                        }


                        alertDialog.show();
                    }
                });


                // 添加长按触发事件
                grid.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddClassActivity.class);
                        intent.putExtra("whichday", finalJ);
                        intent.putExtra("whichjie", finalI);
                        if (grid.getText() != "") {
                            Toast.makeText(MainActivity.this, "当前选中的已经有课了，换一个吧", Toast.LENGTH_SHORT).show();
                        } else {
                            launcher.launch(intent);
                        }
                        return true;
                    }
                });
            }
        }
    }

    private void clearKb() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j < 8; j++) {
                ShapeTextView grid = (ShapeTextView) tableRows[i].getVirtualChildAt(j);
                grid.setText("");
                grid.setBackgroundResource(R.drawable.textviewborder);
            }
        }
    }

    // 初始化周选择器，选择开学日期后将重新调用
    private void InitKbList(int nowWeek) {
        WeekAdapter weekAdapter = new WeekAdapter(nowWeek);
        weekAdapter.setItemClickListener(new WeekAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NowChosenPosition = position;
                tb.setSubtitle("第" + position + "周");
                putClassesIn(classDetailList, position);
                getDayOfWeek(position);
                if (LastshapeTextView != null) {
                    if (LastChosenPosition == nowWeek) {
                        LastshapeTextView.setBackgroundResource(R.color.lightgray);
                    } else {
                        LastshapeTextView.setBackgroundResource(R.color.kbColor);
                    }
                }
                nowShapeTextView = (ShapeTextView) view;

                if (NowChosenPosition != nowWeek) {
                    nowShapeTextView.setBackgroundResource(R.color.paleturquoise);
                }
                LastshapeTextView = nowShapeTextView;
                LastChosenPosition = NowChosenPosition;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weekRecycle.setLayoutManager(layoutManager);
        weekRecycle.setAdapter(weekAdapter);
    }

    /**
     * 获取每周对应的日期并渲染
     *
     * @param nowWeek
     */
    private void getDayOfWeek(int nowWeek) {
        int[] dayOfSpecialWeek = DateUtils.getDayOfSpecialWeek(startOfStudy, nowWeek);
        for (int i = 6; i >= 0; i--) {
            ShapeTextView weekend = (ShapeTextView) tableRows[0].getVirtualChildAt(7 - i);
            weekend.setText(weekDays[6 - i] + dayOfSpecialWeek[i] + "日");
        }
        ShapeTextView weekend = (ShapeTextView) tableRows[0].getVirtualChildAt(0);
        weekend.setText(dayOfSpecialWeek[7] + "月");
    }


    /**
     * 获取当前时间，String[0],[1],[2]依次为年月日
     *
     * @return
     */
    private String[] getNowDate() {
        String[] dates = new String[3];
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        dates[0] = String.valueOf(instance.get(Calendar.YEAR));
        int month = instance.get(Calendar.MONTH + 1);
        if (month < 10) {
            dates[1] = "0" + month;
        } else {
            dates[1] = String.valueOf(month);
        }
        int day = instance.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            dates[2] = "0" + day;
        } else {
            dates[2] = String.valueOf(day);
        }
        return dates;
    }
}