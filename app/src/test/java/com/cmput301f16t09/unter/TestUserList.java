package com.cmput301f16t09.unter;

import junit.framework.TestCase;


public class TestUserList extends TestCase {

    /**
     * Test add user.
     */
    public void testAddUser() {
        User user = new User("Alvin", "AlvinEhh", "alvin@EmAil.com", "780-123-4567", "password");
        User user2 = new User("Kevin", "SwaGMasTer", "KeVin@EmAil.com", "780-541-6364", "password");
        User user3 = new User("Daniel", "DtrAn32", "KeLLy@EmAil.com", "780-654-1342", "password");
        User user4 = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        User user5 = new User("Simon", "SiMooooN", "SimoN@EmAil.com", "780-623-9812", "password");

        UserList userList = new UserList();
        assertTrue(userList.getUsers().isEmpty());
        userList.addUser(user);
        assertFalse(userList.getUsers().isEmpty());
        assertTrue(userList.getUsers().size() == 1);
        userList.addUser(user2);
        assertTrue(userList.getUsers().size() == 2);
        userList.addUser(user3);
        assertTrue(userList.getUsers().size() == 3);
        userList.addUser(user4);
        assertTrue(userList.getUsers().size() == 4);
        userList.addUser(user5);
        assertTrue(userList.getUsers().size() == 5);
    }

    /**
     * Test get users.
     */
    public void testGetUsers() {
        User user = new User("Alvin", "AlvinEhh", "alvin@EmAil.com", "780-123-4567", "password");
        User user2 = new User("Kevin", "SwaGMasTer", "KeVin@EmAil.com", "780-541-6364", "password");
        User user3 = new User("Daniel", "DtrAn32", "KeLLy@EmAil.com", "780-654-1342", "password");
        User user4 = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        User user5 = new User("Simon", "SiMooooN", "SimoN@EmAil.com", "780-623-9812", "password");

        UserList userList = new UserList();
        assertTrue(userList.getUsers().isEmpty());
        userList.addUser(user);
        assertFalse(userList.getUsers().isEmpty());
        assertTrue(userList.getUsers().size() == 1);
        assertTrue(userList.getUsers().get(0).equals(user));
        userList.addUser(user2);
        assertTrue(userList.getUsers().size() == 2);
        assertTrue(userList.getUsers().get(1).equals(user2));
        userList.addUser(user3);
        assertTrue(userList.getUsers().size() == 3);
        assertTrue(userList.getUsers().get(2).equals(user3));
        userList.addUser(user4);
        assertTrue(userList.getUsers().size() == 4);
        assertTrue(userList.getUsers().get(3).equals(user4));
        userList.addUser(user5);
        assertTrue(userList.getUsers().size() == 5);
        assertTrue(userList.getUsers().get(4).equals(user5));
    }
}
