package com.hitesh.sensordetector.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.benevolenceinc.sensordetector.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hitesh on 12/20/18.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        };
        timer.schedule(timerTask,2000);
    }
}
