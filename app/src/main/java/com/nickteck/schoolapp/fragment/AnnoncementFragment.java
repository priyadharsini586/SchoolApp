package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnoncementFragment extends Fragment implements OnBackPressedListener {

    public  TabLayout tabLayout;
    public  ViewPager viewPager;
    public  int int_items = 2;
    private View mainView;
    private Toolbar toolBarTitle;
    String childId = null;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mainView =  inflater.inflate(R.layout.fragment_annoncement, container, false);
        tabLayout = (TabLayout) mainView.findViewById(R.id.tabs);
        viewPager = (ViewPager) mainView.findViewById(R.id.viewpager_announacment);

        toolBarTitle = (Toolbar) getActivity().findViewById(R.id.toolBarTitle);
        TextView toolBarTextView = (TextView) toolBarTitle.findViewById(R.id.toolBarTextView);
        toolBarTextView.setText(" ");
        toolBarTitle.setVisibility(View.GONE);

        if ((CommonFragmentActivity)getActivity() != null) {
            ((CommonFragmentActivity) getActivity()).setOnBackPressedListener(this);
        }
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return mainView;
    }

    @Override
    public void onBackPressed() {
        ((CommonFragmentActivity)getActivity()).finish();
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
                case 1: {
                    SpecificAnnouncementFrgament specificAnnouncementFrgament = new SpecificAnnouncementFrgament();
                    specificAnnouncementFrgament.childId(childId);
                    return specificAnnouncementFrgament;
                }
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
                    String recent_news = "Common Announcement";
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


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    public void getChildID(String childId){
        this.childId = childId;
    }
}
