package xyz.taouvw.mysdutools.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import xyz.taouvw.mysdutools.R;

public class AddClassActivity extends AppCompatActivity {
    EditText classname;
    EditText classLocation;
    EditText classTeacher;
    EditText classWeek;
    EditText classJie;
    String weekRange;
    ImageButton commitButton;
    Button[] buttons = new Button[4];
    TableRow[] tableRows = new TableRow[4];
    ImageButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        init();
    }

    private void init() {
        classname = this.findViewById(R.id.newClass);
        classLocation = this.findViewById(R.id.Classroom);
        classTeacher = this.findViewById(R.id.newTeacherName);
        classWeek = this.findViewById(R.id.ClassWeek);
        classJie = this.findViewById(R.id.classJie);
        commitButton = this.findViewById(R.id.confirmAdd);
        resetButton = this.findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetWeek();
            }
        });
        tableRows[0] = this.findViewById(R.id.NewClassWeekList_1);
        tableRows[1] = this.findViewById(R.id.NewClassWeekList_2);
        tableRows[2] = this.findViewById(R.id.NewClassWeekList_3);
        tableRows[3] = this.findViewById(R.id.NewClassWeekList_4);
        AppCompatCheckBox virtualChildAt;
        int count = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                count++;
                virtualChildAt.setText(count + "");
            }
        }

        buttons[0] = this.findViewById(R.id.ChoseSingleWeek);
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatCheckBox virtualChildAt;
                int m = 0;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                        m = i * 5 + j + 1;
                        if (m % 2 == 1) {
                            virtualChildAt.setChecked(true);
                        }
                    }
                }
            }
        });
        buttons[1] = this.findViewById(R.id.ChoseDoubleWeek);
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatCheckBox virtualChildAt;
                int m = 0;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                        m = i * 5 + j + 1;
                        if (m % 2 == 0) {
                            virtualChildAt.setChecked(true);
                        }
                    }
                }
            }
        });
        buttons[2] = this.findViewById(R.id.Chose_1_8_Week);
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatCheckBox virtualChildAt;
                int m = 0;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 6; j++) {
                        virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                        m = i * 5 + j + 1;
                        if (m <= 8) {
                            virtualChildAt.setChecked(true);
                        }
                    }
                }
            }
        });
        buttons[3] = this.findViewById(R.id.Chose_9_16_Week);
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatCheckBox virtualChildAt;
                int m = 0;
                for (int i = 1; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                        m = i * 5 + j + 1;
                        if (m >= 9 && m <= 16) {
                            virtualChildAt.setChecked(true);
                        }
                    }
                }
            }
        });


    }

    private void resetWeek() {
        AppCompatCheckBox virtualChildAt;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                virtualChildAt = (AppCompatCheckBox) tableRows[i].getVirtualChildAt(j);
                virtualChildAt.setChecked(false);
            }
        }
    }
}