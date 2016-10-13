package com.cmput301f16t09.unter;

import junit.framework.TestCase;


public class TestPost extends TestCase {

    /**
     * Test get start location.
     */
    public void testGetStartLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStartLocation(), "12345 67St");
    }

    /**
     * Test set start location.
     */
    public void testSetStartLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStartLocation(), "12345 67St");
        current_post.setStartLocation("12121 12St");
        assertEquals(current_post.getStartLocation(), "12121 12St");
    }

    /**
     * Test get end location.
     */
    public void testGetEndLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getEndLocation(), "54321 76St");
    }

    /**
     * Test set end location.
     */
    public void testSetEndLocation() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStartLocation(), "12345 67St");
        current_post.setEndLocation("12121 12St");
        assertEquals(current_post.getEndLocation(), "12121 12St");
    }

    /**
     * Test get status.
     */
    public void testGetStatus() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStatus(), "Pending Offer");
    }

    /**
     * Test get drive offers.
     */
    public void testGetDriveOffers() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertTrue(current_post.getDriveOffers().isEmpty());
    }

    /**
     * Test set status.
     */
    public void testSetStatus() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getStatus(), "Pending Offer");
        current_post.setStatus("Pending Approval");
        assertEquals(current_post.getStatus(), "Pending Approval");
    }

    /**
     * Test get fare.
     */
    public void testGetFare() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getFare(), "55");
    }

    /**
     * Test set fare.
     */
    public void testSetFare() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(current_post.getFare(), "55");
        current_post.setFare("66");
        assertEquals(current_post.getFare(), "66");
    }

    /**
     * Test get rider post.
     */
    public void testGetRiderPost() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertEquals(postlist.getPost(0), current_post);
    }

    /**
     * Test add driver offer.
     */
    public void testAddDriverOffer() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        PostList postlist = new PostList();
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        Rider rider = user.getRider();
        rider.createPost(postlist, "12345 67St", "54321 76St", "55", rider);
        Post current_post = postlist.getPost(0);
        assertTrue(current_post.getDriveOffers().isEmpty());
        current_post.addDriverOffer("Paul");
        assertEquals(current_post.getDriveOffers().get(0), "Paul");
    }
}
