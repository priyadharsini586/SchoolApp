package com.nickteck.schoolapp.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;

import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.custom_view.CustomInfoWindowGoogleMap;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.interfaces.OnGetDataFromBusApp;
import com.nickteck.schoolapp.model.InfoWindowData;
import com.nickteck.schoolapp.service.MyFirebaseMessagingService;
import com.nickteck.schoolapp.utilclass.Config;
import com.nickteck.schoolapp.utilclass.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusTrackFragment extends Fragment implements OnMapReadyCallback,OnGetDataFromBusApp, OnBackPressedListener {

    private GoogleMap mMap;
    public View mainView;
    private static final String TAG = BusTrackFragment.class.getSimpleName();
    double currentLongitude = 0.0,currentLatitude = 0.0;
    MarkerOptions marker;
    String getParentDetails;
    DataBaseHandler dataBaseHandler;
    String driverId = null,driverName,driverPhoneNumber,driverPhoto;
    AlertDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_bus_track, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()

                .findFragmentById(R.id.map);
        dataBaseHandler = new DataBaseHandler(getActivity());
        getDriverId();
        mapFragment.getMapAsync(this);
        MyFirebaseMessagingService.onGetDataFromBusAppListener(this);

        Toolbar toolBarTitle = (Toolbar) getActivity().findViewById(R.id.toolBarTitle);
        TextView toolBarTextView = (TextView) toolBarTitle.findViewById(R.id.toolBarTextView);
        toolBarTextView.setText("Route Map");
        if ((CommonFragmentActivity)getActivity() != null) {
            ((CommonFragmentActivity) getActivity()).setOnBackPressedListener(this);
        }

        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Alert")
                .setMessage("Driver not in route")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        ((CommonFragmentActivity)getActivity()).finish();

                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return mainView;
    }

    private void getDriverId() {

        getParentDetails = dataBaseHandler.getParentChildDetails();
        try {
            JSONObject getParentObject = new JSONObject(getParentDetails);
            JSONArray getStudentArray = getParentObject.getJSONArray("student_details");
            for (int i =0 ; i < getStudentArray.length() ; i ++){
                JSONObject studObject = getStudentArray.getJSONObject(i);
                JSONArray getRouteArray = studObject.getJSONArray("bus_route_detils");
                for (int j =0 ; j < getRouteArray.length() ; j++){
                    JSONObject routeObject = getRouteArray.getJSONObject(j);
                    driverId = routeObject.getString("driver_id");
                    driverName = routeObject.getString("driver_name");
                    driverPhoneNumber = routeObject.getString("driver_phone");
                    driverPhoto = routeObject.getString("driver_photo");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }


    @Override
    public void onGetDataFromBusApp(String data) {
        try {
            JSONObject latLon = new JSONObject(data);
            if (latLon.has("message")) {
                JSONObject jsonObject = latLon.getJSONObject("message");
                String status = jsonObject.getString("status");
                if (status.equals("1") ) {
                    if (jsonObject.has("driver_id")) {
                        if (driverId.equals(jsonObject.getString("driver_id"))) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            if (jsonObject.has("Latitude")) {
                                currentLatitude = jsonObject.getDouble("Latitude");
                            }
                            if (jsonObject.has("Longitude")) {
                                currentLongitude = jsonObject.getDouble("Longitude");
                            }
                            if (currentLatitude != 0.0 && currentLongitude != 0.0 && getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LatLng currentpos = new LatLng(currentLatitude, currentLongitude);
                                        mMap.clear();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 22));
                                        marker = new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Bus Route     ");
                                        marker.icon(HelperClass.bitmapDescriptorFromVector(getActivity(), R.drawable.track_icon));

                                        InfoWindowData info = new InfoWindowData();
                                        info.setImage(Constants.DRIVER_IMAGE_URL + driverPhoto);
                                        info.setName(driverName);
                                        info.setPhoneNum(driverPhoneNumber);

                                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getActivity());
                                        mMap.setInfoWindowAdapter(customInfoWindow);

                                        Marker m = mMap.addMarker(marker);
                                        m.setTag(info);
                                        m.showInfoWindow();

                                    }
                                });
                            }
                        }
                    }
                }else {
                    if (jsonObject.has("driver_id")) {
                        if (driverId.equals(jsonObject.getString("driver_id"))) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mMap != null){
                                            mMap.clear();
                                        }
                                        dialog.show();
                                    }
                                });

                            }
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        ((CommonFragmentActivity)getActivity()).finish();
    }
}
