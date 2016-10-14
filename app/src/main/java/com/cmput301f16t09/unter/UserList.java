package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * UserList is a class that keeps track of all the members of Unter
 */


public class UserList {
    private ArrayList<User> userList;

    /**
     * Instantiates a new User list.
     */
    public UserList() {
        userList = new ArrayList<User>();
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public ArrayList<User> getUsers() {
        return this.userList;
    }

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(User user) {
        userList.add(user);
    }
}
