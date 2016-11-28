package com.cmput301f16t09.unter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * This class moniters network connectivity changes and handles notification polling upon these changes
 * in addition to managing the online and offline controllers
 */
public class WifiReceiver extends BroadcastReceiver {

    // Referenced on 2016-11-19
    // http://stackoverflow.com/questions/6362314/wifi-connect-disconnect-listener
    // Basic class for detecting WiFi connection/disconnection
    // Author: warbi
    private static boolean prevConnected = true;
    /**
     * The constant handler.
     */
    final static Handler handler = new Handler();

    // Overriding onReceive for when Wifi signal change is registered
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        // Checks if the internet is connected or not
        if (netInfo != null && netInfo.getState().name().equals("CONNECTED")) {
            if (!prevConnected) {
                NotificationOnlineController.createNotifications(context);

                // Log and toast for wifi connection
                Log.d("WifiReceiver", "Have Wifi Connection");
                Toast.makeText(context, "WiFi Has Reconnected", Toast.LENGTH_SHORT).show();

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

                    // Start polling
                    polling(context);
                }
                prevConnected = true;
            }
        }

        else {
            if (prevConnected) {
                // Log and toast if Wifi disconnected
                Log.d("WifiReceiver", "Don't have Wifi Connection");
                Toast.makeText(context, "WiFi Has Disconnected", Toast.LENGTH_SHORT).show();

                // Start polling
                polling(context);
                prevConnected = false;
            }
        }
    }

    /**
     * Is network available boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * This is the function to be called to initialize periodic notification queries
     *
     * @param context the context
     */
    public static void polling(Context context) {
        //Referenced on Nov 26 2016
        //https://guides.codepath.com/android/Repeating-Periodic-Tasks
        //http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292
        //Author: Kalpa Pathum Welivitigoda

        // Create a runnable to poll for wifi
        final Context c = context;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
            if (WifiReceiver.isNetworkAvailable(c)) {
                // Log the handler check
                Log.d("Handlers", "Called on main thread");
                Toast.makeText(c, "periodic notification check", Toast.LENGTH_SHORT).show();
                NotificationOnlineController.createNotifications(c);
            }
            handler.postDelayed(this, 30000);
            }
        };

        // Checks if the Network is available
        if (WifiReceiver.isNetworkAvailable(context)) {
            handler.post(r);
        }
        if (!WifiReceiver.isNetworkAvailable(c)) {
            handler.removeCallbacks(r);
        }
    }
}

