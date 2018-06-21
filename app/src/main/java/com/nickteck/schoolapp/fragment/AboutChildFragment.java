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
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.model.AboutMyChildDetails;
import com.nickteck.schoolapp.model.ParentDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutChildFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {
    View mainView;
    LinearLayout ldtMainAboutView;
    View view = null;
    DataBaseHandler dataBaseHandler;
    TextView txtStudentName, txtTeacherName;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;
    ApiInterface apiInterface;
    String childId;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_about_child, container, false);
        dataBaseHandler = new DataBaseHandler(getActivity());
        MyApplication.getInstance().setConnectivityListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
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
            try {
                jsonObject.put("student_id", childId);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<AboutMyChildDetails> aboutMyChildDetailsCall = apiInterface.getChildAboutDetails(jsonObject);
            aboutMyChildDetailsCall.enqueue(new Callback<AboutMyChildDetails>() {
                @Override
                public void onResponse(Call<AboutMyChildDetails> call, Response<AboutMyChildDetails> response) {
                    if (response.isSuccessful()) {
                        AboutMyChildDetails aboutMyChildDetails = response.body();
                        if (aboutMyChildDetails.getStatus_code() != null) {
                            if (aboutMyChildDetails.getStatus_code().equals(Constants.SUCESS)) {
                                String studentId = aboutMyChildDetails.getStudent_id();
                                JSONObject parentObject = aboutMyChildDetails.toJSON();
                                if (dataBaseHandler.ifStudentIdisExists(studentId)) {
                                    dataBaseHandler.dropChildAboutDetails(studentId);
                                    dataBaseHandler.insertChildAboutDetails(studentId,parentObject.toString());
                                }else {
                                    dataBaseHandler.insertChildAboutDetails(studentId,parentObject.toString());
                                }

                                setViewFromDb();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutMyChildDetails> call, Throwable t) {

                }
            });


        }else {
            tSnackbar.show();
            setViewFromDb();
        }

    }

    public void setViewFromDb(){
        if (dataBaseHandler.ifChildAboutDetailsisExists()){
            String getChildDetails = dataBaseHandler.getChildAboutDetails(childId);
            try {
                JSONObject getChildAboutObject = new JSONObject(getChildDetails);
                LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.about_child_row, null);
                TextView txtStudentName = v.findViewById(R.id.txtStudentName);
                txtStudentName.setText(getChildAboutObject.getString("student_name"));
                ldtMainAboutView.addView(v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

        if (isNetworkConnected) {
            if (!childId.equals("-1"))
                getDataFromServer();
            else {

            }

        }
        else {
            setViewFromDb();
        }
    }

    public void childId (String childId){
        this.childId = childId;
    }
}
