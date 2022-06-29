package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;

import java.io.Serializable;

public class ShowStoryDM implements Serializable {
    public String attachment;
    public String userId;
    public String type;
    public String userName;
    public String userImg;
    public String created;
    public String  coverimaged;

    public ShowStoryDM() {

    }
    public String getcoverimage() {
        return coverimaged;
    }

    public void setcoverimage(String coverimage) {
        this.coverimaged = coverimage;
    }





    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
