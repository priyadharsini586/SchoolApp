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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    ImageView slider_imageView;
    int page = 0;
    Animation animation;

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
        sliderImages.add(R.drawable.ic_splash_screen);

        slider_imageView = (ImageView) findViewById(R.id.slider_imageView);
        btnSubmit = (CircularProgressButton) findViewById(R.id.btnSubmit);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.login_img_slide);
        testing();
    }

    private void testing() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page == sliderImages.size()-1) {
                    page = 0;
                }else
                {
                    page = page + 1;
                }
                slider_imageView.startAnimation(animation);
                slider_imageView.setImageResource(sliderImages.get(page));
                testing();
            }
        }, 2000);
    }





    private void setOnClickListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.startAnimation();
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
