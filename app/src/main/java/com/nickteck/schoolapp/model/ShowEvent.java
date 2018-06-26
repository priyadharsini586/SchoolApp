package com.nickteck.schoolapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 6/23/2018.
 */

public  class ShowEvent {

    private String Status_code;
    private ArrayList<EventDetails> event_details;



    public ShowEvent(ArrayList<EventDetails> eventDetails, ArrayList<ImageDetails> showEventsImageArrayList,
                     ArrayList<VideoDetails> showEventsVideoArrayList) {
    }

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public ArrayList<EventDetails> getEvent_details() {
        return event_details;
    }

    public void setEvent_details(ArrayList<EventDetails> event_details) {
        this.event_details = event_details;
    }




    public static class EventDetails {
        private String title;
        private String content;
        private String held_on;
        private String date;
        private ArrayList<ImageDetails> image_details;
        private ArrayList<VideoDetails> video_details;

        public EventDetails(String title, String content, String held_on, String date,
                            ArrayList<ImageDetails> imageDetails,ArrayList<VideoDetails> videoDetails) {
            this.title = title;
            this.content = content;
            this.held_on = held_on;
            this.date = date;
            this.image_details = imageDetails;
            this.video_details = videoDetails;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHeld_on() {
            return held_on;
        }

        public void setHeld_on(String held_on) {
            this.held_on = held_on;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<ImageDetails> getImage_details() {
            return image_details;
        }

        public void setImage_details(ArrayList<ImageDetails> image_details) {
            this.image_details = image_details;
        }

        public ArrayList<VideoDetails> getVideo_details() {
            return video_details;
        }

        public void setVideo_details(ArrayList<VideoDetails> video_details) {
            this.video_details = video_details;
        }
    }

    public static class ImageDetails {

        private String img_url;
        private String description;

        public ImageDetails(String img_url,String img_description) {
            this.img_url = img_url;
            this.description = img_description;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class VideoDetails{
        private String video_url;
        private String description;

        public VideoDetails(String video_url,String video_description) {
            this.video_url = video_url;
            this.description = video_description;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public JSONObject toJSON() {
        JSONObject eventObject = new JSONObject();
        try {
            JSONArray eventDetailsArray = new JSONArray();
            if(event_details.size()>0){
                for(int i=0;i<event_details.size(); i++){

                    ShowEvent.EventDetails eventDetails = event_details.get(i);
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("title",eventDetails.getTitle());
                    jsonObject1.put("content",eventDetails.getContent());
                    jsonObject1.put("held_on",eventDetails.getHeld_on());
                    jsonObject1.put("date",eventDetails.getDate());
                    JSONArray imageArray = new JSONArray();
                    for (int j = 0 ; j < eventDetails.getImage_details().size() ; j ++){
                        ShowEvent.ImageDetails imageDetails = eventDetails.getImage_details().get(j);
                        JSONObject imageJson = new JSONObject();
                        imageJson.put("img_url",imageDetails.getImg_url());
                        imageJson.put("description",imageDetails.getDescription());
                        imageArray.put(imageJson);
                    }
                    jsonObject1.put("image_details",imageArray);
                    JSONArray videoArray = new JSONArray();
                    for(int k=0; k< eventDetails.getVideo_details().size(); k++){
                        ShowEvent.VideoDetails videoDetails = eventDetails.getVideo_details().get(k);
                        JSONObject videoJson = new JSONObject();
                        videoJson.put("video_url",videoDetails.getVideo_url());
                        videoJson.put("description",videoDetails.getDescription());
                        videoArray.put(videoJson);
                    }
                    jsonObject1.put("video_details",videoArray);
                    eventDetailsArray.put(jsonObject1);
                }

                eventObject.put("event_details",eventDetailsArray);

            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return eventObject;
    }

}


