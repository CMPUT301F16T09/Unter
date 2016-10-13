package com.example.daniel.unter;

import java.util.ArrayList;

/**
 * Created by Daniel on 2016-10-12.
 */
public class UserList {
    private ArrayList<User> userList;

    public UserList() {
        userList = new ArrayList<User>();
    }

    public ArrayList<User> getUsers() {
        return this.userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }
}
