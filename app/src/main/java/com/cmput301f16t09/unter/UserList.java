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
    public ArrayList<User> getUserList() {
        return this.userList;
    }

    /**
     * Sets user list.
     *
     * @param userList the user list
     */
    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(User user) {
        userList.add(user);
    }

    /**
     * Delete user.
     *
     * @param user the user
     */
    public void deleteUser(User user) {
        this.userList.remove(user);
    }

    /**
     * Modify user.
     *
     * @param user         the user
     * @param name         the name
     * @param phone_number the phone number
     * @param email        the email
     */
    public void ModifyUser(User user, String name, String phone_number, String email) {
        user.setName(name);
        user.setPhoneNumber(phone_number);
        user.setEmail(email);
    }


}
