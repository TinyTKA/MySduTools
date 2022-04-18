package xyz.taouvw.mysdutools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class PropertiesUtils {
    private final static String TAG = "PropertiesUtil";

    private Context mContext;
    private String mFile;
    private Properties mProp;
    private InputStream inputStream;
    private static PropertiesUtils mPropUtil = null;

    public static PropertiesUtils getInstance(Context context, String mFile) {
        if (mPropUtil == null) {
            mPropUtil = new PropertiesUtils();
            mPropUtil.mFile = mFile;
            mPropUtil.mContext = context;
        }
        return mPropUtil;
    }

    public PropertiesUtils init() {
        Log.d(TAG, "path=" + mFile);
        try {
            inputStream = mContext.getAssets().open(mFile);
            mProp = new Properties();
            mProp.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void commit() {
        try {
            File file = new File("/assets/" + mFile);
            OutputStream os = new FileOutputStream(file);
            mProp.store(os, "");
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProp.clear();
    }

    public void clear() {
        mProp.clear();
    }

    public void writeString(String name, String value) {
        mProp.setProperty(name, value);
    }

    public String readString(String name, String defaultValue) {
        return mProp.getProperty(name, defaultValue);
    }

    public void writeInt(String name, int value) {
        mProp.setProperty(name, "" + value);
    }

    public int readInt(String name, int defaultValue) {
        return Integer.parseInt(mProp.getProperty(name, "" + defaultValue));
    }

    public void writeBoolean(String name, boolean value) {
        mProp.setProperty(name, "" + value);
    }

    public boolean readBoolean(String name, boolean defaultValue) {
        return Boolean.parseBoolean(mProp.getProperty(name, "" + defaultValue));
    }

    public void writeDouble(String name, double value) {
        mProp.setProperty(name, "" + value);
    }

    public double readDouble(String name, double defaultValue) {
        return Double.parseDouble(mProp.getProperty(name, "" + defaultValue));
    }

}