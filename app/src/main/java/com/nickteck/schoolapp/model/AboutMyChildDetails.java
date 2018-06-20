package com.nickteck.schoolapp.model;

import com.nickteck.schoolapp.utilclass.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 6/20/2018.
 */

public class AboutMyChildDetails {
   private String Status_code,student_id,student_name;
   private ArrayList<student_notes>student_notes = new ArrayList<>();
    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
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

    public ArrayList<AboutMyChildDetails.student_notes> getStudent_notes() {
        return student_notes;
    }

    public void setStudent_notes(ArrayList<AboutMyChildDetails.student_notes> student_notes) {
        this.student_notes = student_notes;
    }

    public class student_notes{
        private String teacher_id,teacher_name,message,date;

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("student_id",student_id);
            jsonObject.put("student_name",student_name);
            JSONArray studNotesArray = new JSONArray();
            for (int i = 0 ; i < student_notes.size(); i++){
                AboutMyChildDetails.student_notes studentDetails = student_notes.get(i);
                JSONObject studObject = new JSONObject();
                studObject.put("teacher_id",studentDetails.getTeacher_id());
                studObject.put("teacher_name",studentDetails.getTeacher_name());
                studObject.put("message",studentDetails.getMessage());
                studObject.put("date",studentDetails.getDate());
                studNotesArray.put(studObject);
            }
            jsonObject.put("student_notes",studNotesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
