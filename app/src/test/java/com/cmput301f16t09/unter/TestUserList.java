package com.cmput301f16t09.unter;

import junit.framework.TestCase;


public class TestUserList extends TestCase {

    /**
     *
     * Test addUser to the actual UserList Model
     * US 3.01.01
     */
    public void testAddUser() {
        User user = new User("Alvin", "AlvinEhh", "alvin@EmAil.com", "780-123-4567", "password");
        User user2 = new User("Kevin", "SwaGMasTer", "KeVin@EmAil.com", "780-541-6364", "password");
        User user3 = new User("Daniel", "DtrAn32", "KeLLy@EmAil.com", "780-654-1342", "password");
        User user4 = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        User user5 = new User("Simon", "SiMooooN", "SimoN@EmAil.com", "780-623-9812", "password");

        UserList userList = new UserList();
        assertTrue(userList.getUserList().isEmpty());
        userList.addUser(user);
        assertFalse(userList.getUserList().isEmpty());
        assertTrue(userList.getUserList().size() == 1);
        userList.addUser(user2);
        assertTrue(userList.getUserList().size() == 2);
        userList.addUser(user3);
        assertTrue(userList.getUserList().size() == 3);
        userList.addUser(user4);
        assertTrue(userList.getUserList().size() == 4);
        userList.addUser(user5);
        assertTrue(userList.getUserList().size() == 5);
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
        assertTrue(userList.getUserList().isEmpty());
        userList.addUser(user);
        assertFalse(userList.getUserList().isEmpty());
        assertTrue(userList.getUserList().size() == 1);
        assertTrue(userList.getUserList().get(0).equals(user));
        userList.addUser(user2);
        assertTrue(userList.getUserList().size() == 2);
        assertTrue(userList.getUserList().get(1).equals(user2));
        userList.addUser(user3);
        assertTrue(userList.getUserList().size() == 3);
        assertTrue(userList.getUserList().get(2).equals(user3));
        userList.addUser(user4);
        assertTrue(userList.getUserList().size() == 4);
        assertTrue(userList.getUserList().get(3).equals(user4));
        userList.addUser(user5);
        assertTrue(userList.getUserList().size() == 5);
        assertTrue(userList.getUserList().get(4).equals(user5));
    }

    public void testRetrieveUserbyUsername(){

        UserList usrList = new UserList();
        User jellykelly = new User("Kelly", "JellYKeLly", "jellykelly@example.com", "780-653-1241", "kelly's password");

        User testEmptyList = usrList.searchByUsername(jellykelly.getUsername());
        assertEquals(testEmptyList instanceof NonexistantUserException);

        usrList.addUser(jellykelly);
        User testProperList = usrList.searchByUsername(jellykelly.getUsername());
        assertEquals(testProperList instanceof User);
        assertTrue(testProperList.getUsername().equals("JellYKeLly"));
        assertTrue(testProperList.getEmail().equals("jellykelly@example.com"));
        assertTrue(testProperList.getPhoneNumber().equals("780-653-1241"));
        assertTrue(testProperList.getPassword().equals("kelly's password"));

    }

    public void testAddListener(){

        UserList usrList = new UserList();
        Listener l = new Listener();
        usrList.addListener(l);
        assertEquals(usrList.getListener(l),l);

    }


    public void testDeleteListener(){

        UserList usrList = new UserList();
        Listener l = new Listener();
        usrList.addListener(l);
        usrList.deleteListener();
        assertTrue(usrList.getListener(l) instanceof ListenerNotInUserListException);

    }

    int updated = 0;
    public void testNotifyListeners(){
        UserList list = new UserList();
        Listener l = new Listener(){
            public void update(){
                list.this.updated++;
            }
        };

        list.addListener(l);

        User jellykelly = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password");
        list.addUser(jellykelly);
        assertEquals(this.updated,1);
    }

}
