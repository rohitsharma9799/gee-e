package com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.DataModel;

import java.io.Serializable;

public class Grid_Data_Model implements Serializable {

    public String id;
    public String caption;
    public String attachment;
    public String location_string;
    public String lat;
    public String long_location;
    public String active;
    public String user_id;
    public String created;
    public String is_bookmark;
    public String is_like;
    public String user_name;
    public String user_img;
    public String total_likes;
    public String follow;
    public String commentCount;
    public String devicetoken;
    public Grid_Data_Model() {

    }
    public String getdevice_token() {
        return devicetoken;
    }

    public void setdevice_token(String device_token) {
        this.devicetoken = device_token;
    }

    public String getFollow() {
        return follow;
    }

    public String getTotal_likes() {
        return total_likes;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getLocation_string() {
        return location_string;
    }

    public void setLocation_string(String location_string) {
        this.location_string = location_string;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getIs_bookmark() {
        return is_bookmark;
    }


    public String getIs_like() {
        return is_like;
    }


}
