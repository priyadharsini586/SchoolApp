package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
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

import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.adapter.CommonAnnouncementAdapter;
import com.nickteck.schoolapp.adapter.SpecificAnnouncementAdapter;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.model.AboutMyChildDetails;
import com.nickteck.schoolapp.model.AnnoncementDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;

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
public class SpecificAnnouncementFrgament extends Fragment implements OnBackPressedListener, NetworkChangeReceiver.ConnectivityReceiverListener{

    DataBaseHandler dataBaseHandler;
    View view;
    private RecyclerView recyclerview;
    private ArrayList<AnnoncementDetails.SpecialAnnouncementDetails> specific_Announancement_ArrayList;
    SpecificAnnouncementAdapter adapter;
    private TextView NoDataSpecificList;
    private ProgressBar progress_bar;
    private boolean isVisible = false,isNetworkConnected = false;
    private String childId;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_specific_announcement_frgament, container, false);
        dataBaseHandler = new DataBaseHandler(getActivity());
        if ((CommonFragmentActivity)getActivity() != null) {
            ((CommonFragmentActivity) getActivity()).setOnBackPressedListener(this);
        }
        MyApplication.getInstance().setConnectivityListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        init();
        setIntoView();

        return view;
    }



    private void init() {

        recyclerview = (RecyclerView) view.findViewById(R.id.specific_announacemnt_recyclerview);
        NoDataSpecificList = (TextView) view.findViewById(R.id.NoDataSpecificList);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress);
        progress_bar.setVisibility(View.INVISIBLE);
        NoDataSpecificList.setVisibility(View.INVISIBLE);

        if (HelperClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            readStatus();

        } else {
            isNetworkConnected = false;

        }
    }

    public void setIntoView() {
        progress_bar.setVisibility(View.VISIBLE);

        String getSpecificAnnouncementDetails = dataBaseHandler.getSpecificAnnouncementDtails();
        try {

            JSONObject getParentObject = new JSONObject(getSpecificAnnouncementDetails);

            if(getParentObject.has("special_announcement")){
                specific_Announancement_ArrayList = new ArrayList<>();
                JSONArray getSpecificAnnouncementDetail = getParentObject.getJSONArray("special_announcement");
                if(getSpecificAnnouncementDetail.length()>0){
                    for (int i = 0; i < getSpecificAnnouncementDetail.length(); i++) {
                        JSONObject commonannounmentDetails = getSpecificAnnouncementDetail.getJSONObject(i);
                        if (childId.equals(commonannounmentDetails.getString("student_id"))) {
                            String title = commonannounmentDetails.getString("title");
                            String message = commonannounmentDetails.getString("message");
                            String date = commonannounmentDetails.getString("date");
                            String teacherName = commonannounmentDetails.getString("teacher_name");
                            String classe = commonannounmentDetails.getString("classe");
                            String section = commonannounmentDetails.getString("section");
                            String studentId = commonannounmentDetails.getString("student_id");


                            AnnoncementDetails.SpecialAnnouncementDetails details = new AnnoncementDetails.SpecialAnnouncementDetails(classe, section, title, message, teacherName, date, studentId);
                            specific_Announancement_ArrayList.add(details);
                        }else if (childId.equals("-1")){
                            String title = commonannounmentDetails.getString("title");
                            String message = commonannounmentDetails.getString("message");
                            String date = commonannounmentDetails.getString("date");
                            String teacherName = commonannounmentDetails.getString("teacher_name");
                            String classe = commonannounmentDetails.getString("classe");
                            String section = commonannounmentDetails.getString("section");
                            String studentId = commonannounmentDetails.getString("student_id");


                            AnnoncementDetails.SpecialAnnouncementDetails details = new AnnoncementDetails.SpecialAnnouncementDetails(classe, section, title, message, teacherName, date, studentId);
                            specific_Announancement_ArrayList.add(details);
                        }

                    }
                }

                if(specific_Announancement_ArrayList.size()>0){
                    adapter = new SpecificAnnouncementAdapter(getActivity(),specific_Announancement_ArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerview.setLayoutManager(mLayoutManager);
                    recyclerview.setAdapter(adapter);
                    progress_bar.setVisibility(View.INVISIBLE);

                }else {
                    NoDataSpecificList.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.INVISIBLE);

                }

            }else {
                NoDataSpecificList.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.INVISIBLE);
            }






        }catch (Exception e){
            Log.e("TAG", "dataexixts: "+e );


        }



    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isVisible = isVisibleToUser;

        // Make sure that fragment is currently visible
        if (!isVisible && isResumed()) {
            // Call code when Fragment not visible
        } else if (isVisible && isResumed()) {
            // Call code when Fragment becomes visible.
            setIntoView();
        }
    }

    @Override
    public void onBackPressed() {
        ((CommonFragmentActivity)getActivity()).finish();
    }

    public void childId(String childId){
        this.childId = childId;
    }


    public void readStatus(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("announcment_type", "spl");
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
                        HashMap<Object,Object> getChildCount = new HashMap<>();
                        aboutMyChildDetails.setAnnouncementCount(getChildCount);

                    }
                }

                @Override
                public void onFailure(Call<AboutMyChildDetails> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;

    }
}
