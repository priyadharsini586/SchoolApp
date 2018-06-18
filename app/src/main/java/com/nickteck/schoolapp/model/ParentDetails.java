package com.nickteck.schoolapp.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 6/18/2018.
 */

public class ParentDetails {
   private String Status_code,device_id,parent_id,parent_name;

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

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parent_id",parent_id);
            jsonObject.put("parent_name",parent_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
