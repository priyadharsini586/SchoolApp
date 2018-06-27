package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.adapter.ImageCardViewAdapter;
import com.nickteck.schoolapp.model.CommonImageVideoEventData;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvenImageCardViewFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener{

    View view;
    ArrayList<CommonImageVideoEventData> commonImageVideoEventDataArrayList = new ArrayList<>();
    ArrayList<String> VideoArrayList = new ArrayList<>();
    private String seletecTitleData;
    TextView image_common_heaader;
    private RecyclerView image_recyclerview;
    private RecyclerView video_recyclerview;
    ImageCardViewAdapter cardViewAdapter,videoAdapter;
    ArrayList<String> commonArrayList;

    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;




    public EvenImageCardViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_even_image_card_view, container, false);
        Bundle bundle =getArguments();
        commonImageVideoEventDataArrayList = (ArrayList<CommonImageVideoEventData>) bundle.getSerializable("commonArrayList");
       // VideoArrayList = bundle.getStringArrayList("videoArrayList");
        seletecTitleData = bundle.getString("selectedTitle");

        init();
        setData();

        return view;

    }

    private void init() {
        commonArrayList = new ArrayList<>();
        image_common_heaader = (TextView) view.findViewById(R.id.image_common_heaader);
        image_recyclerview = (RecyclerView) view.findViewById(R.id.image_recyclerview);
        video_recyclerview = (RecyclerView) view.findViewById(R.id.video_recyclerview);

        image_common_heaader.setText(seletecTitleData);

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

       /* if (isNetworkConnected) {
            readStatus();
            getDataFromServer();
        }else {
            setViewFromDb();
        }*/

    }

    private void setData() {
        if(commonImageVideoEventDataArrayList.size()>0){
            ArrayList<CommonImageVideoEventData> ImageData = new ArrayList<>();
            ArrayList<CommonImageVideoEventData> videoData = new ArrayList<>();
            for (int j = 0 ; j < commonImageVideoEventDataArrayList.size() ; j ++){
                CommonImageVideoEventData commonImageVideoEventData = commonImageVideoEventDataArrayList.get(j);
                if (commonImageVideoEventData.getType() == 0){
                    ImageData.add(commonImageVideoEventData);
                }else if (commonImageVideoEventData.getType() == 1){
                    videoData.add(commonImageVideoEventData);
                }
            }
            cardViewAdapter = new ImageCardViewAdapter(getActivity(),ImageData,getActivity());
            videoAdapter = new ImageCardViewAdapter(getActivity(),videoData,getActivity());


            RecyclerView.LayoutManager mvideoLayoutManager = new GridLayoutManager(getActivity(), 2);
            video_recyclerview.setLayoutManager(mvideoLayoutManager);
            video_recyclerview.setItemAnimator(new DefaultItemAnimator());
            video_recyclerview.setLayoutManager(mvideoLayoutManager);
            video_recyclerview.setAdapter(videoAdapter);

            for(int i=0;i<commonImageVideoEventDataArrayList.size();i++){
               int type = commonImageVideoEventDataArrayList.get(i).getType();
                if(type == 0){
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                    image_recyclerview.setLayoutManager(mLayoutManager);
                    image_recyclerview.setItemAnimator(new DefaultItemAnimator());
                    image_recyclerview.setLayoutManager(mLayoutManager);
                    image_recyclerview.setAdapter(cardViewAdapter);
                }
            }
        } else {
            Toast.makeText(getActivity(), "Gallery List is Empty", Toast.LENGTH_SHORT).show();
        }

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
}
