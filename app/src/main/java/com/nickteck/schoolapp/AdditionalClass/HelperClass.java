package com.nickteck.schoolapp.AdditionalClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.R;

import java.util.regex.Pattern;

/**
 * Created by admin on 6/13/2018.
 */

public class HelperClass {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    public static TSnackbar showTopSnackBar(View appCompatActivity, String content) {
        TSnackbar snackbar = TSnackbar.make(appCompatActivity, content, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#e50000"));
        snackbar.setMaxWidth(appCompatActivity.getWidth());
        snackbar.setDuration(3000);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        snackbar.show();
        return snackbar;
    }
    private void openCustomDialoge(View v, Activity activity) {
        // common custom alert dialoge
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_alert_dialoge);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
