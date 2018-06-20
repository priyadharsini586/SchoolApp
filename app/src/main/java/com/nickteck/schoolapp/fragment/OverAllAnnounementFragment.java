package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickteck.schoolapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverAllAnnounementFragment extends Fragment {


    public OverAllAnnounementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_over_all_announement, container, false);
    }

}
