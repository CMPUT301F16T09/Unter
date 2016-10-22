package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class PostListOnlineController {
    private static JestDroidClient client;

    private static PostList postlist = null;

    /**
     * Gets post list.
     *
     * @return the post list
     */
    static public PostList getPostList() {
        if (postlist == null) {

            PostListOnlineController.GetPostsTask getPostsTask = new PostListOnlineController.GetPostsTask();
            getPostsTask.execute("");
            postlist = getPostsTask.get();
        }
        return postlist;
    }

    public static class SearchPostListsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Just list top 10000 posts
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
                    ArrayList<Post> foundPosts = result.getSourceAsObjectList(Post.class);
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
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<User> foundPosts = result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPost);
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
                Index index = new Index.Builder(post).build();

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

    public static class DeletePostsTask extends AsyncTask<Post, Void, Void> {

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
