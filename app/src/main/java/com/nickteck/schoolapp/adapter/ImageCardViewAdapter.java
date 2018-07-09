package com.nickteck.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.YouTubeActivity;
import com.nickteck.schoolapp.model.CommonImageVideoEventData;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by admin on 6/26/2018.
 */

public class ImageCardViewAdapter extends RecyclerView.Adapter<ImageCardViewAdapter.ViewHolder> implements NetworkChangeReceiver.ConnectivityReceiverListener {

    Activity activity;
    Context context;
    ArrayList<String> ImageArrayList = new ArrayList<>();
    ArrayList<CommonImageVideoEventData> mcommonArrayList = new ArrayList<>();
    private String final1;
    private String concatUrl;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;

    public ImageCardViewAdapter(Activity activity, ArrayList<CommonImageVideoEventData> commonArrayList,Context context) {
        this.activity = activity;
        this.context = context;
        mcommonArrayList = commonArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mcommonArrayList.get(position).getType()) {
            case 0:
                return CommonImageVideoEventData.IMAGE_TYPE;
            case 1:
                return CommonImageVideoEventData.VIDEO_TYPE;
            default:
                return -1;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CommonImageVideoEventData.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_view_adapter, parent, false);
                return new ViewHolder(view);

            case CommonImageVideoEventData.VIDEO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_card_view_adapter, parent, false);
                return new ViewHolder(view);

        }
        return null;

    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        CommonImageVideoEventData commonImageVideoEventData = mcommonArrayList.get(position);
        if (commonImageVideoEventData != null) {
            switch (commonImageVideoEventData.getType()) {
                case CommonImageVideoEventData.IMAGE_TYPE:
                    Picasso.get()
                            .load(Constants.EVENTS_GALLERY_IMAGE_URI+
                                    mcommonArrayList.get(position).getImage_url())
                            .placeholder(R.drawable.camera_icon)
                            .into(holder.image_view);
                    holder.image_description.setText(mcommonArrayList.get(position).getImage_description());

                    holder.image_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialoge(position);
                        }
                    });

                    break;

                case CommonImageVideoEventData.VIDEO_TYPE:
                    String youTubeUrl = mcommonArrayList.get(position).getVideo_url();
                    String videoDescription = mcommonArrayList.get(position).getVideo_description();

                    String[] splited = youTubeUrl.split("//",2);
                    String part1 = splited[0];
                    String part2 = splited[1];

                    String[] finalyoutubeUrl = part2.split("/",2);
                    String fina1 = finalyoutubeUrl[0];
                    final1 = finalyoutubeUrl[1];

                    concatUrl = "https://www.youtube.com/watch?v="+final1+"&feature="+fina1;
                    final Uri uri = Uri.parse(concatUrl);
                    concatUrl = uri.getQueryParameter("v");
                    final String url = "http://img.youtube.com/vi/" + concatUrl +"/0.jpg";
                    if(url != null) {
                        holder.play_image_view.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.camera_icon)
                                .into(holder.thumbnail_image_view);
                    }

                    holder.video_description.setText(mcommonArrayList.get(position).getVideo_description());
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (HelperClass.isNetworkAvailable(context)) {
                                isNetworkConnected = true;
                            }else {
                                isNetworkConnected = false;
                            }
                            if (isNetworkConnected) {
                                    Intent intent = new Intent(context, YouTubeActivity.class);
                                    intent.putExtra("video_url",final1);
                                    context.startActivity(intent);

                                }else {
                                Toast.makeText(context, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                }

                            }




                    });
            }
        }
    }

    private void openDialoge(int position) {
        if (context != null) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
            LayoutInflater inflater = activity.getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.open_image_dialoge, null);
            ImageView imageClose = (ImageView) alertLayout.findViewById(R.id.imgClose);
            ViewPager myPager = (ViewPager)alertLayout. findViewById(R.id.notifi_viewpager);
            alertbox.setView(alertLayout);
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(activity,context,mcommonArrayList);
            myPager.setAdapter(imagePagerAdapter);

            final int   dotsCount = imagePagerAdapter.getCount();
            final View[]  dots = new View[dotsCount];
            LinearLayout viewPagerCountDots = (LinearLayout) alertLayout.findViewById(R.id.viewPagerCountDots);

            for (int i=0;i<dots.length;i++) {
                dots[i] = new View(context);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dots[i].setBackground(context.getDrawable(R.drawable.default_dot));
                }else {
                    dots[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.default_dot));
                }
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));
                dots[i].setLayoutParams(lp);
                viewPagerCountDots.addView(dots[i]);
            }
            myPager.setCurrentItem(position);
            dots[position].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selected_dot));
            myPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int i = 0; i < dots.length; i++){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dots[i].setBackground(context.getDrawable(R.drawable.default_dot));
                        }else
                        {
                            dots[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.default_dot));
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dots[position].setBackground(context.getDrawable(R.drawable.selected_dot));
                    }else {
                        dots[position].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selected_dot));
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

            });
            final AlertDialog alert = alertbox.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            alert.show();

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return mcommonArrayList.size();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (!isConnected) {
           // tSnackbar.show();
        }else {

            /*if (tSnackbar.isShown()){
                tSnackbar.dismiss();
            }*/

        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        TextView image_description;

        LinearLayout linearLayout;
        CardView card_view;
        ImageView thumbnail_image_view;
        ImageView play_image_view;
        TextView video_description;

        public ViewHolder(View itemView) {
            super(itemView);

            image_view = (ImageView) itemView.findViewById(R.id.image_view);
            image_description = (TextView) itemView.findViewById(R.id.image_description);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.video_Linearlayout);
            thumbnail_image_view = (ImageView)itemView.findViewById(R.id.thumbnail_image_view);
            play_image_view = (ImageView)itemView.findViewById(R.id.play_button);
            video_description = (TextView) itemView.findViewById(R.id.video_description);
        }
    }
}
