package com.nickteck.schoolapp.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.nickteck.schoolapp.network.ConnectivityReceiver;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 7/3/2018.
 */

public class LoadImage extends AsyncTask<String,Void,Bitmap> {

    CircleImageView circleImageView ;
    Context context;
    public LoadImage(CircleImageView circleImageView, Context context){
        this.circleImageView = circleImageView;
        this.context = context;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {

        String url = strings[0];
        Bitmap bitmap = null;
        boolean isNetworkConnected = ConnectivityReceiver.isConnected();
        try {
            if (isNetworkConnected) {
                bitmap = Picasso.get().load(url).get();
                return bitmap;
            }else {
                bitmap = Picasso.get()
                        .load(url)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .get();

                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap aVoid) {

        super.onPostExecute(aVoid);

        circleImageView.setImageBitmap(aVoid);
    }
}
