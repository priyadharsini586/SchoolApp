package com.nickteck.schoolapp.custom_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.fragment.AboutChildFragment;
import com.nickteck.schoolapp.model.InfoWindowData;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);


        CircleImageView img = view.findViewById(R.id.imgDriverImage);

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtPhoneNum = view.findViewById(R.id.txtPhoneNum);

        txtTitle.setText(marker.getTitle());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
//        new LoadImage(img,context).execute(infoWindowData.getImage());
        if (infoWindowData.getImage() != null){
            new LoadImage(img,context).execute(infoWindowData.getImage());
        }

        txtName.setText(infoWindowData.getName());
        txtPhoneNum.setText(infoWindowData.getPhoneNum());


        return view;
    }


}