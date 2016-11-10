package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class PostListOnlineController {
    private static JestDroidClient client;

    private static ArrayList<Post> arrayPostList = null;

    private static PostList postList = new PostList();

    /**
     * Gets post list.
     *
     * @return the post list
     */
    static public PostList getPostList() {
        if (arrayPostList == null) {

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

    public static class SearchPostListsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Just list top 10000 posts
            //String search_string = "\{\"from\": 0, \"size\": 10000}"
            //Replace with our indexes
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match\": {\"" + search_parameters[0] + "\": \"" + search_parameters[1] + "\"}}}";

            // assume that search_parameters[0] is the only search term we are interested in using
            //Add Indexing and such
            Search search = new Search.Builder(search_string)
                    .addIndex("t09")
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

    public static class GetPostsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            // assume that search_parameters[0] is the only search term we are interested in using
            //Add Indexing and such
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("t09")
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

    public static class AddPostsTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                //Add Indexing and such
                Index index = new Index.Builder(post).index("t09").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
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

    public static class UpdatePostsTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                //Add Indexing and such
                Index index = new Index.Builder(post).index("t09").type("post").id(post.getId()).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to update the post.");
                        System.out.println("Elastic search was not able to update the post.");
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

    public static class DeletePostsTask extends AsyncTask<Post, Void, Void> {

        //Need to fill
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();
            return null;
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

    // Idea: we call notifyUsers whenever we communicate with the server to notify user B
    // that user A has made changes to their post (e.g. Driver accepts Riders request)
    private static void notifyUsers(User user){
        
    }
}
