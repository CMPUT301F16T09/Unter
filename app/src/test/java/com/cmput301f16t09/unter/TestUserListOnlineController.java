package com.cmput301f16t09.unter;

import junit.framework.TestCase;

import java.util.ArrayList;

public class TestUserListOnlineController extends TestCase {

//    public void testRetrieveUsers() {
//        try {
//            UserListOnlineController.GetUsersTask getUsersTask = new UserListOnlineController.GetUsersTask();
//            getUsersTask.execute("");
//            ArrayList<User> userlist = getUsersTask.get();
//        }
//        catch (Exception e) {
//            fail("Couldn't obtain from elastic server.");
//        }
//    }

    public void testaddUser() {
        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        try {
            UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
            addUserTask.execute(new_user);
        }
        catch (Exception e) {
            fail("Couldn't add user");
        }
        try {
            UserListOnlineController.GetUsersTask getUsersTask = new UserListOnlineController.GetUsersTask();
            getUsersTask.execute("");
            ArrayList<User> userlist = getUsersTask.get();
            assertTrue(userlist.contains(new_user));
        }
        catch (Exception e) {
            fail("Couldn't obtain from elastic server.");
        }
    }

//    public void testdeleteUser() {
//        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
//        try {
//            UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
//            addUserTask.execute(new_user);
//        }
//        catch (Exception e) {
//            fail("Couldn't add user");
//        }
//        try {
//            UserList userlist = UserListOnlineController.getUserList();
//            UserListOnlineController.DeleteUsersTask deleteUsersTask = new UserListOnlineController.DeleteUsersTask();
//            deleteUsersTask.execute(new_user);
//            assertFalse(userlist.getUserList().contains(new_user));
//        }
//        catch (Exception e) {
//            fail("Couldn't delete user or obtain from server");
//        }
//    }

    public void testsearchUser() {
        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        User new_user2 = new User("Henry2", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        User new_user3 = new User("Aaron", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        try {
            UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
            searchUserListsTask.execute("Henry");
            ArrayList<User> userlist = searchUserListsTask.get();
            assertTrue(userlist.contains(new_user));
            assertTrue(userlist.contains(new_user2));
            assertFalse(userlist.contains(new_user3));
        } catch (Exception e) {
            fail("Couldn't complete search");
        }
    }
}
