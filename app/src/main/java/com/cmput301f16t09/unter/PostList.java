package com.cmput301f16t09.unter;


import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * PostList is a class containing all the Posts created by one rider
 */
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

    /**
     * Search list post list.
     *
     * @param keyword the keyword
     * @return the post list
     */
    public PostList searchList(String keyword) {
        return null;
    }

    /**
     * Modify post.
     *
     * @param post           the post
     * @param start_Custom_location the start location
     * @param end_Custom_location   the end location
     * @param status         the status
     */
    public void ModifyPost(Post post, GeoPoint start_Custom_location, GeoPoint end_Custom_location, String status) {
        post.setStartLocation(start_Custom_location);
        post.setEndLocation(end_Custom_location);
        post.setStatus(status);
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }
}
