package com.nickteck.schoolapp.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nickteck.schoolapp.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/20/2018.
 */

public class StudentCustomListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> studentNameList;


    public StudentCustomListAdapter(Activity activity, ArrayList<String> getStudentNameArrayList) {
        this.activity = activity;
        this.studentNameList = getStudentNameArrayList;

    }

    @Override
    public int getCount() {
        return studentNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate the layout for each list row
        if (convertView == null) {

            convertView = LayoutInflater.from(activity).inflate(R.layout.custom_student_list, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.student_name);
            textView.setText(studentNameList.get(position));

           // View view = (View) convertView.findViewById(R.id.name_seperator);

        }
        return convertView;
    }
}
