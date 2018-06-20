package com.nickteck.schoolapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutChildFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {
    View mainView;
    LinearLayout ldtMainAboutView;
    View view = null;
    DataBaseHandler dataBaseHandler;
    TextView txtStudentName, txtTeacherName;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_about_child, container, false);
        dataBaseHandler = new DataBaseHandler(getActivity());
        MyApplication.getInstance().setConnectivityListener(this);

        initView();


        return mainView;
    }

    private void initView() {

        ldtMainAboutView = (LinearLayout) mainView.findViewById(R.id.ldtMainAboutView);



    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (!isConnected) {
            tSnackbar.show();
        }else
        {
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }
    }
    public void getDataFromServer(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();


        }else {
            tSnackbar.show();
            setViewFromDb();
        }

    }

    public void setViewFromDb(){
        String getParentDetails = dataBaseHandler.getParentChildDetails();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tSnackbar = HelperClass.showTopSnackBar(view, "Network not connected");
        if (HelperClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        } else {
            isNetworkConnected = false;
            tSnackbar.show();
        }

        if (isNetworkConnected)
            getDataFromServer();
        else
            setViewFromDb();
    }
}
