package com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel;

import java.io.Serializable;

public class FollwerDataModel implements Serializable {
    public String user_id_follower;
    public String user_name_follwer;
    public String user_image_follwer;
    public String user_full_name;
    public String bio;
    public String website;
    public String followstatus;


    public FollwerDataModel() {

    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_id_follower() {
        return user_id_follower;
    }

    public String getUser_name_follwer() {
        return user_name_follwer;
    }


    public String getUser_image_follwer() {
        return user_image_follwer;
    }

}
