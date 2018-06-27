package com.nickteck.schoolapp.model;

import java.io.Serializable;

/**
 * Created by admin on 6/26/2018.
 */

public class CommonImageVideoEventData implements Serializable {

    public static final int IMAGE_TYPE=0;
    public static final int VIDEO_TYPE=1;

    private int type;
    private String image_url;
    private String image_description;
    private String video_url;
    private String video_description;

   /* public CommonImageVideoEventData(int type, String image_url, String image_description, String video_url, String video_description) {
        this.type = type;
        this.image_url = image_url;
        this.image_description = image_description;
        this.video_url = video_url;
        this.video_description = video_description;
    }*/





    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_description() {
        return image_description;
    }

    public void setImage_description(String image_description) {
        this.image_description = image_description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }
}
