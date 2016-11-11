package com.cmput301f16t09.unter;

/**
 * Created by Mike on 2016-11-08.
 */
public class CurrentUser {
    private static User currentUser = null;
    private static Post currentPost = null;

    static User getCurrentUser() {
        return currentUser;
    }

    static void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    static void setPost(Post newCurrentPost) {
        currentPost = newCurrentPost;
    }

    static Post getPost() {
        return currentPost;
    }
}
