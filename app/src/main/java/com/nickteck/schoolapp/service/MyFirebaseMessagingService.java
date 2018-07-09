package com.nickteck.schoolapp.service;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nickteck.schoolapp.interfaces.OnGetDataFromBusApp;

import java.util.Collection;
import java.util.Map;

/**
 * Created by admin on 6/29/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static OnGetDataFromBusApp onGetDataFromBusApp ;

    public MyFirebaseMessagingService(){

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> getData = remoteMessage.getData();
            String strMsg = getData.toString();
            if (onGetDataFromBusApp != null) {
                onGetDataFromBusApp.onGetDataFromBusApp(strMsg);
            }
            Log.e(TAG, "Data Payload: " + strMsg);
        }
    }

    public static void onGetDataFromBusAppListener(OnGetDataFromBusApp onGetDataFromBusApp) {
        MyFirebaseMessagingService.onGetDataFromBusApp = onGetDataFromBusApp;
    }
}
