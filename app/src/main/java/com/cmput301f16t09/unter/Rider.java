package com.cmput301f16t09.unter;

/**
 * Created by Daniel on 2016-10-12.
 */
public class Rider extends User {
    public Rider(String name, String username, String email, String phoneNumber, String password) {
        super(name, username, email, phoneNumber, password);
    }

    public void createPost(PostList pl, String startLocation,
                           String endLocation, String fare, Rider riderPost) {
        Post newPost = new Post(startLocation, endLocation, fare, riderPost);
        pl.addPost(newPost);
    }
}
