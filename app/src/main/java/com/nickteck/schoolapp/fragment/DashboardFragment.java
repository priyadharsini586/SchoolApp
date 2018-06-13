package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.DashboardActivity;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;

import org.jetbrains.annotations.NotNull;


public class DashboardFragment extends Fragment  implements OnBackPressedListener{

    View mainView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if ((DashboardActivity)getActivity() != null)
            ((DashboardActivity) getActivity()).setOnBackPressedListener(this);
        return mainView;
    }


    @Override
    public void onBackPressed() {
        ((DashboardActivity)getActivity()).finish();
    }
}
