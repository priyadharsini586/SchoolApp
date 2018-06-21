package com.nickteck.schoolapp.model;

import android.support.annotation.NonNull;

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
   private ArrayList<students_details>students_details = new ArrayList<>();

    public ArrayList<AboutMyChildDetails.students_details> getStudents_details() {
        return students_details;
    }

    public void setStudents_details(ArrayList<AboutMyChildDetails.students_details> students_details) {
        this.students_details = students_details;
    }

    public String getStatus_code() {
        return Status_code;
    }

    private void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getStudent_id() {
        return student_id;
    }

    private void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    private String getStudent_name() {
        return student_name;
    }

    private void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    private ArrayList<AboutMyChildDetails.student_notes> getStudent_notes() {
        return student_notes;
    }

    private void setStudent_notes(ArrayList<AboutMyChildDetails.student_notes> student_notes) {
        this.student_notes = student_notes;
    }

    private class student_notes{
        private String teacher_id,teacher_name,message,date;

        private String getTeacher_id() {
            return teacher_id;
        }

        private void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        private String getTeacher_name() {
            return teacher_name;
        }

        private void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        private String getMessage() {
            return message;
        }

        private void setMessage(String message) {
            this.message = message;
        }

        private String getDate() {
            return date;
        }

        private void setDate(String date) {
            this.date = date;
        }
    }

    private class students_details{
        String student_id,student_name;
        ArrayList<student_notes>student_notes = new ArrayList<>();

        public ArrayList<AboutMyChildDetails.student_notes> getStudent_notes() {
            return student_notes;
        }

        public void setStudent_notes(ArrayList<AboutMyChildDetails.student_notes> student_notes) {
            this.student_notes = student_notes;
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

    public JSONObject toAllChildJSON(){
        JSONObject jsonObject = new JSONObject();
        try {

            JSONArray studentDetailsArray = new JSONArray();
            for (int i = 0 ; i < students_details.size(); i++){
                AboutMyChildDetails.students_details studentDetails = students_details.get(i);
                JSONObject studObject = new JSONObject();
                studObject.put("student_id",studentDetails.getStudent_id());
                studObject.put("student_name",studentDetails.getStudent_name());
                JSONArray notesArray = new JSONArray();
                for (int j = 0 ; j < studentDetails.getStudent_notes().size() ; j ++){
                    AboutMyChildDetails.student_notes student_notes = studentDetails.getStudent_notes().get(j);
                    JSONObject notes = new JSONObject();
                    notes.put("teacher_id",student_notes.getTeacher_id());
                    notes.put("teacher_name",student_notes.getTeacher_name());
                    notes.put("message",student_notes.getMessage());
                    notes.put("date",student_notes.getDate());
                    notesArray.put(notes);
                }
                studObject.put("student_notes",notesArray);
                studentDetailsArray.put(studObject);
            }
            jsonObject.put("students_details",studentDetailsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
