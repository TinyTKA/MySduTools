package xyz.taouvw.mysdutools.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceUtils {
    private Context mContext;
    private String file;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtils(Context context, String file) {
        this.mContext = context;
        this.file = file;
        sharedPreferences = mContext.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //定义一个保存数据的方法
    public void save(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    //定义一个读取 SP 文件的方法
    public String read(String key, String defaultValue) {
        if (!sharedPreferences.contains(key)) {
            this.save(key, defaultValue);
            return defaultValue;
        }
        return sharedPreferences.getString(key, "");
    }

    public void commit() {
        editor.commit();
    }
}
