package com.cmput301f16t09.unter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// 2016-11-19
// http://stackoverflow.com/questions/6362314/wifi-connect-disconnect-listener
// Basic class for detecting WiFi connection/disconnection
// Author: warbi
public class WifiReceiver extends BroadcastReceiver {

    Geocoder coder;
    private static boolean prevConnected = true;
    final static Handler handler = new Handler();
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        coder = new Geocoder(context, Locale.getDefault());

        if (netInfo != null && netInfo.getState().name().equals("CONNECTED")) {
            if (!prevConnected) {
                NotificationOnlineController.createNotifications(context);

                Log.d("WifiReceiver", "Have Wifi Connection");
                Toast.makeText(context, "WiFi Has Reconnected", Toast.LENGTH_SHORT).show();

                if (CurrentUser.getCurrentUser() != null) {

                    for (Post p : PostListMainController.getPostListQueue(context).getPosts()) {
                        PostListOnlineController.AddPostsTask addPostOnline = new PostListOnlineController.AddPostsTask();
                        Post online = reverseGeocodeForOfflinePost(p);
                        addPostOnline.execute(online);
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
                prevConnected = true;
            }
            else {
                if (prevConnected) {
                    Log.d("WifiReceiver", "Don't have Wifi Connection");
                    Toast.makeText(context, "WiFi Has Disconnected", Toast.LENGTH_SHORT).show();

                    polling(context);

                    prevConnected = false;

                }
            }
        }
    }
    public static void polling(Context context) {
        final Context c = context;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if (WifiReceiver.isNetworkAvailable(c)) {
                    Log.d("Handlers", "Called on main thread");
                    Toast.makeText(c, "periodic notification check", Toast.LENGTH_SHORT).show();
                    NotificationOnlineController.createNotifications(c);
                }
                handler.postDelayed(this, 30000);
            }
        };

        if (WifiReceiver.isNetworkAvailable(context)) {
            handler.post(r);
        }
        if (!WifiReceiver.isNetworkAvailable(c)) {
            handler.removeCallbacks(r);
        }
    }
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public Post reverseGeocodeForOfflinePost(Post post){

        Post onlinePost = post;

        try {
            List<Address> start = coder.getFromLocation(post.getStartLocation().getLatitude(),
                                                         post.getStartLocation().getLongitude(), 1);

            List<Address> end = coder.getFromLocation(post.getEndLocation().getLatitude(),
                                                      post.getEndLocation().getLongitude(), 1);

            onlinePost.setStartAddress(start.get(0).getAddressLine(0) + ", " + start.get(0).getAddressLine(1));
            onlinePost.setEndAddress(end.get(0).getAddressLine(0) + ", " + end.get(0).getAddressLine(1));

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("IOException", "IOException was caught");
        }

        return onlinePost;
    }
}
