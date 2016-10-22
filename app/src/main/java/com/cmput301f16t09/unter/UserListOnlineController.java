package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class UserListOnlineController {
    private static JestDroidClient client;

    private static UserList userlist = null;

    /**
     * Gets user list.
     *
     * @return the user list
     */
    static public UserList getUserList() {
        if (userlist == null) {

            //Replace this with retrieval from location
            UserListOnlineController.GetUsersTask getUsersTask = new UserListOnlineController.GetUsersTask();
            getUsersTask.execute("");
            userlist = getUsersTask.get();
        }
        return userlist;
    }

    public static class SearchUserListsTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            //Just list top 10000 users
            //String search_string = "\{\"from\": 0, \"size\": 10000}"
            //Replace with our indexes
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match\": {\"message\": \"" + search_parameters[0] + "\"}}}";

            // assume that search_parameters[0] is the only search term we are interested in using
            //Add Indexing and such
            Search search = new Search.Builder(search_string)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
            }
            catch (Exception e) {
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
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return users;
        }
    }

    public static class AddUsersTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                //Add Indexing and such
                Index index = new Index.Builder(user).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        users.setId(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the user.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public static class DeleteUsersTask extends AsyncTask<User, Void, Void> {

        //Need to fill
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();
        }
    }

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
}
