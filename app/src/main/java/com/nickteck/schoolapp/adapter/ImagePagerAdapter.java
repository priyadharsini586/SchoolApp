package com.nickteck.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.model.CommonImageVideoEventData;
import com.nickteck.schoolapp.utilclass.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 6/27/2018.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CommonImageVideoEventData> commonImageVideoEventData;

    public ImagePagerAdapter(Activity activity, Context context, ArrayList<CommonImageVideoEventData> commonImageVideoEventData) {
        this.activity = activity;
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.commonImageVideoEventData = commonImageVideoEventData;
    }

    @Override
    public int getCount() {
        return commonImageVideoEventData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.image_view_pager_adapter, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.vip_image_page_adapter);
        Picasso.get()
                .load(Constants.EVENTS_GALLERY_IMAGE_URI+commonImageVideoEventData.get(position).getImage_url())
                .placeholder(R.drawable.camera_icon)
                .into(imageView);


        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
