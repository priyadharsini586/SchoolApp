package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.nickteck.schoolapp.adapter.EventAdapter;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.model.ShowEvent;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {

    View view;
    private RecyclerView events_recyclerview;
    private TextView txtNoDataTextview;
    private ProgressBar progress_bar;
    ApiInterface apiInterface;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;
    DataBaseHandler dataBaseHandler;

    Toolbar toolBarTitle;
    private String img_url;
    private String video_url;
    private ArrayList<ShowEvent> finalShowEventsArrayList;
    private ArrayList<ShowEvent.EventDetails> showEventArrayList;
    private ArrayList<ShowEvent.ImageDetails> showEventsImageArrayList;
    private ArrayList<ShowEvent.VideoDetails> showEventsVideoArrayList;
    private String title;
    private String content;
    private String held_on;
    private String date;
    private ShowEvent.ImageDetails imageDetails;
    private ShowEvent.VideoDetails videoDetails;
    EventAdapter eventAdapter;
    private String img_description;
    private String video_description;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_events, container, false);
        MyApplication.getInstance().setConnectivityListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        init();
        return view;
    }

    private void init() {
        dataBaseHandler = new DataBaseHandler(getActivity());
        events_recyclerview = (RecyclerView) view.findViewById(R.id.events_recyclerview);
        txtNoDataTextview = (TextView) view.findViewById(R.id.txtNoDataTextview);
        txtNoDataTextview = (TextView) view.findViewById(R.id.txtNoDataTextview);
        toolBarTitle = (Toolbar) getActivity().findViewById(R.id.toolBarTitle);
        TextView toolBarTextView = (TextView) toolBarTitle.findViewById(R.id.toolBarTextView);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress);
        progress_bar.setVisibility(View.INVISIBLE);
        txtNoDataTextview.setVisibility(View.INVISIBLE);
        toolBarTextView.setText("EVENTS");


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
            getDataFromServer();
        }else {
            setViewFromDb();
        }

    }

    private void getDataFromServer() {

        if (isNetworkConnected) {
            progress_bar.setVisibility(View.VISIBLE);
            Call<ShowEvent> annoncementDetails = apiInterface.getEventDetails();
            annoncementDetails.enqueue(new Callback<ShowEvent>() {

                @Override
                public void onResponse(Call<ShowEvent> call, Response<ShowEvent> response) {
                    if (response.isSuccessful()) {
                        ShowEvent showEvent = response.body();
                        if (showEvent.getStatus_code() != null) {
                            if (showEvent.getStatus_code().equals(Constants.SUCESS)) {
                                JSONObject showEventsDetails =  showEvent.toJSON();

                                dataBaseHandler.dropShowEventsDetails();
                                dataBaseHandler.insertShowEvents(showEventsDetails.toString());
                                progress_bar.setVisibility(View.INVISIBLE);
                                setIntoView();



                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<ShowEvent> call, Throwable t) {

                }
            });


        }
    }

    private void setIntoView() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.VISIBLE);
                String getShowEventsDetails = dataBaseHandler.getShowEventsDetails();
                try{
                    finalShowEventsArrayList = new ArrayList<>();
                    showEventArrayList = new ArrayList<>();
                    JSONObject getParentObject = new JSONObject(getShowEventsDetails);
                    JSONArray getEventDetails = getParentObject.getJSONArray("event_details");
                    if(getEventDetails.length()>0){
                        for (int i = 0; i < getEventDetails.length(); i++) {
                            JSONObject eventDEtails = getEventDetails.getJSONObject(i);
                            title = eventDEtails.getString("title");
                            content = eventDEtails.getString("content");
                            held_on = eventDEtails.getString("held_on");
                            date = eventDEtails.getString("date");
                            JSONArray getEventImageDetails = eventDEtails.getJSONArray("image_details");
                            showEventsImageArrayList = new ArrayList<>();
                            if(getEventImageDetails.length()>0) {
                                for (int j = 0; j < getEventImageDetails.length(); j++) {
                                    JSONObject eventImageDetails = getEventImageDetails.getJSONObject(j);
                                    img_url = eventImageDetails.getString("img_url");
                                    img_description = eventImageDetails.getString("description");
                                    imageDetails = new ShowEvent.ImageDetails(img_url,img_description);
                                    showEventsImageArrayList.add(imageDetails);
                                }
                            }
                            JSONArray getEventVideoDetails = eventDEtails.getJSONArray("video_details");
                            if(getEventVideoDetails.length()>0){
                                showEventsVideoArrayList = new ArrayList<>();
                                for(int k=0; k<getEventVideoDetails.length(); k++){
                                    JSONObject eventVideoDetails = getEventVideoDetails.getJSONObject(k);
                                    video_url = eventVideoDetails.getString("video_url");
                                    video_description = eventVideoDetails.getString("description");
                                    videoDetails = new ShowEvent.VideoDetails(video_url,video_description);
                                    showEventsVideoArrayList.add(videoDetails);
                                }

                            }
                            ShowEvent.EventDetails eventDetails  = new ShowEvent.EventDetails(title,content,held_on,date,showEventsImageArrayList,showEventsVideoArrayList);
                            showEventArrayList.add(eventDetails);
                        }

                        if(showEventArrayList.size()>0){
                            eventAdapter = new EventAdapter(getActivity(),showEventArrayList,getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            events_recyclerview.setLayoutManager(mLayoutManager);
                            events_recyclerview.setAdapter(eventAdapter);
                            progress_bar.setVisibility(View.INVISIBLE);
                        }else {
                            txtNoDataTextview.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Events List is Empty", Toast.LENGTH_SHORT).show();
                        }


                    }else {
                        txtNoDataTextview.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Events List is Empty", Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e ){
                    Log.e("TAG", "dataexixts: "+e );
                }

            }

        });
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

    private void setViewFromDb() {
        setIntoView();
    }
}
