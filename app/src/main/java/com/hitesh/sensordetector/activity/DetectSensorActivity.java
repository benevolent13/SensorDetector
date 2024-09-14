package com.hitesh.sensordetector.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.benevolenceinc.sensordetector.R;
import com.hitesh.sensordetector.progressindicator.AVLoadingIndicatorView;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Created by Hitesh on 12/19/18.
 */
public class DetectSensorActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private TextView tvTitle, tvSuccessMessage, tvName, tvDescription;
    private ImageView ivSuccess;
    int type, descriptionIndex;
    String sensorName;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            sensorName = getIntent().getStringExtra("sensor");
        }
        setContentView(R.layout.detail_layout);
        Random random = new Random();
        int loader = random.nextInt(6);
        String[] loaderArray = { "BallGridPulseIndicator", "BallClipRotatePulseIndicator", "SquareSpinIndicator",
                "PacmanIndicator",
                "BallTrianglePathIndicator", "LineScalePartyIndicator" };

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutDetails);
        final AVLoadingIndicatorView indicatorView = (AVLoadingIndicatorView) findViewById(R.id.loader);
        indicatorView.setIndicator(loaderArray[loader]);
        tvTitle = (TextView) findViewById(R.id.appBarTitle);
        ivSuccess = (ImageView) findViewById(R.id.ivIcon);
        tvSuccessMessage = (TextView) findViewById(R.id.tvSuccessMessage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvTitle.setText(sensorName);
        switch (sensorName) {
            case "Accelerometer":
                type = Sensor.TYPE_ACCELEROMETER;
                descriptionIndex = 0;
                break;
            case "Gyroscope":
                type = Sensor.TYPE_GYROSCOPE;
                descriptionIndex = 1;
                break;
            case "Ambient Temperature":
                type = Sensor.TYPE_AMBIENT_TEMPERATURE;
                descriptionIndex = 2;
                break;
            case "Magnetic":
                type = Sensor.TYPE_MAGNETIC_FIELD;
                descriptionIndex = 3;
                break;
            case "Light":
                type = Sensor.TYPE_LIGHT;
                descriptionIndex = 4;
                break;
            case "Proximity":
                type = Sensor.TYPE_PROXIMITY;
                descriptionIndex = 5;
                break;
            case "Pressure":
                type = Sensor.TYPE_PRESSURE;
                descriptionIndex = 6;
                break;
            case "Game Rotation Vector":
                type = Sensor.TYPE_GAME_ROTATION_VECTOR;
                descriptionIndex = 7;
                break;
            case "Step Detector":
                type = Sensor.TYPE_STEP_DETECTOR;
                descriptionIndex = 8;
                break;
            case "Linear Acceleration":
                type = Sensor.TYPE_LINEAR_ACCELERATION;
                descriptionIndex = 9;
                break;
            case "Gravity":
                type = Sensor.TYPE_GRAVITY;
                descriptionIndex = 10;
                break;
            case "IR":
                type = 1306;
                descriptionIndex = 11;
                break;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                indicatorView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                if (type == 1306) {
                    detectIR();
                } else {
                    detectSensor(type, sensorName, descriptionIndex);
                }

            }
        }, 2000);

    }

    String[] descriptionArray = {
            "An accelerometer sensor reports the acceleration of the device along the 3 sensor axes.",
            "A gyroscope sensor reports the rate of rotation of the device around the 3 sensor axes.",
            "An ambient Temperature sensor provides the ambient (room) temperature in degrees Celsius.",
            "A magnetic field sensor (also known as magnetometer) reports the ambient magnetic field, as measured along the 3 sensor axes.",
            "A light sensor reports the current illumination in SI lux units.",
            "A proximity sensor reports the distance from the sensor to the closest visible surface.",
            "A pressure sensor (also known as barometer) reports the atmospheric pressure in hectopascal (hPa).",
            "A game rotation vector sensor is similar to a rotation vector sensor but not using the geomagnetic field. "
                    +
                    "Therefore the Y axis doesn't point north but instead to some other reference. " +
                    "That reference is allowed to drift by the same order of magnitude as the gyroscope drifts around the Z axis.",
            "A step detector generates an event each time a step is taken by the user.",
            "A linear acceleration sensor reports the linear acceleration of the device in the sensor frame, not including gravity.",
            "A gravity sensor reports the direction and magnitude of gravity in the device's coordinates.",
            "The IR stands for infrared.Most remote controls use infrared to communicate with home entertainment components."
    };

    public void detectIR() {
        ConsumerIrManager irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        if (irManager != null) {
            if (irManager.hasIrEmitter()) {
                present();
            } else {
                notPresent();
            }
        } else {
            notPresent();
        }
    }

    public void detectSensor(int type, String sensorName, int descriptionIndex) {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(type) != null) {
            present();
        } else {
            notPresent();
        }
    }

    public void present() {
        tvSuccessMessage.setText("Kudos! You have the " + sensorName + " sensor.");
        tvName.setText("Name :" + "\n" + mSensorManager.getDefaultSensor(type).getName());
        tvDescription.setText(descriptionArray[descriptionIndex]);
        ivSuccess.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.right));
    }

    public void notPresent() {
        tvSuccessMessage.setText("Oops! You don't have the " + sensorName + " sensor.");
        tvName.setText("");
        tvDescription.setText(descriptionArray[descriptionIndex]);
        ivSuccess.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wrong));
    }
}
