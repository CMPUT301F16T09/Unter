package com.cmput301f16t09.unter;

import io.searchbox.annotations.JestId;

/**
 * This model class keeps track of who the msg is meant for
 * it is to be stored on elastic search to be fetched by said user
 */
public class Notification {

    private String username;
    private String notification;
    @JestId
    private String documentId;
    private String postType ;

    /**
     * Instantiates a new Notification.
     *
     * @param user                 the user
     * @param notification_message the notification message
     */
    public Notification(String user, String notification_message) {
        this.username = user;
        this.notification = notification_message;
        this.postType = "";
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets notification.
     *
     * @return the notification
     */
    public String getNotification() {
        return notification;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return documentId;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets notification.
     *
     * @param notification the notification
     */
    public void setNotification(String notification) {
        this.notification = notification;
    }

    /**
     * Sets id.
     *
     * @param documentId the document id
     */
    public void setId(String documentId) {
        this.documentId = documentId;
    }

    public String toString(){
        return notification;
    }

    /**
     * Get post type string.
     *
     * @return the string
     */
    public String getPostType(){
        return postType;
    }

    /**
     * Set post type.
     *
     * @param type the type
     */
    public void setPostType(String type){
        this.postType=type;
    }
}
