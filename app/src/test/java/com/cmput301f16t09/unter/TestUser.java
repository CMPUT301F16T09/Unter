package com.cmput301f16t09.unter;

import junit.framework.TestCase;

/**
 * Created by Daniel on 2016-10-12.
 */
public class TestUser extends TestCase{
    public void testGetUserName() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertFalse(user.getName().equals("Alvin"));
        assertTrue(user.getName().equals("alvin"));
    }

    public void testGetUserUsername() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertFalse(user.getUsername().equals("AlvinEhh"));
        assertTrue(user.getUsername().equals("alvinehh"));

    }

    public void testGetUserPhoneNumber() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getPhoneNumber().equals("780-123-4567"));
    }

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

    public void testSetUserName() {
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

    public void testSetUserUsername() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getUsername().equals("alvin"));
        user.setUsername("KevinWASSUP");
        assertFalse(user.getUsername().equals("alvinehh"));
        assertTrue(user.getUsername().equals("kevinwassup"));
        assertFalse(user.getUsername().equals("KevinWASSUP"));
    }

    public void testSetUserPhoneNumber() {
        String name = "Alvin";
        String username = "AlvinEhh";
        String email = "alvin@email.com";
        String phoneNumber = "780-123-4567";
        String password = "password";
        User user = new User(name, username, email, phoneNumber, password);
        assertTrue(user.getPhoneNumber().equals("Alvin"));
        user.setPhoneNumber("780-345-6789");
        assertFalse(user.getPhoneNumber().equals("780-123-4567"));
        assertTrue(user.getPhoneNumber().equals("780-345-6789"));
    }

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
}
