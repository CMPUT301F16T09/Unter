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
     * Gets users in the list.
     *
     * @return the userlist
     */
    public ArrayList<User> getUserList() {
        return this.userList;
    }

    /**
     * Sets userlist to the inputted list of users.
     *
     * @param userList the user list
     */
    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    /**
     * Add a user to the list
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        userList.add(user);
    }

    /**
     * Delete user from the list
     *
     * @param user the user to delete
     */
    public void deleteUser(User user) {
        this.userList.remove(user);
    }

    /**
     * Modify the users Name, Phone number and email
     *
     * @param user         the user to edit
     * @param name         the name to change to
     * @param phone_number the phone number to change to
     * @param email        the email to change to
     */
    public void ModifyUser(User user, String name, String phone_number, String email) {
        user.setName(name);
        user.setPhoneNumber(phone_number);
        user.setEmail(email);
    }

    /**
     * Search by username user.
     *
     * @param username the username
     * @return the user
     */
    public User searchByUsername(String username) {
        return null;
    }

    /**
     * Add listener.
     *
     * @param l the l
     */
    public void addListener(Listener l) {
    }

    /**
     * Gets listener.
     *
     * @param l the l
     * @return the listener
     */
    public long getListener(Listener l) {
        return 1;
    }

    /**
     * Delete listener.
     */
    public void deleteListener() {
    }
}
