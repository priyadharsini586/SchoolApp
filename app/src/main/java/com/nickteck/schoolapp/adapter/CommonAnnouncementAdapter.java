package com.nickteck.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.CommonAnnouncmentFragment;

import org.w3c.dom.Text;

/**
 * Created by admin on 6/21/2018.
 */

public class CommonAnnouncementAdapter extends RecyclerView.Adapter<CommonAnnouncementAdapter.ViewHolder> {

    Context context;
    Activity activity;
    CommonAnnouncmentFragment morderTakenFragment;


    public CommonAnnouncementAdapter() {
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.common_announcement_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_textview;
        TextView staff_name;
        TextView announcemnt_message;
        TextView date_month;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            title_textview = (TextView) itemView.findViewById(R.id.title_textview);
            staff_name = (TextView) itemView.findViewById(R.id.staff_name);
            announcemnt_message = (TextView) itemView.findViewById(R.id.announcemnt_message);
            date_month = (TextView) itemView.findViewById(R.id.date_month);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
