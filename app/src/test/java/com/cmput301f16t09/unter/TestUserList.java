package com.cmput301f16t09.unter;

import junit.framework.TestCase;

/**
 * Created by Daniel on 2016-10-12.
 */
public class TestUserList extends TestCase {

    public void testGetUsers() {
        User user = new User("Alvin", "AlvinEhh", "alvin@EmAil.com", "780-123-4567", "password");
        User user2 = new User("Kevin", "SwaGMasTer", "KeVin@EmAil.com", "780-541-6364", "password");
        User user3 = new User("Daniel", "DtrAn32", "KeLLy@EmAil.com", "780-654-1342", "password");
        User user4 = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        User user5 = new User("Simon", "SiMooooN", "SimoN@EmAil.com", "780-623-9812", "password");

    }

    public void testAddUser() {
    }
}
