package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickteck.schoolapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnoncementFragment extends Fragment {

    public  TabLayout tabLayout;
    public  ViewPager viewPager;
    public  int int_items = 2;
    private View mainView;


    public AnnoncementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView =  inflater.inflate(R.layout.fragment_annoncement, container, false);
        tabLayout = (TabLayout) mainView.findViewById(R.id.tabs);
        viewPager = (ViewPager) mainView.findViewById(R.id.viewpager_announacment);


        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return mainView;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CommonAnnouncmentFragment();
                case 1:
                    return new SpecificAnnouncementFrgament();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    String recent_news = "Over All Announcement";
                    return recent_news;
                case 1:
                    String category = "Specific Announcement";
                    return category;
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }
}
