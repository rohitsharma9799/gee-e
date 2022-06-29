package com.geee.Inner_VP_Package.Home_Package.DataModel;

import java.io.Serializable;

public class Comment_Data_Model implements Serializable {
    public int id;
    public String PA_comment;
    public String user_name;
    public String user_id;
    public String full_name;
    public String user_img;
    public String time_ago;

    public Comment_Data_Model() {

    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getPA_comment() {
        return PA_comment;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
}
