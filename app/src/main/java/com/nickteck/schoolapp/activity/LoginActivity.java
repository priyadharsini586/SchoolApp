package com.nickteck.schoolapp.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.nickteck.schoolapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity {


    CircularProgressButton btnSubmit;
    ArrayList<Integer> sliderImages = new ArrayList<>();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static ViewPager mPager;
    CirclePageIndicator indicator;
    ImageView slider_imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        init();
        setOnClickListener();

    }



    private void init() {
        sliderImages.add(R.drawable.slide_1);
        sliderImages.add(R.drawable.silde_2);
        sliderImages.add(R.drawable.slide_3);

        slider_imageView = (ImageView) findViewById(R.id.slider_imageView);

        btnSubmit = (CircularProgressButton) findViewById(R.id.btnSubmit);

    }






    private void setOnClickListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.startAnimation();
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  mVideoView.pause();
    }
}
