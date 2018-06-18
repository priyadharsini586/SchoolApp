package com.nickteck.schoolapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public class student_details{
        private String student_id,student_name,student_std,student_section,student_photo;

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
                studObject.put("student_photo",studentDetails.getStudent_photo());
                studentDetailArray.put(studObject);
            }
            jsonObject.put("student_details",studentDetailArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
