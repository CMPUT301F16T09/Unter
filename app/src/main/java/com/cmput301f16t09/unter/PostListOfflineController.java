package com.cmput301f16t09.unter;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The PostListOfflineController deals with saving and retrieving posts to/from file stored on
 * hard disk. It calls PostListOnlineController to retrieve the most updated information from
 * elasticsearch whenever the method getPostList is called, if the device is online. If the device
 * is offline, then retrieve data from the saved file.
 * @author Daniel
 */
public class PostListOfflineController {
    private static PostList postlist = null;
    private static final String FILENAME = "real_offline_posts.sav";
    private static final String QUEUE_FILENAME = "queue_offline_posts.sav";
    private static PostList postListQueue = new PostList();

    /**
     * Gets post list.
     *
     * @param context the context of the activity
     * @return the post list
     */
    static public PostList getPostList(Context context) {
        /**
         * Create a new list if the list has not been initialized
         * @see PostList
         */
        if (postlist == null) {
            postlist = new PostList();
        }

        /**
         * Try to fetch posts from elasticsearch server using PostListOnlineController. Also saves
         * posts to offline file (real_offline_posts.sav)
         * @see PostListOnlineController
         * @see #saveOfflinePosts(Context)
         */
        try {
            if (isNetworkAvailable(context)) {
                PostListOnlineController.GetPostsTask onlinePosts = new PostListOnlineController.GetPostsTask();
                onlinePosts.execute("");
                // App crashes when using postlist.getposts().clear(), or if there is no list clearing statement
                postlist = new PostList();
                postlist.setPostList(onlinePosts.get(1000, TimeUnit.MILLISECONDS));
                saveOfflinePosts(context);
            }
            else {
                loadOfflinePosts(context);
            }
        }

        /**
         * If fetching from elasticsearch fails, read data from file (offline behaviour)
         * @param e
         * @see #loadOfflinePosts(Context)
         */
        //
        catch (Exception e) {
            // Add To Queue FILE
            loadOfflinePosts(context);
            Toast.makeText(context, "Cannot load posts from elasticsearch", Toast.LENGTH_SHORT).show();
            Log.i("Error", "Loading failed");
        }
        return postlist;
    }

    /**
     * Load offline posts to postlist (file saved on real_offline_posts.sav).
     *
     * @param context the context of the activity
     * @return the post list
     */
    public static PostList loadOfflinePosts(Context context)
    {
        Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();

        /**
         * Try to load in files using Gson
         * @see Gson
         * @see ContextWrapper
         * @see File
         * @see FileInputStream
         * @see BufferedReader
         * @see JsonReader
         */
        try
        {
            // 2016-11-14
            // Getting file directory code to ensure file is created from:
            // stackoverflow.com/questions/5017292/how-to-create-a-file-on-android-internal-storage
            // Author: Audrius
            ContextWrapper cw = new ContextWrapper(context);
            File dir = cw.getDir(FILENAME, context.MODE_PRIVATE);

            // Open the file stream and buffer to load the data from FILENAME
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader br_in = new BufferedReader(new InputStreamReader(fis));

            // Instantiate Gson
            Gson gson = new Gson();

            // 2016-11-14
            // Type that Gson will convert the Json to
            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // Author: Alex
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();

            // Allows Json reading to be more flexible to avoid crashing
            JsonReader reader = new JsonReader(br_in);
            reader.setLenient(true);

            // save the posts from Gson into an ArrayList of Posts
             ArrayList<Post> tempList = gson.fromJson(br_in, listType);

            // Store the data into the main PostList
            postlist.setPostList(tempList);
        }

        // If there is no file, return error and create empty postlist
        catch (FileNotFoundException f) {
            Log.e("Error", "Could not load offline posts");
            postlist = new PostList();
        }
        return postlist;
    }

    /**
     * Static method to save posts to a file for offline retrieval
     *
     * @param context the context of the activity
     */
    public static void saveOfflinePosts(Context context)
    {
        /**
         * Try to open the file and write to it
         * @see FileOutputStream
         * @see BufferedWriter
         * @see Gson
         * @throws IOException that file cannot be opened or written to
         */
        try {
        // Open the FileOutputStream and BufferedWriter to write to FILENAME
        FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        // Instantiate Gson
        Gson gson = new Gson();

        // Write the data in list to BufferedWriter
        gson.toJson(postlist.getPosts(), bw);

        // Flush the buffer to prevent memory leakage and close the OutputStream
        bw.flush();
        fos.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add offline post.
     *
     * @param offlinePost the post
     * @param context     the context of the activity
     */
    public static void addOfflinePost(Post offlinePost, Context context) {
        /**
         * Get the post list and add the post to elastic search (if online) and save to offline file
         * @see #getPostList(Context)
         * @see #saveOfflinePosts(Context)
         * @see PostList
         */
        if (!isNetworkAvailable(context)) {
            loadOfflineQueuePosts(context);
            postListQueue.addPost(offlinePost);
            saveOfflineQueuePosts(context);
        }

        getPostList(context).addPost(offlinePost);
        saveOfflinePosts(context);
    }

    // 2016-11-15
    // http://stackoverflow.com/questions/9570237/android-check-internet-connection
    // Author: Jared Burrows
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void saveOfflineQueuePosts(Context context)
    {
        /**
         * Try to open the file and write to it
         * @see FileOutputStream
         * @see BufferedWriter
         * @see Gson
         * @throws IOException that file cannot be opened or written to
         */
        try {
            // Open the FileOutputStream and BufferedWriter to write to FILENAME
            FileOutputStream fos = context.openFileOutput(QUEUE_FILENAME, context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            // Instantiate Gson
            Gson gson = new Gson();

            // Write the data in list to BufferedWriter
            gson.toJson(postListQueue.getPosts(), bw);

            // Flush the buffer to prevent memory leakage and close the OutputStream
            bw.flush();
            fos.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PostList loadOfflineQueuePosts(Context context)
    {
        Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();

        /**
         * Try to load in files using Gson
         * @see Gson
         * @see ContextWrapper
         * @see File
         * @see FileInputStream
         * @see BufferedReader
         * @see JsonReader
         */
        try
        {
            // 2016-11-14
            // Getting file directory code to ensure file is created from:
            // stackoverflow.com/questions/5017292/how-to-create-a-file-on-android-internal-storage
            // Author: Audrius
            ContextWrapper cw = new ContextWrapper(context);
            File dir = cw.getDir(QUEUE_FILENAME, context.MODE_PRIVATE);

            // Open the file stream and buffer to load the data from FILENAME
            FileInputStream fis = context.openFileInput(QUEUE_FILENAME);
            BufferedReader br_in = new BufferedReader(new InputStreamReader(fis));

            // Instantiate Gson
            Gson gson = new Gson();

            // 2016-11-14
            // Type that Gson will convert the Json to
            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // Author: Alex
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();

            // Allows Json reading to be more flexible to avoid crashing
            JsonReader reader = new JsonReader(br_in);
            reader.setLenient(true);

            // save the posts from Gson into an ArrayList of Posts
            ArrayList<Post> tempList = gson.fromJson(br_in, listType);

            // Store the data into the main PostList
            postListQueue.setPostList(tempList);
        }

        // If there is no file, return error and create empty postlist
        catch (FileNotFoundException f) {
            Log.e("Error", "Could not load offline posts");
            postlist = new PostList();
        }
        return postListQueue;
    }
}
