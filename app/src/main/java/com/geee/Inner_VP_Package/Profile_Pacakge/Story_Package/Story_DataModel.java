package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;

import java.io.Serializable;

public class Story_DataModel implements Serializable {
    public   String image_url;
    public   String user_id;
    public String created;
    public String type;
    public Story_DataModel() {

    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
