package com.hitesh.sensordetector.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.benevolenceinc.sensordetector.R;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hitesh.sensordetector.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> list = new ArrayList<String>();
    private int mPosition;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Create a full screen content callback.
        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                mInterstitialAd = null;
                // Proceed to the next level.
                goToDetailPage();
            }
        };

        // Load an interstitial ad. When a natural transition in the app occurs (such as a level
        // ending in a game), show the interstitial. In this simple example, the press of a
        // button is used instead.
        //
        // If the button is clicked before the interstitial is loaded, the user should proceed to
        // the next part of the app (in this case, the next level).
        //
        // If the interstitial is finished loading, the user will view the interstitial before
        // proceeding.
        InterstitialAd.load(
                this,
                getString(R.string.interstitial_id),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        mInterstitialAd = ad;
                        mInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                    }
                });
        list.add(getString(R.string.ir));
        list.add(getString(R.string.gyroscope));
        list.add(getString(R.string.accelerometer));
        list.add(getString(R.string.ambient_temperature));
        list.add(getString(R.string.magnetic));
        list.add(getString(R.string.light));
        list.add(getString(R.string.proximity));
        list.add(getString(R.string.pressure));
        list.add(getString(R.string.game_rotation));
        list.add(getString(R.string.step_detector));
        list.add(getString(R.string.linear_accelaration));
        list.add(getString(R.string.gravity));
        list.add(getString(R.string.all));

        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(list,getApplicationContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                if(mInterstitialAd != null){
                    mInterstitialAd.show(MainActivity.this);
                }else {
                   goToDetailPage();
                }
            }
        });
    }

    private void goToDetailPage() {
        if(list.get(mPosition).equalsIgnoreCase(getString(R.string.all))){
            Log.d("Nothing","To be done");
            Intent intent = new Intent(MainActivity.this,AllSensorActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MainActivity.this,DetectSensorActivity.class);
            intent.putExtra("sensor",list.get(mPosition));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.back_alert_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        TextView tvRate = (TextView)dialogView.findViewById(R.id.tvRate);
        TextView tvYes = (TextView)dialogView.findViewById(R.id.tvYes);
        TextView tvNo = (TextView)dialogView.findViewById(R.id.tvNo);
        TextView tvShare = (TextView)dialogView.findViewById(R.id.tvShare);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" +getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                alertDialog.dismiss();

            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,R.string.app_name);
                    String sAux = "\nDetect sensors in your device.\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+getPackageName()+"\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose to Share"));
                } catch(Exception e) {
                    //e.toString();
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

}
