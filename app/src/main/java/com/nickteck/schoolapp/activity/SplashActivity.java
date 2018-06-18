package com.nickteck.schoolapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.LoginActivity;
import com.nickteck.schoolapp.database.DataBaseHandler;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DataBaseHandler dataBaseHandler = new DataBaseHandler(getApplicationContext());
                if (dataBaseHandler.checkTableIsEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }

            }
        },SPLASH_TIME_OUT);
    }
}
