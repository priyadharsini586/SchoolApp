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
public class SpecificAnnouncementFrgament extends Fragment {
    View view;


    public SpecificAnnouncementFrgament() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_specific_announcement_frgament, container, false);
        return view;
    }

}
