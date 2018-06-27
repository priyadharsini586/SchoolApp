package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.adapter.CommonAnnouncementAdapter;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.model.AboutMyChildDetails;
import com.nickteck.schoolapp.model.AnnoncementDetails;
import com.nickteck.schoolapp.model.ParentDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonAnnouncmentFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener,
        OnBackPressedListener {
    View view;
    private RecyclerView recyclerview;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;
    DataBaseHandler dataBaseHandler;
    ApiInterface apiInterface;
    private ArrayList<AnnoncementDetails.CommonAnnounacementDetails> common_Announancement_ArrayList;
    CommonAnnouncementAdapter announcementAdapter;
    TextView txtNoDataTextview;
    private ProgressBar progress_bar;


    public CommonAnnouncmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_over_all_announement, container, false);

        MyApplication.getInstance().setConnectivityListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if ((CommonFragmentActivity)getActivity() != null) {
            ((CommonFragmentActivity) getActivity()).setOnBackPressedListener(this);
        }
        init();

        return view;
    }

    private void init() {
        dataBaseHandler = new DataBaseHandler(getActivity());
        recyclerview = (RecyclerView) view.findViewById(R.id.common_announacemnt_recyclerview);
        txtNoDataTextview = (TextView) view.findViewById(R.id.txtNoDataTextview);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress);
        progress_bar.setVisibility(View.INVISIBLE);
        txtNoDataTextview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (!isConnected) {

            tSnackbar.show();
        }else {
            if (tSnackbar.isShown()){
                tSnackbar.dismiss();
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
            readStatus();
            getDataFromServer();
        }else {
            setViewFromDb();
            }
        }

    private void getDataFromServer() {
        if (isNetworkConnected){
            progress_bar.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("parent_id", dataBaseHandler.getParentId());
            }catch (JSONException e){
                e.printStackTrace();
            }

            Call<AnnoncementDetails> annoncementDetails = apiInterface.getAnnoncementDetails(jsonObject);
            annoncementDetails.enqueue(new Callback<AnnoncementDetails>() {

                @Override
                public void onResponse(Call<AnnoncementDetails> call, Response<AnnoncementDetails> response) {
                    if (response.isSuccessful()){
                        AnnoncementDetails details = response.body();
                        if (details.getStatus_code() != null) {
                            if (details.getStatus_code().equals(Constants.SUCESS)) {
                                    JSONObject commonAnnoncementobject =  details.toJSON();
                                    JSONObject specificAnnouncement =  details.toJSONS();

                                    dataBaseHandler.dropCommonAnnounacementDetails();
                                    dataBaseHandler.insertCommonAnnouncementDetails(details.getParent_id(),commonAnnoncementobject.toString(),specificAnnouncement.toString());
                                    progress_bar.setVisibility(View.INVISIBLE);
                                    setIntoView();
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<AnnoncementDetails> call, Throwable t) {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void setIntoView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.VISIBLE);
                String getCommonAnnouncementDetails = dataBaseHandler.getCommonAnnouncementDtails();
                try {
                    common_Announancement_ArrayList = new ArrayList<>();
                    JSONObject getParentObject = new JSONObject(getCommonAnnouncementDetails);
                    JSONArray getCommonAnnouncementDetail = getParentObject.getJSONArray("common_announcement");
                    for (int i = 0; i < getCommonAnnouncementDetail.length(); i++) {
                        JSONObject commonannounmentDetails = getCommonAnnouncementDetail.getJSONObject(i);
                        String title = commonannounmentDetails.getString("title");
                        String message = commonannounmentDetails.getString("message");
                        String date = commonannounmentDetails.getString("date");
                        AnnoncementDetails.CommonAnnounacementDetails details = new AnnoncementDetails.CommonAnnounacementDetails(title,message,date);
                        common_Announancement_ArrayList.add(details);

                    }
                    if(common_Announancement_ArrayList.size()>0){
                        announcementAdapter = new CommonAnnouncementAdapter(getActivity(),common_Announancement_ArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerview.setLayoutManager(mLayoutManager);
                        recyclerview.setAdapter(announcementAdapter);
                        progress_bar.setVisibility(View.INVISIBLE);

                    }else {
                        txtNoDataTextview.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Common Announcement is Empty", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.e("TAG", "dataexixts: "+e );

                }

            }
        });
    }

    private void setViewFromDb() {
        setIntoView();


    }

    @Override
    public void onBackPressed() {

        ((CommonFragmentActivity)getActivity()).finish();
    }


    public void readStatus(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("announcment_type", "cmn");
                jsonObject.put("parent_id",dataBaseHandler.getParentId());
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<AboutMyChildDetails> aboutMyChildDetailsCall = apiInterface.readAnnoncementStatus(jsonObject);
            aboutMyChildDetailsCall.enqueue(new Callback<AboutMyChildDetails>() {
                @Override
                public void onResponse(Call<AboutMyChildDetails> call, Response<AboutMyChildDetails> response) {
                    if (response.isSuccessful()){
                        AboutMyChildDetails aboutMyChildDetails = AboutMyChildDetails.getInstance();
                        aboutMyChildDetails.setCommounAnnouncementCount(0);

                    }
                }

                @Override
                public void onFailure(Call<AboutMyChildDetails> call, Throwable t) {

                }
            });

        }
    }
}

