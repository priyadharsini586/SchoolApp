package com.nickteck.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.EvenImageCardViewFragment;
import com.nickteck.schoolapp.model.CommonImageVideoEventData;
import com.nickteck.schoolapp.model.ShowEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.nickteck.schoolapp.utilclass.Constants.EVENT_IMAGE_CARD_VIEW_FRAGMENT;

/**
 * Created by admin on 6/23/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    Activity activity;
    ArrayList<ShowEvent.EventDetails> showEventArrayList;
    Context context;
    private ArrayList<CommonImageVideoEventData> LocalImageArrayList = new ArrayList<>();

    public EventAdapter(Activity activity, ArrayList<ShowEvent.EventDetails> showEventArrayList,Context context) {
        this.activity = activity;
        this.showEventArrayList = showEventArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_event_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title_textview.setText(showEventArrayList.get(position).getTitle());
        if(showEventArrayList.get(position).getHeld_on()!= null){
            String postedDateValue = showEventArrayList.get(position).getHeld_on();
            changeDateFormat(postedDateValue,holder,"Held");
        }
     //   holder.held_on_date.setText(showEventArrayList.get(position).getHeld_on());
        holder.announcemnt_message.setText(showEventArrayList.get(position).getContent());

        if(showEventArrayList.get(position).getDate()!=null){
            String DateValue = showEventArrayList.get(position).getDate();
            changeDateFormat(DateValue,holder,"Post");
        }

        if(showEventArrayList.get(position).getImage_details().size()>0){
            holder.image_icon.setVisibility(View.VISIBLE);
            holder.image_icon.setImageResource(R.mipmap.image_galler);

        }if(showEventArrayList.get(position).getVideo_details().size()>0){
            holder.video_icon.setVisibility(View.VISIBLE);
            holder.video_icon.setImageResource(R.mipmap.videooo);
        }


        holder.event_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(showEventArrayList.get(position).getImage_details().size()> 0 ||
                        showEventArrayList.get(position).getVideo_details().size()>0 ){

                    for(int i=0; i<showEventArrayList.get(position).getImage_details().size(); i++){
                        CommonImageVideoEventData commonImageVideoEventData = new CommonImageVideoEventData();
                        commonImageVideoEventData.setImage_url(showEventArrayList.get(position).getImage_details().get(i).getImg_url());
                        commonImageVideoEventData.setImage_description(showEventArrayList.get(position).getImage_details().get(i).getDescription());
                        commonImageVideoEventData.setType(0);
                        LocalImageArrayList.add(commonImageVideoEventData);

                    }

                    for(int i=0; i<showEventArrayList.get(position).getVideo_details().size();i++){
                        CommonImageVideoEventData commonImageVideoEventData = new CommonImageVideoEventData();
                        commonImageVideoEventData.setVideo_url(showEventArrayList.get(position).getVideo_details().get(i).getVideo_url());
                        commonImageVideoEventData.setVideo_description(showEventArrayList.get(position).getVideo_details().get(i).getDescription());
                        commonImageVideoEventData.setType(1);
                        LocalImageArrayList.add(commonImageVideoEventData);
                    }

                    String getSelectedTitle = showEventArrayList.get(position).getTitle();

                    EvenImageCardViewFragment cardViewFragment = new EvenImageCardViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("commonArrayList",LocalImageArrayList);
                    bundle.putString("selectedTitle",getSelectedTitle);
                    cardViewFragment.setArguments(bundle);
                    HelperClass.replaceFragment(cardViewFragment,EVENT_IMAGE_CARD_VIEW_FRAGMENT,(AppCompatActivity)context);

                }

            }
        });


    }

    private void changeDateFormat(String dateValue, ViewHolder holder,String checkValue) {
        String date_value = dateValue;
        DateFormat readFormat = new SimpleDateFormat( "dd MMM yyyy hh:mm");
        Date d = null;
        try {
            d = readFormat.parse(date_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat(" dd MMM yyyy h:mma");
        String finalDate_format =f2.format(d);

        if(checkValue.equals("Held") ){
            holder.held_on_date.setText(finalDate_format);
        }else {
            holder.posted_date.setText(finalDate_format);
        }



    }

    @Override
    public int getItemCount() {
        return showEventArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_textview;
        TextView held_on_date;
        TextView announcemnt_message;
        ImageView image_icon;
        ImageView video_icon;
        LinearLayout event_linearLayout;
        TextView posted_date;

        public ViewHolder(View itemView) {
            super(itemView);
            event_linearLayout = (LinearLayout) itemView.findViewById(R.id.event_linearLayout);
            title_textview = (TextView)itemView.findViewById(R.id.title_textview);
            held_on_date = (TextView) itemView.findViewById(R.id.date_time);
            announcemnt_message = (TextView) itemView.findViewById(R.id.announcemnt_message);
            image_icon = (ImageView) itemView.findViewById(R.id.image_icon);
            video_icon = (ImageView)itemView.findViewById(R.id.video_icon);
            posted_date = (TextView) itemView.findViewById(R.id.posted_date);
        }
    }
}
