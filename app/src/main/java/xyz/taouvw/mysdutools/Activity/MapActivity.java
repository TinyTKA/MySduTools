package xyz.taouvw.mysdutools.Activity;

import android.content.res.Resources;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.IOException;
import java.util.Properties;

import xyz.taouvw.mysdutools.MyView.PinView;
import xyz.taouvw.mysdutools.R;

public class MapActivity extends CheckPermissionsActivity {
    PinView map;
    private TextView tvResult;
    Double longitude;   //经度
    Double latitude;    //纬度
    float angle;
    float speed;
    Resources resources;
    DisplayMetrics dm;
    Properties properties = new Properties();
    PointF positionXY;
    float lastAngle = 0f;
    PointF[] positionXYs;
    PointF[] positionLALTs;
    PointF pointF = new PointF();

    Toolbar tb;


    int screenWidth;
    int screenHeight;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    SensorManager sm;
    Sensor mSensorOrientation;
    float[] sensorValue = new float[10];
    int count = 0;
    double[] locationValueLa = new double[2];
    double[] locationValueLo = new double[2];
    int count2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toast.makeText(this, "地图较大，加载需要时间，请稍等", Toast.LENGTH_LONG).show();
        init();
        initLocation();
        startLocation();
    }

    private void init() {
        positionXY = new PointF();
//        tvResult = this.findViewById(R.id.tvR);
        resources = this.getResources();
        dm = resources.getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;

        tb = this.findViewById(R.id.tbOfMap);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        map = this.findViewById(R.id.map2);
        ImageSource resource = ImageSource.resource(R.mipmap.map2);
        map.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        map.setMaxScale(1.4f);
        map.setImage(resource);
        try {
            properties.load(getAssets().open("values.properties"));
            initPosition();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        positionXY.set(Float.parseFloat(properties.getProperty("leftUpX")), Float.parseFloat(properties.getProperty("leftUpY")));
        map.setToxy(positionXY);

        //初始化传感器
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sm.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (count == 10) {
                    int average = 0;
                    count = 0;
                    for (int i = 0; i < sensorValue.length; i++) {
                        average += sensorValue[i];
                    }
                    average /= sensorValue.length;

                    map.setOri(average);
                } else {
                    sensorValue[count] = sensorEvent.values[0];
                    count++;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }, mSensorOrientation, SensorManager.SENSOR_DELAY_UI);
    }


    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {

        //初始化client
        try {
            AMapLocationClient.updatePrivacyAgree(this, true);
            AMapLocationClient.updatePrivacyShow(this, true, true);
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationOption = getDefaultOption();

            //设置定位参数
            locationClient.setLocationOption(locationOption);
            // 设置定位监听
            locationClient.setLocationListener(locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(1500);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {

            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    angle = location.getBearing();
                    speed = location.getSpeed();
//                    if (count2 == 2) {
//                        double averageLa = 0f;
//                        double averageLo = 0f;
//                        count2 = 0;
//                        for (int i = 0; i < locationValueLa.length; i++) {
//                            averageLo += locationValueLo[i];
//                            averageLa += locationValueLa[i];
//                        }
//                        averageLa /= locationValueLa.length;
//                        averageLo /= locationValueLo.length;
//                        PointF position = getPosition(averageLo, averageLa);
//                        map.setPin(position);

//                    } else {
//                        locationValueLa[count2] = latitude;
//                        locationValueLo[count2] = longitude;
//                        count2++;
//                    }

                    PointF position = getPosition(longitude, latitude);
                    Log.e("经纬度", "onLocationChanged: " + longitude + "  " + latitude);
                    Log.e("坐标", "onLocationChanged: " + position.x + "  " + position.y);
                    map.setPin(position);

                    //定位完成的时间
                } else {
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "请打开定位开关", Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        try {
            //根据控件的选择，重新设置定位参数
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位

            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        try {
            // 停止定位
            locationClient.stopLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocation();
        destroyLocation();
    }

    private void initPosition() {
        positionXYs = new PointF[5];
        positionLALTs = new PointF[5];
        // 左上角，顺序：经度，纬度
        positionXYs[0] = new PointF();
        positionLALTs[0] = new PointF();
        positionXYs[0].set(Float.parseFloat(properties.getProperty("leftUpX")), Float.parseFloat(properties.getProperty("leftUpY")));
        positionLALTs[0].set(Float.parseFloat(properties.getProperty("leftUpLongitude")), Float.parseFloat(properties.getProperty("leftUpLatitude")));
        // 右上角，顺序：经度，纬度
        positionXYs[1] = new PointF();
        positionLALTs[1] = new PointF();
        positionXYs[1].set(Float.parseFloat(properties.getProperty("rightUpX")), Float.parseFloat(properties.getProperty("rightUpY")));
        positionLALTs[1].set(Float.parseFloat(properties.getProperty("rightUpLongitude")), Float.parseFloat(properties.getProperty("rightUpLatitude")));
        // 左下角，顺序：经度，纬度
        positionXYs[2] = new PointF();
        positionLALTs[2] = new PointF();
        positionXYs[2].set(Float.parseFloat(properties.getProperty("leftBottomX")), Float.parseFloat(properties.getProperty("leftBottomY")));
        positionLALTs[2].set(Float.parseFloat(properties.getProperty("leftBottomLongitude")), Float.parseFloat(properties.getProperty("leftBottomLatitude")));
        // 右下角，顺序：经度，纬度
        positionXYs[3] = new PointF();
        positionLALTs[3] = new PointF();
        positionXYs[3].set(Float.parseFloat(properties.getProperty("rightBottomX")), Float.parseFloat(properties.getProperty("rightBottomY")));
        positionLALTs[3].set(Float.parseFloat(properties.getProperty("rightBottomLongitude")), Float.parseFloat(properties.getProperty("rightBottomLatitude")));
        // 东门，顺序：经度，纬度
        positionXYs[4] = new PointF();
        positionLALTs[4] = new PointF();
        positionXYs[4].set(Float.parseFloat(properties.getProperty("eastX")), Float.parseFloat(properties.getProperty("eastY")));
        positionLALTs[4].set(Float.parseFloat(properties.getProperty("eastLongitude")), Float.parseFloat(properties.getProperty("eastLatitude")));

    }

    private PointF getPosition(Double Longitude, Double latitude) {
        PointF pointF = new PointF();

        if (Longitude > positionLALTs[3].x || Longitude < positionLALTs[2].x || latitude > positionLALTs[1].y || latitude < positionLALTs[3].y) {
            Toast.makeText(getApplicationContext(), "您已经出校！无法定位", Toast.LENGTH_SHORT).show();
            pointF.x = 0;
            pointF.y = 0;
            return pointF;
        }
        pointF.x = (float) (positionXYs[2].x + (longitude - positionLALTs[2].x) * ((positionXYs[3].x - positionXYs[2].x) / (positionLALTs[3].x - positionLALTs[2].x)));
        pointF.y = (float) (positionXYs[3].y - (latitude - positionLALTs[3].y) * ((positionXYs[3].y - positionXYs[1].y) / (positionLALTs[1].y - positionLALTs[3].y)));
        pointF.x += 55;
        pointF.y += 145;

        return pointF;
    }

}