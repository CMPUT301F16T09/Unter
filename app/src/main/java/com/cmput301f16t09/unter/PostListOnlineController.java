package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * The Postlist Online controller for elastic search.
 */
public class PostListOnlineController {

    //Static variables for JestDroidClient and holding posts.
    private static JestDroidClient client;
    private static ArrayList<Post> arrayPostList = null;
    private static PostList postList = new PostList();

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
     * The type Search post lists task.
     * Searches for a matching type and keyword.
     */
    public static class SearchPostListsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Just list top 10000 posts with type and keyword
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match_phrase\": {\"" + search_parameters[0] + "\": \"" + search_parameters[1] + "\"}}}";

            //Add Indexing and type
            Search search = new Search.Builder(search_string)
                    .addIndex("t09test")
                    .addType("post")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPosts);
                }
                else {
                    Log.i("Error", "The search query failed to find any posts that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return posts;
        }
    }

    /**
     * The type get all posts from elastic search.
     */
    public static class GetPostsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Add Indexing and type
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("t09test")
                    .addType("post")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPosts);
                }
                else {
                    Log.i("Error", "The search query failed to find any posts that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return posts;
        }
    }

    /**
     * The type Add posts task to elastic search.
     */
    public static class AddPostsTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                //Add Indexing and type
                Index index = new Index.Builder(post).index("t09test").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //Adds a id if successfully added to elastic search
                        post.setId(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the post.");
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

    /**
     * The type Update posts task.
     */
    public static class UpdatePostsTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                //Add Indexing, type and id of post
                Index index = new Index.Builder(post).index("t09test").type("post").id(post.getId()).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to update the post.");
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

    /**
     * The type Delete posts task from elastic search.
     */
    public static class DeletePostsTask extends AsyncTask<Post, Void, Void> {

        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                // Adds index, type and id of post to be deleted.
                Delete index = new Delete.Builder(post.getId()).index("t09test").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to delete the post.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete the post from elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    /**
     * The type Delete posts task from elastic search.
     */
    public static class DeletePostsTaskId extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... posts) {
            verifySettings();

            for (String post: posts) {
                // Adds index, type and id of post to be deleted.
                Delete index = new Delete.Builder(post).index("t09test").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to delete the post.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete the post from elastic search!");
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

    // Idea: we call notifyUsers whenever we communicate with the server to notify user B
    // that user A has made changes to their post (e.g. Driver accepts Riders request)
    private static void notifyUsers(User user){

    }
}
