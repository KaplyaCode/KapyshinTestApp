package com.example.kapyshintestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import com.appsflyer.AppsFlyerLib;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref;
    final String ACCESS = "acesGranted";
    private Boolean access = false;
    private Timer timer;
    private TimerTask timerTask;
    private AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
    private static String ONESIGNAL_APP_ID = "6ffbbe4f-9e83-41b6-a8d8-c1f678ac825d";

    private WebViewFragment webFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appsflyer.init(getString(R.string.appsFlyer_id), null, this);
        appsflyer.start(this);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        timer = new Timer();

        FragmentSelector();

    }

    public void FragmentSelector(){
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

        try {
            access = sPref.getBoolean(ACCESS, false);
        }  catch (Exception e){}

        if (!access){
            StartFragment fragment = new StartFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }else startTimer();
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isOnline(MainActivity.this)){
                            NoInternetFragment fragment = new NoInternetFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.container, fragment);
                            fragmentTransaction.commit();
                            webFragment = null;
                        }else if (webFragment == null){
                            webFragment = new WebViewFragment();

                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.container, webFragment);
                            fragmentTransaction.commit();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public void onClickExit(View view) {
        finish();
        System.exit(0);
    }

    public void onClickAccept(View view) {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(ACCESS, true);
        ed.commit();
        FragmentSelector();
    }
}