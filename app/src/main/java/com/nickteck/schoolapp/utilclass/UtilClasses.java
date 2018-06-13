package com.nickteck.schoolapp.utilclass;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.nickteck.schoolapp.R;

/**
 * Created by admin on 6/13/2018.
 */

public class UtilClasses {

    public static void setFragmentContainer(AppCompatActivity mContext, Fragment fragment, String fragmentTag){
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.frag_slide_in_from_right, R.anim.frag_slide_out_to_left);
        fragmentTransaction.add(R.id.fragmentContainer, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }
}
