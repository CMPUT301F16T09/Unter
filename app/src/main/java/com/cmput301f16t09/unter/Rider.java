package com.cmput301f16t09.unter;

/**
 * A Rider is a subclass of User, Riders request rides by creating a Post and listing the information
 * that rider needs to specify
 */

public class Rider extends User {
    /**
     * Instantiates a new Rider.
     *
     * @param name        the name
     * @param username    the username
     * @param email       the email
     * @param phoneNumber the phone number
     * @param password    the password
     */
    public Rider(String name, String username, String email, String phoneNumber, String password) {
        super(name, username, email, phoneNumber, password);
    }

    /**
     * Create post.
     *
     * @param pl            the pl
     * @param startLocation the start location
     * @param endLocation   the end location
     * @param fare          the fare
     * @param riderPost     the rider post
     */
    public void createPost(PostList pl, String startLocation,
                           String endLocation, String fare, Rider riderPost) {
        Post newPost = new Post(startLocation, endLocation, fare, riderPost);
        pl.addPost(newPost);
    }
}
