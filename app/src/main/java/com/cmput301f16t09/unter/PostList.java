package com.cmput301f16t09.unter;

import java.util.ArrayList;


public class PostList {
    private ArrayList<Post> postList;

    /**
     * Instantiates a new Post list.
     */
    public PostList() {
        this.postList = new ArrayList<Post>();
    }

    /**
     * Gets posts.
     *
     * @return the posts
     */
    public ArrayList<Post> getPosts() {
        return this.postList;
    }

    /**
     * Add post.
     *
     * @param post the post
     */
    public void addPost(Post post) {
        postList.add(post);
    }

    /**
     * Delete post.
     *
     * @param post the post
     * @throws Exception the exception
     */
    public void deletePost(Post post) throws Exception{
        postList.remove(post);
    }

    /**
     * Gets post.
     *
     * @param i the
     * @return the post
     */
    public Post getPost(int i) {
        return postList.get(i);
    }
}
