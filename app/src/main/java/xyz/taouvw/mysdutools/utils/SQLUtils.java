package xyz.taouvw.mysdutools.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

import xyz.taouvw.mysdutools.Bean.ClassDetail;

public class SQLUtils extends SQLiteOpenHelper {


    public SQLiteDatabase db;

    public SQLUtils(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.db = this.getWritableDatabase();
    }

    public Boolean storeClassInfo(List<ClassDetail> classDetails) {

        return true;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE CLASSINFO(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "classname VARCHAR(100)," +
                "classroom VARCHAR(100)," +
                "teacher VARCHAR(50)," +
                "weekR VARCHAR(50)," +
                "whichday VARCHAR(30)," +
                "whichjie VARCHAR(30)," +
                "weekrange VARCHAR(30)," +
                "classTime VARCHAR(30)" +
                "); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER TABLE CLASSINFO ADD classcode VARCHAR(30)");
    }

    public boolean addClassesInfo(List<ClassDetail> classDetails, Context context) {
        int i = 0;
        db.execSQL("DELETE FROM CLASSINFO");
        StringBuffer stringBuffer = new StringBuffer();
        String dot = ",";
        if (classDetails.size() == 0) {
            Toast.makeText(context, "添加失败，请重试!", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int j = 0; j < classDetails.size(); j++) {
//            Log.e("TAG", "addClassesInfo: " + classDetails.get(j).toString());
            stringBuffer.append("insert into CLASSINFO VALUES(null,");
            stringBuffer.append(classDetails.get(j).generateSQL());
            stringBuffer.append(";");
            db.execSQL(stringBuffer.toString());
            stringBuffer.delete(0, stringBuffer.length() - 1);
        }
        return true;
    }

    public Cursor queryInfo(String sql, String[] selectArgs) {
        Cursor cursor = db.rawQuery(sql, selectArgs);
        return cursor;
    }

    public void addMyOwnClass(ClassDetail classDetail) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO CLASSINFO VALUES(null,");
        stringBuilder.append(classDetail.generateSQL());
        stringBuilder.append(";");
        db.execSQL(stringBuilder.toString());
    }
}
