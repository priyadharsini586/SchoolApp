package com.nickteck.schoolapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.model.AboutMyChildDetails;
import com.nickteck.schoolapp.model.AnnoncementDetails;
import com.nickteck.schoolapp.model.ParentDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonAnnouncmentFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener{
    View view;
    private RecyclerView recyclerview;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;
    DataBaseHandler dataBaseHandler;
    ApiInterface apiInterface;


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


        init();

        return view;
    }

    private void init() {
        dataBaseHandler = new DataBaseHandler(getActivity());
        recyclerview = (RecyclerView) view.findViewById(R.id.common_announacemnt_recyclerview);

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
            getDataFromServer();
        }else {
            setViewFromDb();
            }
        }

    private void getDataFromServer() {
        if (isNetworkConnected){
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

    private void setViewFromDb() {


    }

}

