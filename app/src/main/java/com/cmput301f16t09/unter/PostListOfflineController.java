package com.cmput301f16t09.unter;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.os.Environment;
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
    private static final String FILENAME = "real_offline_posts.sav";
    private static final String QUEUE_FILENAME = "queue_offline_posts.sav";
    private static final String UPDATE_FILENAME = "update_offline_posts.sav";
    private static final String DELETE_FILENAME = "delete_offline_posts.sav";

    /**
     * Load offline posts to postlist (file saved on real_offline_posts.sav).
     *
     * @param context the context of the activity
     * @return the post list
     */
    public static PostList loadOfflinePosts(String saveType, PostList postList, Context context)
    {
        Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
        File dir;
        FileInputStream fis;
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

            // If the string is queueOffline, then the data is stored in an offline queue, otherwise
            // it is stored to the main list ("mainOffline")
            if (saveType.equals("queueOffline")) {
                String path = Environment.getExternalStorageDirectory() + "/" + QUEUE_FILENAME;
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
//                dir = cw.getDir(QUEUE_FILENAME, context.MODE_PRIVATE);
                fis = new FileInputStream(path);
            }

            else if(saveType.equals("updateOffline")) {
                String path = Environment.getExternalStorageDirectory() + "/" + UPDATE_FILENAME;
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
//                dir = cw.getDir(UPDATE_FILENAME, context.MODE_PRIVATE);
                fis = context.openFileInput(path);
            }

            else if(saveType.equals("deleteOffline")) {
                String path = Environment.getExternalStorageDirectory() + "/" + DELETE_FILENAME;
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
//                dir = cw.getDir(DELETE_FILENAME, context.MODE_PRIVATE);
                fis = context.openFileInput(path);
            }

            else {
                String path = Environment.getExternalStorageDirectory() + "/" + FILENAME;
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
//                dir = cw.getDir(FILENAME, context.MODE_PRIVATE);
                fis = context.openFileInput(path);

            }


            // Open the file stream and buffer to load the data from FILENAME
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
            postList.setPostList(tempList);

            br_in.close();
            fis.close();
        }

        // If there is no file, return error and create empty postlist
        catch (FileNotFoundException f) {
            Log.e("Error", "Could not load offline posts");
            postList = new PostList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postList;
    }

    /**
     * Static method to save posts to a file for offline retrieval
     *
     * @param context the context of the activity
     */
    public static void saveOfflinePosts(String saveType, PostList postList, Context context)
    {
        FileOutputStream fos;
        /**
         * Try to open the file and write to it
         * @see FileOutputStream
         * @see BufferedWriter
         * @see Gson
         * @throws IOException that file cannot be opened or written to
         */
        try {
            if (saveType.equals("queueOffline")) {
                String path = Environment.getExternalStorageDirectory() + "/" + QUEUE_FILENAME;
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
                fos = new FileOutputStream(path);
            }

            else if(saveType.equals("updateOffline")) {
                String path = Environment.getExternalStorageDirectory() + "/" + UPDATE_FILENAME;
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
                fos = context.openFileOutput(Environment.getExternalStorageDirectory() +
                        "/" + UPDATE_FILENAME, context.MODE_PRIVATE);
            }

            else if(saveType.equals("deleteOffline")) {
                File f = new File(Environment.getExternalStorageDirectory() +
                        "/" + UPDATE_FILENAME);
                if (!f.exists()) {
                    f.createNewFile();
                }
                fos = context.openFileOutput(Environment.getExternalStorageDirectory() +
                        "/" + DELETE_FILENAME, context.MODE_PRIVATE);
            }

            else {
                File f = new File(Environment.getExternalStorageDirectory() +
                        "/" + FILENAME);
                if (!f.exists()) {
                    f.createNewFile();
                }
                fos = context.openFileOutput(Environment.getExternalStorageDirectory() +
                        "/" + FILENAME, context.MODE_PRIVATE);
            }
            // Open the FileOutputStream and BufferedWriter to write to FILENAME
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            // Instantiate Gson
            Gson gson = new Gson();

            // Write the data in list to BufferedWriter
            gson.toJson(postList.getPosts(), bw);

            // Flush the buffer to prevent memory leakage and close the OutputStream
            bw.flush();

            bw.close();
            fos.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
