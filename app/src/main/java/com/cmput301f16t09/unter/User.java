package com.cmput301f16t09.unter;

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
    private String id;

    /**
     * Instantiates a new User.
     *
     * @param name        the name
     * @param username    the username
     * @param email       the email
     * @param phoneNumber the phone number
     * @param password    the password
     * @param myRequests  the myRequests PostList
     * @param myOffers    the myOffers PostList
     * @param id          the jest id
     */
    public User(String name, String username, String email, String phoneNumber, String password) {
        this.name = name.toLowerCase();
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.myRequests = new PostList();
        this.myOffers =new PostList();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.toLowerCase();
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets my requests.
     *
     * @return the my requests
     */
    public PostList getMyRequests() {
        return myRequests;
    }

    /**
     * Gets my offers.
     *
     * @return the my offers
     */
    public PostList getMyOffers() {
        return myOffers;
    }

    /**
     * Sets my requests.
     *
     * @param myRequests the my requests
     */
    public void setMyRequests(PostList myRequests) {
        this.myRequests = myRequests;
    }

    /**
     * Sets my offers.
     *
     * @param myOffers the my offers
     */
    public void setMyOffers(PostList myOffers) {
        this.myOffers = myOffers;
    }

    /**
     * Add request reference.
     *
     * @param post the post
     */
    public void addRequestReference(Post post) {
        this.myRequests.addPost(post);
    }

    /**
     * Add offer reference.
     *
     * @param post the post
     */
    public void addOfferReference(Post post) {
        this.myOffers.addPost(post);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addRideRequest(Post rideRequest) {
        this.myRequests.addPost(rideRequest);
    }

    public void deleteRideRequest(Post rideRequest) {
        this.myRequests.getPosts().remove(rideRequest);
    }

    public void deleteOfferReference(Post rideRequest) {
        this.myOffers.getPosts().remove(rideRequest);
    }

    public String toString(){
        String output = getUsername();
        return output;
    }
}
