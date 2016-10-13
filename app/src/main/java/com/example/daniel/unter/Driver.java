package com.example.daniel.unter;

/**
 * Created by Daniel on 2016-10-12.
 */
public class Driver extends User {
    public Driver(String name, String username, String email, String phoneNumber, String password) {
        super(name, username, email, phoneNumber, password);
    }

    public void updatePost(Post p) {
        p.addDriverOffer(this.getName());
        if (p.getStatus().equals("Pending Offer")) {
            p.setStatus("Pending Approval");
        }
    }
}
