package com.nickteck.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 6/21/2018.
 */

public class AnnoncementDetails {

    private String Status_code;
    private String parent_id;
    private ArrayList<CommonAnnounacementDetails> common_announcement;
    private ArrayList<SpecialAnnouncementDetails> special_announcement;

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public ArrayList<CommonAnnounacementDetails> getCommon_announcement() {
        return common_announcement;
    }

    public void setCommon_announcement(ArrayList<CommonAnnounacementDetails> common_announcement) {
        this.common_announcement = common_announcement;
    }

    public ArrayList<SpecialAnnouncementDetails> getSpecial_announcement() {
        return special_announcement;
    }

    public void setSpecial_announcement(ArrayList<SpecialAnnouncementDetails> special_announcement) {
        this.special_announcement = special_announcement;
    }


    public static class CommonAnnounacementDetails {
        private String title;
        private String message;
        private String date;

        public CommonAnnounacementDetails(String title, String message, String date) {
            this.title = title;
            this.message = message;
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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


    public static   class SpecialAnnouncementDetails {

        private String classe;
        private String section;
        private String title;
        private String message;
        private String teacher_name;
        private String date;

        public SpecialAnnouncementDetails(String classe, String section, String title, String message,
                                          String teacher_name, String date) {
            this.classe = classe;
            this.section = section;
            this.title = title;
            this.message = message;
            this.teacher_name = teacher_name;
            this.date = date;
        }

        public String getClasse() {
            return classe;
        }

        public void setClasse(String classe) {
            this.classe = classe;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
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
            jsonObject.put("parent_id",parent_id);
            JSONArray commonAnnouncementArray = new JSONArray();
            if(common_announcement.size()>0){

                for(int i=0;i<common_announcement.size(); i++){
                    AnnoncementDetails.CommonAnnounacementDetails details = common_announcement.get(i);
                    JSONObject common_annoncement = new JSONObject();
                    common_annoncement.put("title",details.getTitle());
                    common_annoncement.put("message",details.getMessage());
                    common_annoncement.put("date",details.getDate());
                    commonAnnouncementArray.put(common_annoncement);
                }
                jsonObject.put("common_announcement",commonAnnouncementArray);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject toJSONS(){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray specificAnnouncement = new JSONArray();
            if(special_announcement.size()>0){
                for(int i=0;i<special_announcement.size(); i++){
                    AnnoncementDetails.SpecialAnnouncementDetails specialAnnouncementDetails = special_announcement.get(i);
                    JSONObject common_annoncement = new JSONObject();
                    common_annoncement.put("title",specialAnnouncementDetails.getTitle());
                    common_annoncement.put("message",specialAnnouncementDetails.getMessage());
                    common_annoncement.put("teacher_name",specialAnnouncementDetails.getTeacher_name());
                    common_annoncement.put("date",specialAnnouncementDetails.getDate());
                    common_annoncement.put("classe",specialAnnouncementDetails.getClasse());
                    common_annoncement.put("section",specialAnnouncementDetails.getSection());
                }
                jsonObject.put("special_announcement",specificAnnouncement);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;


    }

}
