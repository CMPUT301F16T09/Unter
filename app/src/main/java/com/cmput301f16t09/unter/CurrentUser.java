package com.cmput301f16t09.unter;

import android.app.Notification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A model class that holds the Current User's most recently accessed information to be
 * stored on their phone's local memory.
 * Uses Lazy Singleton to allow for its information to be accessed from anywhere.
 *
 * currentPost is Post object that is set when the user selects a post from any ListView
 */
public class CurrentUser {
    private static User currentUser = null;
    private static Post currentPost = null;


    /**
     * Retrieves the user object representing the person who initially logged
     * into the app and their information is recorded in this object.
     *
     * @return the current user object
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Upon login, changes the null object with a real user object once
     * the user has logged into the app successfully.
     *
     * @param newCurrentUser the user object representing the current user to be swapped
     */
    public static void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    /**
     * Gets the current post that the user has selected previously. Usually from a ListView.
     * Used to prevent passing bundles inside intents very often between a listview and
     * a specific posts details in two different activity screens.
     *
     * @return the current post
     */
    public static Post getCurrentPost() {
        return currentPost;
    }

    /**
     * Sets current post to be the one that user has selected for later retrieval.
     *
     * @param newCurrentPost the new current post
     */
    public static void setCurrentPost(Post newCurrentPost) {
        currentPost = newCurrentPost;
    }

}
