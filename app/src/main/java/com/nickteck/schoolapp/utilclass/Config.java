package com.nickteck.schoolapp.utilclass;

/**
 * Created by admin on 6/27/2018.
 */

public class Config  {

    public static final String DEVELOPER_KEY = "AIzaSyBrqMGci99-EOOCzBd9kFRGF10KZusORYY";

    public static final String TOPIC_GLOBAL = "studentNote";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    private static Config ourInstance = new Config();
    private int millSec = 0;

    public static Config getInstance() {
        return ourInstance;
    }
    public int getMillSec() {
        return millSec;
    }

    public void setMillSec(int millSec) {
        this.millSec = millSec;
    }
}
