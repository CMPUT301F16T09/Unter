package com.cmput301f16t09.unter;

import io.searchbox.annotations.JestId;

/**
 * Created by Kevin on 11/21/2016.
 *
 * This model class keeps track of who the msg is meant for
 * it is to be stored on elastic search to be fetched by said user
 */

public class Notification {

    private String username;
    private String notification;
    @JestId
    private String documentId;
    private String postType ;

    public Notification(String user, String notification_message) {
        this.username = user;
        this.notification = notification_message;
        this.postType = "";
    }

    public String getUsername() {
        return username;
    }

    public String getNotification() {
        return notification;
    }

    public String getId() {
        return documentId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setId(String documentId) {
        this.documentId = documentId;
    }

    public String toString(){
        return notification;
    }

    public String getPostType(){
        return postType;
    }

    public void setPostType(String type){
        this.postType=type;
    }
}
