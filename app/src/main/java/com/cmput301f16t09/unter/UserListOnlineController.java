package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

public class UserListOnlineController {
    private static JestDroidClient client;

    private static ArrayList<User> arrayUserList = null;
    private static UserList userList = new UserList();

    /**
     * Gets user list.
     *
     * @return the user list
     */
    static public UserList getUserList() {
        if (arrayUserList == null) {

            //Replace this with retrieval from location
            UserListOnlineController.GetUsersTask getUsersTask = new UserListOnlineController.GetUsersTask();
            getUsersTask.execute("");
            try {
                arrayUserList = getUsersTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            userList.setUserList(arrayUserList);
        }
        return userList;
    }

    public static class SearchUserListsTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            //Just list top 10000 users
            //Replace with our indexes
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match\": {\"" + search_parameters[0] + "\": \"" + search_parameters[1] + "\"}}}";

            // assume that search_parameters[0] is the only search term we are interested in using
            //Add Indexing
            Search search = new Search.Builder(search_string)
                    .addIndex("t09")
                    .addType("user")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<User> foundUsers = (ArrayList<User>) result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                } else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return users;
        }
    }

    public static class GetUsersTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            // assume that search_parameters[0] is the only search term we are interested in using
            //Add Indexing and such
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("t09")
                    .addType("user")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<User> foundUsers = (ArrayList<User>) result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                } else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return users;
        }
    }

    public static class AddUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                //Add Indexing and such
                Index index = new Index.Builder(user).index("t09").type("user").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the user.");
                        System.out.println("Elastic search was not able to add the user.");
                    }
                } catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a user to elastic search!");
                    System.out.println("We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


    public static class UpdateUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... user) {
            verifySettings();
            //Index index = new Index.Builder(user).index("t09").type("user").id(user[0].getId()).build();

//            String script =  "{\n" +
//                    "    \"script\" : \"ctx._source.tags += params\",\n" +
//                    "    \"params\" : {\n" +
//                    "        \"tag\" : \"blue\"\n" +
//                    "    }\n" +
//                    "}";


            String script = "{\n" +
                    "    \"doc\" : {\n" +
                    "        \"name\" :" + user[0].getName().toString() + ",\n" +
                    "        \"email\" :" + user[0].getEmail().toString() + ",\n" +
                    "        \"password\" :" + user[0].getPassword().toString() + ",\n" +
                    "        \"phoneNumber\" :" + user[0].getPhoneNumber().toString() + "\n" +
                    "    }\n" +
                    "}";
            try {
                client.execute(new Update.Builder(script).index("t09").type("user").build());

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Error","Could Not update user info.");
            }

            return null;
        }

    }

//    public static class DeleteUsersTask extends AsyncTask<User, Void, Void> {
//
//        //Need to fill
//        @Override
//        protected Void doInBackground(User... Users) {
//            verifySettings();
//        }
//    }

    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            //Replace with webserver
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }



    public void editUser(String name, String phoneNumber, String userEmail, String userPassword) {

        SearchUserListsTask  searchTask = new SearchUserListsTask();

        //User user = getLoggedIn();      //probably not going to be used

//        if (!name.isEmpty()) {user.setName(name);}
//        if (!phoneNumber.isEmpty()) {user.setPhoneNumber(phoneNumber);}
//        if (!userEmail.isEmpty()) {user.setEmail(userEmail);}
//        if (!userPassword.isEmpty()) {user.setPassword(userPassword);}
    }
}