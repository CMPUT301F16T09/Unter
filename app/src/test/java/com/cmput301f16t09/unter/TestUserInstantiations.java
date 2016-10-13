package com.cmput301f16t09.unter;

import junit.framework.TestCase;


public class TestUserInstantiations extends TestCase {

    /**
     * Test get driver.
     */
    public void testGetDriver() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        user.setDriver(new Driver(name, username, email, phoneNumber, password));
        Rider r = user.getRider();
        Driver d = user.getDriver();
        UserInstantiations riderAnddriver = new UserInstantiations(r, d);
        assertTrue(riderAnddriver.getDriver().equals(d));
        assertTrue(riderAnddriver.getDriver().getName().equals(name.toLowerCase()));
        assertTrue(riderAnddriver.getDriver().getUsername().equals(username.toLowerCase()));
        assertTrue(riderAnddriver.getDriver().getEmail().equals(email.toLowerCase()));
        assertTrue(riderAnddriver.getDriver().getPhoneNumber().equals(phoneNumber));
        assertTrue(riderAnddriver.getDriver().getPassword().equals(password));
    }

    /**
     * Test get rider.
     */
    public void testGetRider() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        user.setRider(new Rider(name, username, email, phoneNumber, password));
        user.setDriver(new Driver(name, username, email, phoneNumber, password));
        Rider r = user.getRider();
        Driver d = user.getDriver();
        UserInstantiations riderAnddriver = new UserInstantiations(r, d);
        assertTrue(riderAnddriver.getRider().equals(r));
        assertTrue(riderAnddriver.getRider().getName().equals(name.toLowerCase()));
        assertTrue(riderAnddriver.getRider().getUsername().equals(username.toLowerCase()));
        assertTrue(riderAnddriver.getRider().getEmail().equals(email.toLowerCase()));
        assertTrue(riderAnddriver.getRider().getPhoneNumber().equals(phoneNumber));
        assertTrue(riderAnddriver.getRider().getPassword().equals(password));
    }
}
