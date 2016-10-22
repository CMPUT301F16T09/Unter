package com.cmput301f16t09.unter;

public class UserListOnlineController {
    private static UserList userlist = null;


    /**
     * Gets user list.
     *
     * @return the user list
     */
    static public UserList getUserList() {
        if (userlist == null) {

            //Replace this with retrieval from location
            userlist = new UserList();
        }
        return userlist;
    }
}
