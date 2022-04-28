package xyz.taouvw.mysdutools.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import xyz.taouvw.mysdutools.R;

public class SplashActivity extends AppCompatActivity {

    public static int[] images = new int[]{R.mipmap.firstpage1, R.mipmap.firstpage2, R.mipmap.firstpage3,
            R.mipmap.firstpage4, R.mipmap.firstpage5, R.mipmap.firstpage6, R.mipmap.firstpage7};
    ImageView imageView;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imageView = this.findViewById(R.id.firstpage);
        imageView.setImageResource(images[random.nextInt(7)]);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}