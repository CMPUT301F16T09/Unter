package com.cmput301f16t09.unter;

import junit.framework.TestCase;

/**
 * Created by Daniel on 2016-10-12.
 */
public class TestPost extends TestCase {

    public void testGetStartLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStartLocation(), "12345 67St");
    }

    public void testSetStartLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStartLocation(), "12345 67St");
        current_post.setStartLocation("12121 12St");
        assertEquals(current_post.getStartLocation(), "12121 12St");
    }

    public void testGetEndLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getEndLocation(), "54321 76St");
    }

    public void testSetEndLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStartLocation(), "12345 67St");
        current_post.setEndLocation("12121 12St");
        assertEquals(current_post.getEndLocation(), "12121 12St");
    }

    public void testGetStatus() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStatus(), "Pending Offer");
    }

    public void testGetDriveOffers() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertTrue(current_post.getDriveOffers().isEmpty());
    }

    public void testSetStatus() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStatus(), "Pending Offer");
        current_post.setStatus("Pending Approval");
        assertEquals(current_post.getStatus(), "Pending Approval");
    }

    public void testGetFare() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getFare(), "55");
    }

    public void testSetFare() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getFare(), "55");
        current_post.setFare("66");
        assertEquals(current_post.getFare(), "66");
    }

    public void testGetRiderPost() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(postlist.getPost(0), current_post);
    }

    public void testAddDriverOffer() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.getrideOrDrive().setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getrideOrDrive().getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertTrue(current_post.getDriveOffers().isEmpty());
        current_post.addDriverOffer("Paul");
        assertEquals(current_post.getDriveOffers().get(0), "Paul");
    }
}
