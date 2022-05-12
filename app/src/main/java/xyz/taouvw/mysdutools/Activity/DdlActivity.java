package xyz.taouvw.mysdutools.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

import xyz.taouvw.mysdutools.Adapter.DDlAdapter;
import xyz.taouvw.mysdutools.Bean.DdlBean;
import xyz.taouvw.mysdutools.Callback.SimpleItemTouchHelperCallback;
import xyz.taouvw.mysdutools.R;
import xyz.taouvw.mysdutools.utils.DdlListUtils;

public class DdlActivity extends AppCompatActivity {

    LinearLayout activity_ddl_layout;
    FrameLayout ddl_list_layout;
    ConstraintLayout empty_ddl;
    LinearLayout ddl_recycle_list;
    LinearLayout ddl_add_dialog;

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    TextInputLayout ddl_new_name;
    TextInputLayout ddl_new_desc;
    Button add_dialog_cancel;
    Button add_dialog_confirm;
    AppCompatButton chose_date;
    AppCompatButton chose_time;

    int day_of_ddl = 1;
    int month_of_ddl = 1;
    int year_of_ddl = 2022;
    int hour_of_ddl = 12;
    int minute_of_ddl = 0;
    String name_of_ddl = "";
    String desc_of_ddl = "";

    DDlAdapter dDlAdapter;
    ArrayList<DdlBean> ddlBeans = new ArrayList<>();
    RecyclerView ddl_list;
    ItemTouchHelper.Callback callback;
    ItemTouchHelper touchHelper;


    Toolbar tb;

    DdlListUtils ddlListUtils;
    FloatingActionButton add_btn;
    AlertDialog.Builder AddDialog_builder;
    AlertDialog Add_dialog;
    Boolean isEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载需要的布局
        activity_ddl_layout = (LinearLayout) LayoutInflater.from(DdlActivity.this).inflate(R.layout.activity_ddl, null);
        ddl_list_layout = activity_ddl_layout.findViewById(R.id.ddl_frame);
        empty_ddl = (ConstraintLayout) LayoutInflater.from(DdlActivity.this).inflate(R.layout.empty_ddl, null);
        ddl_recycle_list = (LinearLayout) LayoutInflater.from(DdlActivity.this).inflate(R.layout.not_empty_ddl, null);
        ddl_list = ddl_recycle_list.findViewById(R.id.ddl_recycleView);
        setContentView(activity_ddl_layout);

        // 初始化
        init();
    }

    void init() {

        add_dialog_init();

        ddlListUtils = new DdlListUtils(DdlActivity.this, "ddl.db", null, 1);

        // 添加按钮初始化
        add_btn = this.findViewById(R.id.add_ddl);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_dialog.show();
            }
        });
        // 工具栏
        tb = this.findViewById(R.id.tbOfDdl);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 假数据初始化


        // ddl列表初始化
        ddlBeans = ddlListUtils.getAllDdl();
        dDlAdapter = new DDlAdapter(ddlBeans, ddlListUtils);
        ddl_list.setAdapter(dDlAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DdlActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ddl_list.setLayoutManager(linearLayoutManager);
        dDlAdapter.setItemClickListener(new DDlAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onCheckIfIsEmpty() {
                //这里直接查没有问题，因为查之前对应数据就已经从数据库里删除了，相当于同步
                ddlBeans = ddlListUtils.getAllDdl();
                if (ddlBeans.size() == 0) {
                    if (!isEmpty) {
                        isEmpty = true;
                        ddl_list_layout.removeView(ddl_recycle_list);
                        ddl_list_layout.addView(empty_ddl);
                    }
                }
            }
        });

        callback = new SimpleItemTouchHelperCallback(dDlAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(ddl_list);


        if (ddlBeans.size() == 0) {
            isEmpty = true;
            ddl_list_layout.addView(empty_ddl);
        } else {
            isEmpty = false;
            ddl_list_layout.addView(ddl_recycle_list);
        }
    }

    void add_dialog_init() {
        // 添加对话框布局加载
        ddl_add_dialog = (LinearLayout) LayoutInflater.from(DdlActivity.this).inflate(R.layout.add_new_ddl_layout, null);
        ddl_new_name = ddl_add_dialog.findViewById(R.id.new_ddl_name);
        ddl_new_desc = ddl_add_dialog.findViewById(R.id.new_ddl_desc);
        add_dialog_cancel = ddl_add_dialog.findViewById(R.id.add_ddl_dialog_cancel);
        add_dialog_confirm = ddl_add_dialog.findViewById(R.id.add_ddl_dialog_confirm);
        chose_date = ddl_add_dialog.findViewById(R.id.ddl_date_btn);
        chose_time = ddl_add_dialog.findViewById(R.id.ddl_time_btn);

        AddDialog_builder = new AlertDialog.Builder(DdlActivity.this);
        AddDialog_builder.setView(ddl_add_dialog);
        Add_dialog = AddDialog_builder.create();
        Calendar calendar = Calendar.getInstance();
        // 时间选择对话框
        timePickerDialog = new TimePickerDialog(DdlActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                StringBuilder stringBuilder = new StringBuilder();
                hour_of_ddl = i;
                minute_of_ddl = i1;
                if (hour_of_ddl <= 9) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hour_of_ddl);
                stringBuilder.append(":");
                if (minute_of_ddl <= 9) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(minute_of_ddl);
                chose_time.setText(stringBuilder.toString());

                Log.e("选择时间", "onTimeSet: " + hour_of_ddl + ":" + minute_of_ddl);
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        // 日期选择对话框
        datePickerDialog = new DatePickerDialog(DdlActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                year_of_ddl = year;
                month_of_ddl = monthOfYear + 1;
                day_of_ddl = dayOfMonth;
                chose_date.setText(year_of_ddl + "年" + month_of_ddl + "月" + day_of_ddl + "日");
                Log.e("选择日期", "onDateSet: " + year_of_ddl + ":" + month_of_ddl + ":" + day_of_ddl);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        add_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ddl_new_desc.getEditText().setText("");
                ddl_new_name.getEditText().setText("");
                Add_dialog.dismiss();
            }
        });
        add_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ddl_new_name.getEditText().getText().toString();
                if (name.equals("")) {
                    ddl_new_name.setErrorEnabled(true);
                    ddl_new_name.setError("ddl内容不能为空");
                    ddl_new_name.setErrorIconDrawable(null);
                    return;
                }
                name_of_ddl = ddl_new_name.getEditText().getText().toString();
                desc_of_ddl = ddl_new_desc.getEditText().getText().toString();
                if (isEmpty) {
                    ddl_list_layout.removeView(empty_ddl);
                    isEmpty = false;
                    ddl_list_layout.addView(ddl_recycle_list);
                }
                DdlBean ddlBean = new DdlBean(name_of_ddl, desc_of_ddl, day_of_ddl, month_of_ddl, year_of_ddl, hour_of_ddl, minute_of_ddl);
                // 添加
                ddlBeans.add(ddlBean);
                dDlAdapter.addData(ddlBean);
                // 数据库添加操作
                ddlListUtils.addDdlInfo(ddlBean);
                ddl_new_desc.getEditText().setText("");
                ddl_new_name.getEditText().setText("");
                Log.e("TAG", "onClick:添加ddl 确定" + name_of_ddl + " " + desc_of_ddl);
                Add_dialog.dismiss();
            }
        });

        chose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        chose_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        ddl_new_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!ddl_new_name.getEditText().getText().equals("")) {
                    ddl_new_name.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}