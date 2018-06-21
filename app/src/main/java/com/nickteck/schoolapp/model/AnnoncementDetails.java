package com.nickteck.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 6/21/2018.
 */

public class AnnoncementDetails {

    private String Status_code;
    private String parent_id;
    private ArrayList<CommonAnnounacementDetails> common_announcement;
    @SerializedName("special_announcement")
   private ArrayList<special_announcement> special_announcement;

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

    public ArrayList<special_announcement> getSpecial_announcement() {
        return special_announcement;
    }

    public void setSpecial_announcement(ArrayList<special_announcement> special_announcement) {
        this.special_announcement = special_announcement;
    }

    public class CommonAnnounacementDetails {
        private String title;
        private String message;
        private String date;

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

    public class special_announcement {

//        private String classe;
//        private String section;
      //  private String title;
     //   private String message;
     //   private String teacher_name;
       // private String date;

//        public String getClasse() {
//            return classe;
//        }
//
//        public void setClasse(String classe) {
//            this.classe = classe;
//        }

//        public String getSection() {
//            return section;
//        }
//
//        public void setSection(String section) {
//            this.section = section;
//        }

       /* public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }*/

        /*public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }*/

        /*public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }*/

       /* public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }*/
    }

}
