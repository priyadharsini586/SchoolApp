package com.nickteck.schoolapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.AboutChildFragment;
import com.nickteck.schoolapp.utilclass.Constants;

public class AboutChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_child);


        AboutChildFragment aboutChildFragment = new AboutChildFragment();
        HelperClass.replaceFragment(aboutChildFragment, Constants.ABOUT_CHILD_FRAGMENT,AboutChildActivity.this);

    }
}
