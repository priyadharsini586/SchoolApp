package com.nickteck.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.CommonAnnouncmentFragment;
import com.nickteck.schoolapp.model.AnnoncementDetails;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 6/21/2018.
 */

public class CommonAnnouncementAdapter extends RecyclerView.Adapter<CommonAnnouncementAdapter.ViewHolder> {

    Context context;
    Activity mactivity;
    ArrayList<AnnoncementDetails.CommonAnnounacementDetails> mannounacementDetails;


    public CommonAnnouncementAdapter(Activity activity, ArrayList<AnnoncementDetails.CommonAnnounacementDetails>
            announacementDetails) {
        this.mactivity = activity;
        this.mannounacementDetails = announacementDetails;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.common_announcement_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title_textview.setText(mannounacementDetails.get(position).getTitle());
        holder.announcemnt_message.setText(mannounacementDetails.get(position).getMessage());
        holder.date_month.setText(mannounacementDetails.get(position).getDate());
        holder.teacher_staff_linear.setVisibility(View.GONE);
        holder.staff_name.setText(mannounacementDetails.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return mannounacementDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_textview;
        TextView staff_name;
        TextView announcemnt_message;
        TextView date_month;
        TextView time;
        LinearLayout teacher_staff_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            title_textview = (TextView) itemView.findViewById(R.id.title_textview);
            staff_name = (TextView) itemView.findViewById(R.id.staff_name);
            announcemnt_message = (TextView) itemView.findViewById(R.id.announcemnt_message);
            date_month = (TextView) itemView.findViewById(R.id.date_month);
            time = (TextView) itemView.findViewById(R.id.time);
            teacher_staff_linear = (LinearLayout) itemView.findViewById(R.id.teacher_staff_linear);
        }
    }
}
