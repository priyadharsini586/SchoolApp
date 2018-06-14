package com.nickteck.schoolapp.model;

/**
 * Created by admin on 6/13/2018.
 */

public class LoginDetails {

  private String Status_code;
  private String Status_message;
  private int image_drawable;


    public int getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(int image_drawable) {
        this.image_drawable = image_drawable;
    }

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getStatus_message() {
        return Status_message;
    }

    public void setStatus_message(String status_message) {
        Status_message = status_message;
    }
}
