package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.aliases.GetAliases;

public class SearchEngine {
    //Static variables for JestDroidClient and holding posts.
    private static JestDroidClient client;
    private static ArrayList<Post> arrayPostList = null;
    private static PostList postList = new PostList();
    private static HashMap<String, Post> uniquePosts = new HashMap<>();
    /**
     * Gets post list from elastic search.
     *
     * @return the postList
     */
    static public PostList getPostList() {
        if (arrayPostList == null) {

            // GetsPostsTask to obtain posts from elasticsearch
            PostListOnlineController.GetPostsTask getPostsTask = new PostListOnlineController.GetPostsTask();
            getPostsTask.execute("");
            try {
                arrayPostList = getPostsTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            postList.setPostList(arrayPostList);
        }
        return postList;
    }

    /**
     * The type get all posts from elastic search.
     */
    public static class SearchKeyword extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            ArrayList<String> search_string = new ArrayList<>();
            String search_details = "{\"from\": 0, \"size\": 10000, \"query\": {\"multi_match\": {\"query\": \"" + search_parameters[0] + "\", \"type\": \"best_fields\", \"fields\": [\"user\", \"startAddress\", \"startLocation\", \"endAddress\", \"endLocation\", \"fare\"]}}}";
            search_string.add(search_details);
            String search_location = "{\"from\": 0, \"size\": 10000, \"query\": {\"multi_match\": {\"query\": \"" + search_parameters[0] + "\", \"type\": \"best_fields\", \"fields\": [\"startAddress.mLatitude\", \"startAddress.mLongitude\", \"endLocation.mLatitude\", \"endLocation.mLongitude\"]}}}";
            search_string.add(search_location);
            for (int i = 0; i < 2; i++) {
                //Add Indexing and type{"from": 0, "size": 10000, "query": {"multi_match": {"query": "home", "type": "best_fields", "fields": ["driver_OfferList","startAddress", "startLocation", "endAddress", "endLocation", "fare"]}}}
                Search search = new Search.Builder(search_string.get(i))
                        .addIndex("t09test2")
                        .addType("post")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                        for (Post p : foundPosts) {
                            if (!uniquePosts.containsKey(p.getId())) {
                                uniquePosts.put(p.getId(), p);
                                posts.add(p);
                            }
                        }
                    } else {
                        Log.i("Error", "The search query failed to find any posts that matched.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }

            return posts;
        }
    }

    public static class SearchLocation extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Add Indexing and type{"from": 0, "size": 10000, "query": {"multi_match": {"query": "40.7313023", "type": "best_fields", "fields": ["startAddress.mLatitude", "startAddress.mLongitude", "endLocation.mLatitude", "endLocation.mLongitude", "fare"]}}}
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"multi_match\": {\"query\": \"" + search_parameters[0] + "\", \"type\": \"best_fields\", \"fields\": [\"startAddress.mLatitude\", \"startAddress.mLongitude\", \"endLocation.mLatitude\", \"endLocation.mLongitude\"]}}}";
            Search search = new Search.Builder(search_string)
                    .addIndex("t09test2")
                    .addType("post")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPosts);
                } else {
                    Log.i("Error", "The search query failed to find any posts that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return posts;
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

    // Idea: we call notifyUsers whenever we communicate with the server to notify user B
    // that user A has made changes to their post (e.g. Driver accepts Riders request)
    private static void notifyUsers(User user){

    }
}
