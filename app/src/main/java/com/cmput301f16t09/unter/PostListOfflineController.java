package com.cmput301f16t09.unter;

import android.content.Context;
import android.content.ContextWrapper;
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

/**
 * The type Post list offline controller.
 */
public class PostListOfflineController {
    private static PostList postlist = null;
    private static final String FILENAME = "real_offline_posts.sav";
    // Create a second file and store into that for offline queue?
    private static final String QUEUE_FILENAME = "queue_offline_posts.sav";
//    private static PostList postListQueue = new PostList();

    /**
     * Gets post list.
     *
     * @return the post list
     */
    static public PostList getPostList(Context context) {
        if (postlist == null) {
            postlist = new PostList();
        }
        try {
            PostListOnlineController.GetPostsTask onlinePosts = new PostListOnlineController.GetPostsTask();
            onlinePosts.execute("");
            postlist = new PostList();
            postlist.setPostList(onlinePosts.get());

            // If queue FILE offline list is not empty, add Posts to elastic search
            // Clear queue FILE

            saveOfflinePosts(context);
        }
        catch (Exception e) {
            // Add To Queue FILE
            loadOfflinePosts(context);
            Toast.makeText(context, "Cannot store posts", Toast.LENGTH_SHORT).show();
            Log.i("Error", "Loading failed");
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
        Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
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
        catch (FileNotFoundException f) {
            postlist = new PostList();
        }
        return postlist;
    }

    public static void saveOfflinePosts(Context context)
    {
        // Also write to queue FILE
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

    /**
     * Add offline post.
     *
     * @param offlinePost   the post
     * @param context       the context
     */
    public static void addOfflinePost(Post offlinePost, Context context) {

        getPostList(context).addPost(offlinePost);
        saveOfflinePosts(context);
}
}
