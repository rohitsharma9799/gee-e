package com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel;

import java.io.Serializable;

public class FollowReqModel implements Serializable {
    public String request_id;
    public String user_name_follwer;
    public String user_image_follwer;
    public String user_full_name;
    public String sender_id;
    public String recever_id;
    public String bio;
    public String website;

    public FollowReqModel() {

    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecever_id() {
        return recever_id;
    }

    public void setRecever_id(String recever_id) {
        this.recever_id = recever_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getUser_name_follwer() {
        return user_name_follwer;
    }

    public void setUser_name_follwer(String user_name_follwer) {
        this.user_name_follwer = user_name_follwer;
    }

    public String getUser_image_follwer() {
        return user_image_follwer;
    }

    public void setUser_image_follwer(String user_image_follwer) {
        this.user_image_follwer = user_image_follwer;
    }

}
