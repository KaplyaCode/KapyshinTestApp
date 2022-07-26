package com.example.kapyshintestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button exitButton;

    SharedPreferences sPref;
    final String ACCESS = "acesGranted";
    private Boolean access = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

        try {
            access = sPref.getBoolean(ACCESS, false);
        }  catch (Exception e){}

        if (!access){
            StartFragment fragment = new StartFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }else if (true){
            NoInternetFragment fragment = new NoInternetFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }



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
    }
}