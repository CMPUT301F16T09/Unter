package com.cmput301f16t09.unter;

public class CurrentUser {
    private static User currentUser = null;

    private static Post currentPost = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    public static Post getCurrentPost() {
        return currentPost;
    }

    public static void setCurrentPost(Post newCurrentPost) {
        currentPost = newCurrentPost;
    }

}
