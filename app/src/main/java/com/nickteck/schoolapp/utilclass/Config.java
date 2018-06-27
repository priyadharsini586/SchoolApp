package com.nickteck.schoolapp.utilclass;

/**
 * Created by admin on 6/27/2018.
 */

public class Config  {

    public static final String DEVELOPER_KEY = "AIzaSyBrqMGci99-EOOCzBd9kFRGF10KZusORYY";
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
