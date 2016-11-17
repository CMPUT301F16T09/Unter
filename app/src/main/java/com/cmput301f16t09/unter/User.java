package com.cmput301f16t09.unter;

import io.searchbox.annotations.JestId;

/**
 * Users are members of Unter, a User class contains personal information that is needed to use Unter
 * Data class
 */
public class User {
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private PostList myRequests;
    private PostList myOffers;
    @JestId
    private String documentId;

    /**
     * Instantiates a new User.
     *
     * @param name        the name of the user
     * @param username    the username of the user
     * @param email       the email of the user
     * @param phoneNumber the phone number of the user
     * @param password    the password of the user
     */
    public User(String name, String username, String email, String phoneNumber, String password) {
        this.name = name.toLowerCase();
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.myRequests = new PostList();
        this.myOffers = new PostList();
    }

    /**
     * Returns the User's id.
     *
     * @return the id
     */
    public String getId() {
        return documentId;
    }

    /**
     * Changes User's id to the input of this method
     *
     * @param id the id
     */
    public void setId(String id) {
        this.documentId = id;
    }

    /**
     * Returns the User's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes User's name to the input of the method
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Returns the User's username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the User's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes User's email to the input of the method
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * Returns the User's phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Changes User's phone number to the input of the method
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.toLowerCase();
    }

    /**
     * Returns the User's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Changes User's password to the input of the method
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the User's list of requests.
     *
     * @return the my requests
     */
    public PostList getMyRequests() {
        return myRequests;
    }

    /**
     * Returns the User's list of offers.
     *
     * @return the my offers
     */
    public PostList getMyOffers() {
        return myOffers;
    }

    /**
     * Changes User's my requests to the input of the method
     *
     * @param myRequests the my requests
     */
    public void setMyRequests(PostList myRequests) {
        this.myRequests = myRequests;
    }

    /**
     * Changes User's my offers to the input of the method
     *
     * @param myOffers the my offers
     */
    public void setMyOffers(PostList myOffers) {
        this.myOffers = myOffers;
    }

    /**
     * Add the inputted request reference.
     *
     * @param post the post
     */
    public void addRequestReference(Post post) {
        this.myRequests.addPost(post);
    }

    /**
     * Add the inputted offer reference.
     *
     * @param post the post
     */
    public void addOfferReference(Post post) {
        this.myOffers.addPost(post);
    }

    /**
     * Changes User's username to the input of the method
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Add the inputted ride request.
     *
     * @param rideRequest the ride request
     */
    public void addRideRequest(Post rideRequest) {
        this.myRequests.addPost(rideRequest);
    }

    /**
     * Delete the inputted ride request.
     *
     * @param rideRequest the ride request
     */
    public void deleteRideRequest(Post rideRequest) {
        this.myRequests.getPosts().remove(rideRequest);
    }

    /**
     * Delete the inputted offer reference.
     *
     * @param rideRequest the ride request
     */
    public void deleteOfferReference(Post rideRequest) {
        this.myOffers.getPosts().remove(rideRequest);
    }

    /**
     * Returns the username of the User
     * same as @getUsername()
     * @return output, the username of the user
     */

//    public String toString(){
//        String output = getUsername();
//        return output;
//    }
}
