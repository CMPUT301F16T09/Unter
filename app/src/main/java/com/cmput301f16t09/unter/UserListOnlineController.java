package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * The type UserList Online Controller for elastic search.
 */
public class UserListOnlineController {

    //Static variables for JestDroidClient and holding users.
    private static JestDroidClient client;
    private static ArrayList<User> arrayUserList = null;
    private static UserList userList = new UserList();

    /**
     * Gets userList from elastic search
     *
     * @return the user list
     */
    static public UserList getUserList() {
        if (arrayUserList == null) {

            // GetsUsersTask to obtain users from elasticsearch
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

    /**
     * The type Search user lists task from elastic search.
     * Searches for specified type and keyword.
     */
    public static class SearchUserListsTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            //Just list top 10000 users with type and keyword
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match_phrase\": {\"" + search_parameters[0] + "\": \"" + search_parameters[1] + "\"}}}";

            //Add indexing and type
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

    /**
     * The type Get users task whih gets all users from elastic search.
     */
    public static class GetUsersTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            //Add Indexing and type.
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("t09")
                    .addType("user")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<User> foundUsers = (ArrayList<User>) result.getSourceAsObjectList(User.class);
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

    /**
     * The type Add users task which adds a user to elastic search.
     */
    public static class AddUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                //Add Indexing and type.
                Index index = new Index.Builder(user).index("t09").type("user").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //Adds a id if successfully added to elastic search
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

    /**
     * The type Update users task which updates a user on elastic search.
     */
    public static class UpdateUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                //Add Indexing, type and id.
                Index index = new Index.Builder(user).index("t09").type("user").id(user.getId()).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to update the user.");
                    }
                } catch (Exception e) {
                    Log.i("Uhoh", "We failed to update a user to elastic search!");
                    System.out.println("We failed to update a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    /**
     * Verifies settings of the elastic search server
     */
    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}