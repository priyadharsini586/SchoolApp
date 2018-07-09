package com.nickteck.schoolapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.AboutChildFragment;
import com.nickteck.schoolapp.fragment.AnnoncementFragment;
import com.nickteck.schoolapp.fragment.BusTrackFragment;
import com.nickteck.schoolapp.fragment.CalendarFragment;
import com.nickteck.schoolapp.fragment.EventsFragment;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.interfaces.OnGetDataFromBusApp;
import com.nickteck.schoolapp.utilclass.Config;
import com.nickteck.schoolapp.utilclass.Constants;

public class CommonFragmentActivity extends AppCompatActivity {

    String fromFragment = null,childID = null;
    private LinearLayout mainViewActivity;
    protected  OnBackPressedListener onBackPressedListener ;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = CommonFragmentActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_child);

        mainViewActivity = (LinearLayout) findViewById(R.id.mainViewActivity);
        setupForSrvice();
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
            annoncementFragment.getChildID(childID);
            HelperClass.replaceFragment(annoncementFragment, Constants.ANNOUNEMENT_FRAGMENT, CommonFragmentActivity.this);
        }else if (fromFragment.equals(Constants.CALENDAR_FRAGMENT)){
            CalendarFragment calendarFragment = new CalendarFragment();
            HelperClass.replaceFragment(calendarFragment,Constants.CALENDAR_FRAGMENT,CommonFragmentActivity.this);
        }else if (fromFragment.equals(Constants.EVENTS_FRAGMENT)){
            EventsFragment eventsFragment = new EventsFragment();
            HelperClass.replaceFragment(eventsFragment, Constants.EVENTS_FRAGMENT, CommonFragmentActivity.this);
        }else if (fromFragment.equals(Constants.BUS_TRACK_FRAGMENT)){
            BusTrackFragment busTrackFragment = new BusTrackFragment();
            HelperClass.replaceFragment(busTrackFragment, Constants.BUS_TRACK_FRAGMENT, CommonFragmentActivity.this);
        }



        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressed();

        }
        else {
            super.onBackPressed();

        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }



    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();



    }


    public void setupForSrvice(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

        FirebaseMessaging.getInstance().subscribeToTopic("latlan");

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
    }


}
