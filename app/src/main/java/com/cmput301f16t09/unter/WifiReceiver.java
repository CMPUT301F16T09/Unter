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

        if (netInfo != null && netInfo.getState().name().equals("CONNECTED")) {

            NotificationOnlineController.createNotifications(context);

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
            }
        }

        else {
            Log.d("WifiReceiver", "Don't have Wifi Connection");
            Toast.makeText(context, "WiFi Has Disconnected", Toast.LENGTH_SHORT).show();
        }


        final Context c = context;
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.d("Handlers", "Called on main thread");
                Toast.makeText(c, "periodic notification check", Toast.LENGTH_SHORT).show();
                NotificationOnlineController.createNotifications(c);
                handler.postDelayed(this, 10000);
            }
        };


        if (!(c instanceof MainGUIActivity)&(isNetworkAvailable(c))){
            handler.post(r);
            r.run();
        }

        if (!isNetworkAvailable(c)){
            handler.removeCallbacks(r);
        }


    } //on receive

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}


//https://guides.codepath.com/android/Repeating-Periodic-Tasks
//http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292

