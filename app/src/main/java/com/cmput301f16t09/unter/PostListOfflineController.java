package com.cmput301f16t09.unter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.location.Location;
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
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * The type Post list offline controller.
 */
public class PostListOfflineController {
    private static PostList postlist = null;
    private static final String FILENAME = "real_offline_posts.sav";
    // Create a second file and store into that for offline queue?
//    private static PostList postListQueue = new PostList();

    /**
     * Gets post list.
     *
     * @return the post list
     */
    static public PostList getPostList(Context context) {
        try {
            postlist.getPosts().clear();

            PostListOnlineController.GetPostsTask onlinePosts = new PostListOnlineController.GetPostsTask();
            onlinePosts.execute("");

            try {
                postlist.setPostList(onlinePosts.get());
                saveOfflinePosts(context);
            }
            catch (Exception e) {
                loadOfflinePosts(context);
                Toast.makeText(context, "Cannot store posts", Toast.LENGTH_SHORT).show();
                Log.i("Error", "Loading failed");
            }
        }
        catch (Exception e) {
            postlist = new PostList();
            Log.i("Error", "New Post List Created");
        }

        return postlist;
    }

    /**
     * Load offline posts post list.
     *
     * @param context the context
     * @return the post list
     */
// Function to load stored data in FILENAME
    public static PostList loadOfflinePosts(Context context)
    {
        try
        {
            // Getting file directory code to ensure file is created from:
            // stackoverflow.com/questions/5017292/how-to-create-a-file-on-android-internal-storage
            ContextWrapper cw = new ContextWrapper(context);
            File dir = cw.getDir(FILENAME, context.MODE_PRIVATE);
//            new FileOutputStream(FILENAME, false).close();
            // Open the file stream and buffer to load the data from FILENAME
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader br_in = new BufferedReader(new InputStreamReader(fis));

            // Instantiate Gson
            Gson gson = new Gson();

            // Type that Gson will convert the Json to
            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();

            JsonReader reader = new JsonReader(br_in);
            reader.setLenient(true);
            ArrayList<Post> tempList = gson.fromJson(br_in, listType);

            // Load the data into the global variable postListQueue
            postlist.setPostList(tempList);
        }

        // If there is no file, return error.
        catch (FileNotFoundException e) {
            Log.i("Error", "No Habits");
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        return postlist;
    }

    public static void saveOfflinePosts(Context context)
    {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addOfflinePost(Post offlinePost, Context context) {

        postlist.addPost(offlinePost);
        saveOfflinePosts(context);
    }
}
