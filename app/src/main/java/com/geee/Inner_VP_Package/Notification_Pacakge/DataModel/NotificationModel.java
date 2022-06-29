package com.geee.Inner_VP_Package.Notification_Pacakge.DataModel;

public class NotificationModel {
    public String id;
    public String user_id;
    public String post_id;
    public String post_user_id;
    public String noti_msg;
    public String created;
    public String attachment;
    public String user_name;
    public String user_img;

    public NotificationModel() {

    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_user_id() {
        return post_user_id;
    }


    public String getNoti_msg() {
        return noti_msg;
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
}
