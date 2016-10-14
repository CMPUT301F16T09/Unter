package com.cmput301f16t09.unter;

/**
 * Driver is a subclass of User, Drivers can accept a riders post by updating its status
 */

public class Driver extends User {
    /**
     * Instantiates a new Driver.
     *
     * @param name        the name
     * @param username    the username
     * @param email       the email
     * @param phoneNumber the phone number
     * @param password    the password
     */
    public Driver(String name, String username, String email, String phoneNumber, String password) {
        super(name, username, email, phoneNumber, password);
    }

    /**
     * Update post.
     *
     * @param p the p
     */
    public void updatePost(Post p) {
        p.addDriverOffer(this.getName());
        if (p.getStatus().equals("Pending Offer")) {
            p.setStatus("Pending Approval");
        }
    }
}
