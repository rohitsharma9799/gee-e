package com.geee.Inner_VP_Package.Home_Package.DataModel;

public class Like_DataModel {
    public String id;
    public String user_id;
    public String full_name;
    public String user_name;
    public String user_pic;
    public String follow_unfolow;

    public Like_DataModel(String id, String user_id, String full_name, String user_pic, String follow_unfolow, String user_name) {
        this.id = id;
        this.user_id = user_id;
        this.full_name = full_name;
        this.user_pic = user_pic;
        this.follow_unfolow = follow_unfolow;
        this.user_name = user_name;
    }


    public Like_DataModel() {

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getFollow_unfolow() {
        return follow_unfolow;
    }

    public void setFollow_unfolow(String follow_unfolow) {
        this.follow_unfolow = follow_unfolow;
    }
}
