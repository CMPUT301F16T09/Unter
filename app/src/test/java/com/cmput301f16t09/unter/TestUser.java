package com.cmput301f16t09.unter;

import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;

public class TestUser extends TestCase{
    /**
     * US 3.03.01 Display Contact Info
     * Test get user name.
     */
    public void testGetName() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);

        assertFalse(user.getName().equals("Alvin"));
        assertTrue(user.getName().equals("alvin"));
    }

    /**
     * US 3.03.01 Display Contact Info
     * Test get user username.
     */
    public void testGetUsername() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertFalse(user.getUsername().equals("AlvinEhh"));
        assertTrue(user.getUsername().equals("alvinehh"));

    }

    /**
     * US 3.03.01 Display Contact Info
     * US 1.05.01 Phone Driver who accepted my request
     * Test get user phone number.
     */
    public void testGetUserPhoneNumber() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getPhoneNumber().equals("780-123-4567"));
    }

    /**
     * US 3.03.01 Display Contact Info
     * US 1.05.01 Email Driver who accepted my request
     * Test get user email.
     */
    public void testGetUserEmail() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alViN@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertFalse(user.getEmail().equals("alViN@email.com"));
        assertTrue(user.getEmail().equals("alvin@email.com"));
    }

    /**
     * Test get user password.
     */
    public void testGetUserPassword() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "paSSword";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getPassword().equals("paSSword"));
        assertFalse(user.getPassword().equals("password"));
    }

    /**
     * US 3.02.01 Edit Contact Info
     * Test set name.
     */
    public void testSetName() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getName().equals("alvin"));
        user.setName("KeVin");
        assertFalse(user.getName().equals("alvin"));
        assertTrue(user.getName().equals("kevin"));
        assertFalse(user.getName().equals("KeVin"));
    }

    /**
     * US 3.02.01 Edit Contact Info
     * Test set user phone number.
     */
    public void testSetUserPhoneNumber() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getPhoneNumber().equals("780-123-4567"));
        user.setPhoneNumber("780-345-6789");
        assertFalse(user.getPhoneNumber().equals("780-123-4567"));
        assertTrue(user.getPhoneNumber().equals("780-345-6789"));
    }

    /**
     * US 3.02.01 Edit Contact Info
     * Test set user email.
     */
    public void testSetUserEmail() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getEmail().equals("alvin@email.com"));
        user.setEmail("kevin@Email.com");
        assertFalse(user.getEmail().equals("alvin@email.com"));
        assertTrue(user.getEmail().equals("kevin@email.com"));
        assertFalse(user.getEmail().equals("kevin@Email.com"));
    }

    /**
     * US 3.01.01 Unique User Profile
     * US 3.02.01 Edit Contact Info
     * Test set user password.
     * Passwords are optional. There will be a password when a new user is created.
     * This allows for that password to be changed.
     */
    public void testSetUserPassword() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getPassword().equals("password"));
        user.setPassword("12345Aa");
        assertFalse(user.getPassword().equals("password"));
        assertTrue(user.getPassword().equals("12345Aa"));
        assertFalse(user.getPassword().equals("12345aa"));
    }

    /**
     *US 1.01.01 Create Requests between two locations
     *US 1.02.01
     */

    public void testAddtoMyRequests(){
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        GeoPoint startLoc = new GeoPoint(53.52676, -113.52715);
        GeoPoint endLoc = new GeoPoint(53.54565, -113.49026);
        String myFare = "40.39";
        Post rideRequest = new Post(startLoc,endLoc,myFare, newUsr);
        newUsr.addRideRequest(rideRequest);
        assertEquals(newUsr.getMyRequests().getPosts().size(),1);

    }

    /**
     * US 1.04.01 Cancel Requests
     * US 1.07.01 Rider Confirms Completion
     *
     *
     */
    public void testDeleteFromMyRequests(){
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        GeoPoint startLoc = new GeoPoint(53.52676, -113.52715);
        GeoPoint endLoc = new GeoPoint(53.54565, -113.49026);
        String myFare = "40.39";
        Post rideRequest = new Post(startLoc,endLoc,myFare, newUsr);
        newUsr.addRideRequest(rideRequest);
        assertEquals(newUsr.getMyRequests().getPosts().size(),1);
        newUsr.deleteRideRequest(rideRequest);
        assertEquals(newUsr.getMyRequests().getPosts().size(),0);
    }

    /**
     * US 5.01.01 Driver Accepts a Request
     *
     */
    public void testAddtoMyOffers(){
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        GeoPoint startLoc = new GeoPoint(53.52676, -113.52715);
        GeoPoint endLoc = new GeoPoint(53.54565, -113.49026);
        String myFare = "40.39";
        Post a_request_i_accepted = new Post(startLoc,endLoc,myFare, newUsr);
        newUsr.addOfferReference(a_request_i_accepted);
        assertEquals(newUsr.getMyOffers().getPosts().size(),1);
    }

    /**
     * US 1.08.01 Rider choses a driver
     * details outlined in "call dibs" in glossary
     */
    public void testDeleteFromMyOffers(){
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        GeoPoint startLoc = new GeoPoint(53.52676, -113.52715);
        GeoPoint endLoc = new GeoPoint(53.54565, -113.49026);
        String myFare = "40.39";
        Post rideRequest = new Post(startLoc,endLoc,myFare, newUsr);
        newUsr.addOfferReference(rideRequest);
        assertEquals(newUsr.getMyRequests().getPosts().size(),1);

        newUsr.deleteOfferReference(rideRequest);
        assertEquals(newUsr.getMyOffers().getPosts().size(),0);
    }
}
