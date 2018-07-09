package com.nickteck.schoolapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.nickteck.schoolapp.AdditionalClass.DownloadImage;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.utilclass.Constants;
import com.nickteck.schoolapp.utilclass.UtilClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 6/18/2018.
 */

public class ParentDetails {
   private String Status_code,device_id,parent_id,parent_name;
   ArrayList<student_details>student_details  = new ArrayList<>();

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public static class student_details{
        private String student_id,student_name,student_std,student_section,student_photo;
        private ArrayList<bus_route_detils>bus_route_detils  = new ArrayList<>();
        public student_details(){

        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getStudent_std() {
            return student_std;
        }

        public void setStudent_std(String student_std) {
            this.student_std = student_std;
        }

        public String getStudent_section() {
            return student_section;
        }

        public void setStudent_section(String student_section) {
            this.student_section = student_section;
        }

        public String getStudent_photo() {
            return student_photo;
        }

        public void setStudent_photo(String student_photo) {
            this.student_photo = student_photo;
        }

        public ArrayList<ParentDetails.bus_route_detils> getBus_route_detils() {
            return bus_route_detils;
        }

        public void setBus_route_detils(ArrayList<ParentDetails.bus_route_detils> bus_route_detils) {
            this.bus_route_detils = bus_route_detils;
        }
    }

    private class bus_route_detils{
        private String route_name,driver_id,driver_name,driver_phone,driver_photo;

        public String getRoute_name() {
            return route_name;
        }

        public void setRoute_name(String route_name) {
            this.route_name = route_name;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getDriver_name() {
            return driver_name;
        }

        public void setDriver_name(String driver_name) {
            this.driver_name = driver_name;
        }

        public String getDriver_phone() {
            return driver_phone;
        }

        public void setDriver_phone(String driver_phone) {
            this.driver_phone = driver_phone;
        }

        public String getDriver_photo() {
            return driver_photo;
        }

        public void setDriver_photo(String driver_photo) {
            this.driver_photo = driver_photo;
        }
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parent_id",parent_id);
            jsonObject.put("parent_name",parent_name);
            JSONArray studentDetailArray = new JSONArray();
            for (int i = 0 ; i < student_details.size(); i++){
                ParentDetails.student_details studentDetails = student_details.get(i);
                JSONObject studObject = new JSONObject();
                studObject.put("student_id",studentDetails.getStudent_id());
                studObject.put("student_name",studentDetails.getStudent_name());
                studObject.put("student_std",studentDetails.getStudent_std());
                studObject.put("student_section",studentDetails.getStudent_section());
                studObject.put("student_photo",Constants.CHILD_IMAGE_URL+ studentDetails.getStudent_photo());
                JSONArray routeDetailsArray = new JSONArray();
                for (int j =0 ; j < studentDetails.getBus_route_detils().size() ; j++){
                    ParentDetails.bus_route_detils  bus_route_detils = studentDetails.getBus_route_detils().get(j);
                    JSONObject route = new JSONObject();
                    route.put("route_name",bus_route_detils.getRoute_name());
                    route.put("driver_id",bus_route_detils.getDriver_id());
                    route.put("driver_name",bus_route_detils.getDriver_name());
                    route.put("driver_phone",bus_route_detils.getDriver_phone());
                    route.put("driver_photo",bus_route_detils.getDriver_photo());
                    routeDetailsArray.put(route);
                }
                studObject.put("bus_route_detils",routeDetailsArray);
                studentDetailArray.put(studObject);
            }
            jsonObject.put("student_details",studentDetailArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



}
