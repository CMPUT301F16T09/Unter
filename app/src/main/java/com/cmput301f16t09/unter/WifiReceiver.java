package com.cmput301f16t09.unter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

// 2016-11-19
// http://stackoverflow.com/questions/6362314/wifi-connect-disconnect-listener
// Basic class for detecting WiFi connection/disconnection
// Author: warbi
public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        final Context c = context;

        // Create the Handler object (on the main thread by default)
         final Handler handler = new Handler();
        // Define the code block to be executed
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                Toast.makeText(c, "periodic notification check", Toast.LENGTH_SHORT).show();
                NotificationOnlineController.createNotifications(c);
                handler.postDelayed(this, 30000);
            }
        };

        if (netInfo != null && netInfo.getState().name().equals("CONNECTED")) {

            NotificationOnlineController.createNotifications(context);

            Log.d("WifiReceiver", "Have Wifi Connection");
            Toast.makeText(context, "WiFi Has Reconnected", Toast.LENGTH_SHORT).show();


            // Start the initial runnable task by posting through the handler
            handler.post(r);
            r.run();

            if (CurrentUser.getCurrentUser() != null) {

                for (Post p : PostListMainController.getPostListQueue(context).getPosts()) {
                    PostListOnlineController.AddPostsTask addPostOnline = new PostListOnlineController.AddPostsTask();
                    addPostOnline.execute(p);
                }
                // ADD CLEARING THE SAVE FILE
                PostListMainController.clearPostListQueue(context);

                for (Post p : PostListMainController.getPostListUpdate(context).getPosts()) {
                    PostListMainController.updatePosts(p, context);
                }
                // ADD CLEARING THE SAVE FILE
                PostListMainController.clearPostListUpdate(context);

                for (Post p : PostListMainController.getPostListDelete(context).getPosts()) {
                    PostListMainController.deletePosts(p, context);
                }
                // ADD CLEARING THE SAVE FILE
                PostListMainController.clearPostListDelete(context);
            }
        }

        else {
            Log.d("WifiReceiver", "Don't have Wifi Connection");
            Toast.makeText(context, "WiFi Has Disconnected", Toast.LENGTH_SHORT).show();
            handler.removeCallbacks(r);
        }
    }
};


//https://guides.codepath.com/android/Repeating-Periodic-Tasks
//http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292