package xyz.taouvw.mysdutools.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableRow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import xyz.taouvw.mysdutools.R;

public class AddClassActivity extends AppCompatActivity {

    private static final String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    TextInputLayout classname;
    EditText classLocation;
    EditText classTeacher;
    AppCompatButton classWeek;
    AppCompatButton classJie;

    AppCompatButton commitButton;
    Button[] buttons = new Button[4];
    TableRow[] tableRows = new TableRow[4];
    ImageButton resetButton;
    Toolbar toolbar;
    NumberPicker numberPicker1;
    NumberPicker numberPicker3;
    LinearLayout pickWeek;
    LinearLayout pickTime;
    String weekRange = "";
    String className = "";
    String classroom = "";
    String classTea = "";
    String jie = "1";
    String zhou = "1";


    AlertDialog pickWeekDialog;
    AlertDialog pickTimeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        pickWeek = (LinearLayout) getLayoutInflater().inflate(R.layout.pickweek, null);
        pickTime = (LinearLayout) getLayoutInflater().inflate(R.layout.picktime, null);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void init() {
        classname = this.findViewById(R.id.newClass);
        classLocation = this.findViewById(R.id.newClassroom);
        classTeacher = this.findViewById(R.id.newTeacherName);
        classWeek = this.findViewById(R.id.choseweek);
        classJie = this.findViewById(R.id.chosetime);
        commitButton = this.findViewById(R.id.confirmAdd);
        initPickWeek(pickWeek);
        initPickTime(pickTime);
        //监听课程名输入格式
        classname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!classname.getEditText().getText().equals("")) {
                    classname.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //提交按钮
        commitButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddClassActivity.this, MainActivity.class);
            String newclassname = classname.getEditText().getText().toString();
            if (newclassname.equals("")) {
                classname.setErrorEnabled(true);
                classname.setError("课程名不能为空");
                classname.setErrorIconDrawable(null);
                return;
            }
//            Log.e("TAG", "init: " + newclassname);
//            Log.e("TAG", "init: " + classLocation.getText());
//            Log.e("TAG", "init: " + classTeacher.getText());
//            Log.e("TAG", "init: " + weekRange);
//            Log.e("TAG", "init: " + zhou);
//            Log.e("TAG", "init: " + jie);


            intent.putExtra("classname", newclassname);
            intent.putExtra("classroom", classLocation.getText().toString());
            intent.putExtra("classteacher", classTeacher.getText().toString());
            intent.putExtra("weekrange", weekRange);
            intent.putExtra("whichday", zhou);
            intent.putExtra("whichjie", jie);
            setResult(1, intent);
            finish();
        });
        toolbar = this.findViewById(R.id.tbOfAddClass);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(AddClassActivity.this, MainActivity.class);
            setResult(0, intent);
            finish();
        });

        AlertDialog.Builder pickWeekBuilder = new AlertDialog.Builder(AddClassActivity.this);
        pickWeekBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                int count = 0;
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 6; k++) {
                        count++;
                        AppCompatCheckBox virtualChildAt = (AppCompatCheckBox) tableRows[j].getVirtualChildAt(k);
                        if (virtualChildAt.isChecked()) {
                            stringBuilder.append(count);
                            stringBuilder.append(",");
                        }
                    }
                }
                weekRange = stringBuilder.toString();
                classWeek.setText(weekRange.substring(0, weekRange.length() - 1) + "周");
                Log.e("选择了", "onClick: " + weekRange);
            }
        });
        pickWeekBuilder.setNeutralButton("重置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetWeek();
                classWeek.setText("1-8周");
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 1; j < 9; j++) {
                    stringBuilder.append(j);
                    stringBuilder.append(",");
                }
                weekRange = stringBuilder.toString();
            }
        });
        pickWeekBuilder.setTitle("选择周数");
        pickWeekBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                weekRange = "";
            }
        });
        pickWeekBuilder.setView(pickWeek);
        pickWeekDialog = pickWeekBuilder.create();
        classWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickWeekDialog.show();
            }
        });


        AlertDialog.Builder pickTimeBuilder = new AlertDialog.Builder(AddClassActivity.this);
        pickTimeBuilder.setView(pickTime);
        pickTimeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                jie = numberPicker3.getValue() + "";
                zhou = numberPicker1.getValue() + "";
                Log.e("TAG", "onClick: " + zhou);
                Log.e("选择了", "onClick: " + weekDays[Integer.parseInt(zhou) - 1] + "第" + jie + "节");
                classJie.setText(weekDays[Integer.parseInt(zhou) - 1] + "  第" + jie + "节");
            }
        });
        pickTimeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                zhou = 1 + "";
                jie = 1 + "";
                classJie.setText(weekDays[Integer.parseInt(zhou) - 1] + "  第" + jie + "节");

            }
        });
        pickTimeDialog = pickTimeBuilder.create();
        classJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTimeDialog.show();
            }
        });
    }

    /**
     * 初始化周数与快捷选择按钮
     */
    private void initWeekChose(LinearLayout pickWeek) {

        tableRows[0] = pickWeek.findViewById(R.id.NewClassWeekList_1);
        tableRows[1] = pickWeek.findViewById(R.id.NewClassWeekList_2);
        tableRows[2] = pickWeek.findViewById(R.id.NewClassWeekList_3);
        tableRows[3] = pickWeek.findViewById(R.id.NewClassWeekList_4);
        AppCompatCheckBox virtualChildAt;
        int count = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                virtualChildAt.setText(count + "");
                count++;
            }
        }
        buttons[0] = pickWeek.findViewById(R.id.ChoseSingleWeek);
        buttons[0].setOnClickListener(view -> {
            AppCompatCheckBox virtualChildAt1;
            int m = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {
                    virtualChildAt1 = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                    m++;
                    if (m % 2 == 1) {
                        virtualChildAt1.setChecked(true);
                    }
                }
            }
        });
        buttons[1] = pickWeek.findViewById(R.id.ChoseDoubleWeek);
        buttons[1].setOnClickListener(view -> {
            AppCompatCheckBox virtualChildAt12;
            int m = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {
                    virtualChildAt12 = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                    m++;
                    if (m % 2 == 0) {
                        virtualChildAt12.setChecked(true);
                    }
                }
            }
        });
        buttons[2] = pickWeek.findViewById(R.id.Chose_1_8_Week);
        buttons[2].setOnClickListener(view -> {
            AppCompatCheckBox virtualChildAt13;
            int m = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    virtualChildAt13 = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                    m++;
                    if (m <= 8) {
                        virtualChildAt13.setChecked(true);
                    }
                }
            }
        });
        buttons[3] = pickWeek.findViewById(R.id.Chose_9_16_Week);
        buttons[3].setOnClickListener(view -> {
            AppCompatCheckBox virtualChildAt14;
            int m = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {
                    virtualChildAt14 = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                    m++;
                    if (m >= 9 && m <= 16) {
                        virtualChildAt14.setChecked(true);
                    }
                }
            }
        });
    }

    /**
     * 重置周数选择
     */
    private void resetWeek() {
        AppCompatCheckBox virtualChildAt;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                virtualChildAt.setChecked(false);
            }
        }
    }

    private void initPickWeek(LinearLayout pickWeek) {
        initWeekChose(pickWeek);
    }

    private void initPickTime(LinearLayout pickTime) {
        numberPicker1 = pickTime.findViewById(R.id.numberPicker1);
        numberPicker1.setDisplayedValues(weekDays);
        numberPicker1.setMinValue(1);
        numberPicker1.setMaxValue(7);
        numberPicker1.setValue(1);
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker3 = pickTime.findViewById(R.id.numberPicker3);
        numberPicker3.setMinValue(1);
        numberPicker3.setMaxValue(5);
        numberPicker3.setValue(1);
        numberPicker3.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }
}