package xyz.taouvw.mysdutools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import xyz.taouvw.mysdutools.Bean.DdlBean;

public class DdlListUtils extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public DdlListUtils(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.db = this.getWritableDatabase();
    }

    public DdlListUtils(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE DDLINFO(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100)," +
                "ddldesc VARCHAR(100)," +
                "year VARCHAR(20)," +
                "month VARCHAR(20)," +
                "day VARCHAR(20)," +
                "hour VARCHAR(20)," +
                "minute VARCHAR(20)" +
                "); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addDdlInfo(DdlBean ddlBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO DDLINFO VALUES(null,'");
        stringBuilder.append(ddlBean.getDDl_name());
        stringBuilder.append("','");
        stringBuilder.append(ddlBean.getDDl_detail());
        stringBuilder.append("','");
        stringBuilder.append(ddlBean.getYear_of_ddl());
        stringBuilder.append("','");
        stringBuilder.append(ddlBean.getMonth_of_ddl());
        stringBuilder.append("','");
        stringBuilder.append(ddlBean.getDay_of_ddl());
        stringBuilder.append("','");
        stringBuilder.append(ddlBean.getHour_of_ddl());
        stringBuilder.append("','");
        stringBuilder.append(ddlBean.getMinute_of_ddl());
        stringBuilder.append("');");
        db.execSQL(stringBuilder.toString());
    }

    @SuppressLint("Range")
    public ArrayList<DdlBean> getAllDdl() {
        ArrayList<DdlBean> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM DDLINFO", null);
        int count = cursor.getCount();
        if (count == 0) {
            return arrayList;
        } else {
            for (int i = 0; i < count; i++) {
                if (cursor.moveToPosition(i)) {
                    DdlBean ddlBean = new DdlBean();
                    ddlBean.setDDl_name(cursor.getString(cursor.getColumnIndex("name")));
                    ddlBean.setDDl_detail(cursor.getString(cursor.getColumnIndex("ddldesc")));
                    ddlBean.setYear_of_ddl(Integer.parseInt(cursor.getString(cursor.getColumnIndex("year"))));
                    ddlBean.setMonth_of_ddl(Integer.parseInt(cursor.getString(cursor.getColumnIndex("month"))));
                    ddlBean.setDay_of_ddl(Integer.parseInt(cursor.getString(cursor.getColumnIndex("day"))));
                    ddlBean.setHour_of_ddl(Integer.parseInt(cursor.getString(cursor.getColumnIndex("hour"))));
                    ddlBean.setMinute_of_ddl(Integer.parseInt(cursor.getString(cursor.getColumnIndex("minute"))));
                    ddlBean.generateDate();
                    arrayList.add(ddlBean);
                }
            }
        }
        return arrayList;
    }

    public void removeDdl(String name) {
        db.execSQL("DELETE FROM DDLINFO WHERE name='" + name + "';");
    }
}
