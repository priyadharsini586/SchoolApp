package com.nickteck.schoolapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.AboutChildFragment;
import com.nickteck.schoolapp.fragment.AnnoncementFragment;
import com.nickteck.schoolapp.utilclass.Constants;

public class CommonFragmentActivity extends AppCompatActivity {

    String fromFragment = null,childID = null;
    private LinearLayout mainViewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_child);

        mainViewActivity = (LinearLayout) findViewById(R.id.mainViewActivity);

        if (getIntent().hasExtra("from")) {
            fromFragment = getIntent().getStringExtra("from");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras from");
        }

        if (getIntent().hasExtra("childId")) {
            childID = getIntent().getStringExtra("childId");
        } else {
           // throw new IllegalArgumentException("Activity cannot find  extras from");
        }
        if (fromFragment.equals(Constants.ABOUT_CHILD_FRAGMENT)) {
            AboutChildFragment aboutChildFragment = new AboutChildFragment();
            aboutChildFragment.childId(childID);
            HelperClass.replaceFragment(aboutChildFragment, Constants.ABOUT_CHILD_FRAGMENT, CommonFragmentActivity.this);
        }else if (fromFragment.equals(Constants.ANNOUNEMENT_FRAGMENT)) {
            AnnoncementFragment annoncementFragment = new AnnoncementFragment();
            HelperClass.replaceFragment(annoncementFragment, Constants.ANNOUNEMENT_FRAGMENT, CommonFragmentActivity.this);
        }

    }
}
