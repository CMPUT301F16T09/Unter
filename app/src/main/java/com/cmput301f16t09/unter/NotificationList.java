package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * Created by Wookiez on 11/21/2016.
 */

public class NotificationList {
    private ArrayList<Notification> notificationList;

    public NotificationList() {
        this.notificationList = new ArrayList<Notification>();
    }

    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
