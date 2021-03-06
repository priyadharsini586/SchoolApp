package com.nickteck.schoolapp.service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.activity.DashboardActivity;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.fragment.DashboardFragment;
import com.nickteck.schoolapp.interfaces.OnGetDataFromBusApp;
import com.nickteck.schoolapp.model.ParentDetails;
import com.nickteck.schoolapp.utilclass.Config;
import com.nickteck.schoolapp.utilclass.Constants;
import com.nickteck.schoolapp.utilclass.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 6/29/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static OnGetDataFromBusApp onGetDataFromBusApp ;
    private ArrayList<String> studentArrayListId = new ArrayList<>();

    private NotificationUtils notificationUtils;

    public MyFirebaseMessagingService(){

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if(remoteMessage.getFrom().equals("/topics/latlan")){
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



        }else if(remoteMessage.getFrom().equals("/topics/studentNote")) {
          //  {"messge":"hi","student_id":"1, 2, 4, 5"}
            // Check if message contains a data payload.


            if (remoteMessage.getData().size() > 0) {

                Map<String, String> getData = remoteMessage.getData();
                String strMsg = getData.toString();

                try {
                    JSONObject jsonObj = new JSONObject(strMsg);
                    String string = jsonObj.getString("student_id");
                    getStudentIdList();
                    List<String> myList = new ArrayList<String>(Arrays.asList(string.split(",")));
                    boolean isStudentId = false;
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(getApplicationContext());
                    String parent_id = dataBaseHandler.getParentId();
                    Log.e("parent id",parent_id);
                    for (int i =0 ; i < myList.size() ; i ++){
                        String studentID = myList.get(i).trim();
                        for (int j =0 ; j < studentArrayListId.size(); j++){
                            String studentId = studentArrayListId.get(j).trim();
                            if (studentID.equals(studentId)){
                                isStudentId = true;
                            }
                        }

                    }
                    if (isStudentId) {
                        handleNotification(strMsg);
                    }else {
                        Log.e(TAG, "From: " + remoteMessage.getFrom());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {

                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
            }

           /* // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }*/

        }




    }



    private void handleNotification(String message) {
        Intent resultIntent = new Intent(getApplicationContext(), CommonFragmentActivity.class);
        resultIntent.putExtra("from",Constants.ABOUT_CHILD_FRAGMENT);
        resultIntent.putExtra("childId","-1");
        showNotificationMessage(getApplicationContext(), "notify", message, "12.00 Pm", resultIntent);

//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message





    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            final String title = data.getString("title");
            final String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            String from = data.getString("from");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);
            Log.e(TAG, "from: " + from);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("title", title);
                pushNotification.putExtra("from",from);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                if (from.equals("tips")) {
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                }
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                resultIntent.putExtra(Constants.fromMore,Constants.DASHBOARD_FRAGMENT);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("from",from);
//                startActivity(intent);
               /* Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                resultIntent.putExtra("message", message);*/

                // check for image attachment
                if (from.equals("tips")) {
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }






    public static void onGetDataFromBusAppListener(OnGetDataFromBusApp onGetDataFromBusApp) {
        MyFirebaseMessagingService.onGetDataFromBusApp = onGetDataFromBusApp;
    }

    public void getStudentIdList (){
        DataBaseHandler dataBaseHandler = new DataBaseHandler(getApplicationContext());
        if (dataBaseHandler.ifParentChildisExists()) {
            String getParentDetails = dataBaseHandler.getParentChildDetails();
            try {
                JSONObject getParentObject = new JSONObject(getParentDetails);
                JSONArray getStudentArray = getParentObject.getJSONArray("student_details");
                studentArrayListId = new ArrayList<>();
                for (int i = 0; i < getStudentArray.length(); i++) {
                    JSONObject studObject = getStudentArray.getJSONObject(i);
                    if (studObject.has("student_id")) {
                        String studId = studObject.getString("student_id");
                        studentArrayListId.add(studId);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
