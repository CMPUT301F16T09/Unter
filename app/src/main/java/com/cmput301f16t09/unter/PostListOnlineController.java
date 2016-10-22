package com.cmput301f16t09.unter;

public class PostListOnlineController {
    private static PostList postlist = null;

    /**
     * Gets post list.
     *
     * @return the post list
     */
    static public PostList getPostList() {
        if (postlist == null) {

            //Replace this with retrieval from location
            postlist = new PostList();
        }
        return postlist;
    }
}
