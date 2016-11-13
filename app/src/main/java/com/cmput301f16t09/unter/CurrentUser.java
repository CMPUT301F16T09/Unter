package com.cmput301f16t09.unter;

/**
 * The type Current user.
 */
public class CurrentUser {
    private static User currentUser = null;
    private static Post currentPost = null;

    /**
     * Gets current user.
     *
     * @return the current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets current user.
     *
     * @param newCurrentUser the new current user
     */
    public static void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    /**
     * Gets current post.
     *
     * @return the current post
     */
    public static Post getCurrentPost() {
        return currentPost;
    }

    /**
     * Sets current post.
     *
     * @param newCurrentPost the new current post
     */
    public static void setCurrentPost(Post newCurrentPost) {
        currentPost = newCurrentPost;
    }
}
