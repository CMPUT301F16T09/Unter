package com.cmput301f16t09.unter;

import io.searchbox.annotations.JestId;

/**
 * Created by Wookiez on 11/21/2016.
 */

public class Notification {

    private String username;
    private String notification;
    @JestId
    private String documentId;

    public Notification(String user, String notification_message) {
        this.username = user;
        this.notification = notification_message;
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
}
