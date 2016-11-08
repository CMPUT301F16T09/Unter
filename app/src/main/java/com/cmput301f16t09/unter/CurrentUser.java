package com.cmput301f16t09.unter;

/**
 * Created by Mike on 2016-11-08.
 */
public class CurrentUser {
    public static User currentUser = null;

    static User getCurrentUser() {
        return currentUser;
    }

    static void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }
}
