package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * PostList is a class containing all the Posts created by one user
 */
public class PostList {
    private ArrayList<Post> postList;
    private ArrayList<Listener> listeners;

    /**
     * Instantiates a new Post list.
     */
    public PostList() {
        this.postList = new ArrayList<Post>();
        this.listeners = new ArrayList<Listener>();
    }

    /**
     *
     * @return the listeners
     */
    private ArrayList<Listener> getListeners() {
        if (this.listeners == null) {
            listeners = new ArrayList<Listener>();
        }
        return listeners;
    }

    /**
     * Notify listeners.
     */
    public void notifyListeners()
    {
        for (Listener l : getListeners())
        {
            l.update();
        }
    }

    /**
     * Add listener.
     *
     * @param l the listener
     */
// Adds listener to the HabitList
    public void addListener(Listener l)
    {
        listeners.add(l);
    }

    /**
     * Gets the posts.
     *
     * @return the posts
     */
    public ArrayList<Post> getPosts() {
        return this.postList;
    }

    /**
     * Add a post and notifies listeners.
     *
     * @param post the post
     */
    public void addPost(Post post) {
        postList.add(post);
        notifyListeners();
    }

    /**
     * Delete the post and notifies listeners
     *
     * @param post the post
     * @throws Exception the exception
     */
    public void deletePost(Post post) throws Exception{
        postList.remove(post);
        notifyListeners();
    }

    /**
     * Gets post.
     *
     * @param i the index
     * @return the post
     */
    public Post getPost(int i) {
        return postList.get(i);
    }

    /**
     * Search list post list for a keyword.
     *
     * @param keyword the keyword
     * @return the post list
     */
    public PostList searchList(String keyword) {
        return null;
    }

    /**
     * Sets post list and notifies listeners.
     *
     * @param postList the post list
     */
    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
        notifyListeners();
    }

    /**
     * Pick post.
     *
     * @param post the post
     */
    public void pickPost(Post post) {
        this.postList.clear();
        this.postList.add(post);
    }
}
