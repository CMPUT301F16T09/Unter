package com.cmput301f16t09.unter;

import android.content.Context;
import android.content.ContextWrapper;
import android.test.mock.MockContext;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

/**
 * The type Post list offline controller.
 */
public class PostListOfflineController {
    private static PostList postlist = null;
    private static final String FILENAME = "offline_posts.sav";
    private static PostList offlinePostList = new PostList();

    /**
     * Gets post list.
     *
     * @return the post list
     */
    static public PostList getPostList() {
        if (postlist == null) {

            //Replace this with retrieval from location
            postlist = new PostList();
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
    public PostList loadOfflinePosts(Context context)
    {
        try
        {
            // Getting file directory code to ensure file is created from:
            // stackoverflow.com/questions/5017292/how-to-create-a-file-on-android-internal-storage
            ContextWrapper cw = new ContextWrapper(context);
            File dir = cw.getDir(FILENAME, context.MODE_PRIVATE);

            // Open the file stream and buffer to load the data from FILENAME
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader br_in = new BufferedReader(new InputStreamReader(fis));

            // Instantiate Gson
            Gson gson = new Gson();

            // Type that Gson will convert the Json to
            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();

            // Load the data into the global variable offlinePostList
            offlinePostList = gson.fromJson(br_in, listType);
        }

        // If there is no file, return error.
        catch (FileNotFoundException e) {
            Log.i("Error", "No Habits");
        }
        return offlinePostList;
    }

    private void saveOfflinePosts(Context context)
    {
        try {
            // Open the FileOutputStream and BufferedWriter to write to FILENAME
            FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            // Instantiate Gson
            Gson gson = new Gson();

            // Write the data in hlist to BufferedWriter
            gson.toJson(offlinePostList, bw);

            // Flush the buffer to prevent memory leakage and close the OutputStream
            bw.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add offline post.
     *
     * @param startLocation the start location
     * @param endLocation   the end location
     * @param fare          the fare
     * @param context       the context
     */
    public void addOfflinePost(Location startLocation, Location endLocation, String fare, Context context) {
        Post offlinePost = new Post(startLocation, endLocation, fare);
        getPostList().addPost(offlinePost);
        offlinePostList.addPost(offlinePost);
        saveOfflinePosts(context);
    }

}
