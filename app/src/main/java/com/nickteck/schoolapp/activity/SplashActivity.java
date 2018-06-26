package com.nickteck.schoolapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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
        DataBaseHandler dataBaseHandler = new DataBaseHandler(getApplicationContext());
        if (dataBaseHandler.checkTableIsEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();

        }else {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        /*new Handler().postDelayed(new Runnable() {
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
        },SPLASH_TIME_OUT);*/
    }


}
