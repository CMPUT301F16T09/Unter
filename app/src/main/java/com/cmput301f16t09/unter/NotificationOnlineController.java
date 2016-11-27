package com.cmput301f16t09.unter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Kelly on 2016-11-21.
 */


//http://stackoverflow.com/questions/7040742/android-notification-manager-having-a-notification-without-an-intent
public class NotificationOnlineController {

    //Static variables for JestDroidClient and holding notificationss.
    private static JestDroidClient client;
    private static ArrayList<Notification> arrayNotificationList = null;
    static final private ArrayList<Notification> list = new ArrayList<Notification>();
//    private static int mID = 0;

    public static ArrayList<Notification> getList(){
        return list;
    }

    public static void findNotifications(){
        //any status changes are placed into notification list

        list.clear();

        try {
            NotificationOnlineController.SearchNotificationListsTask searchNotificationListsTask = new NotificationOnlineController.SearchNotificationListsTask();
            searchNotificationListsTask.execute("username", CurrentUser.getCurrentUser().getUsername());
            list.addAll(searchNotificationListsTask.get());

        } catch (Exception e) {
            Log.i("Error", "Error trying to obtain notification");
        }
    }

    public static void createNotifications(Context c){

        findNotifications();

        Intent resultIntent = new Intent(c,NotificationsActivity.class);
        //resultIntent.setAction(Intent.ACTION_MAIN);
        // resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(c.getApplicationContext(), 0, resultIntent, 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c.getApplicationContext())
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_menu_compass)
                        .setContentTitle("Unter")
                        .setContentText("There has been a status change!")
                ;

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        //http://stackoverflow.com/questions/16885706/click-on-notification-to-go-current-activity
        //David Wasser, Nov 22 2016
        if (list.isEmpty()){
            //Toast.makeText(c, "No new notifications!", Toast.LENGTH_SHORT).show();
        }
        else {
           // for (Notification n : templist) {
                mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There are " +  list.size() + " new updates to your open posts.")
                );
                android.app.Notification N = mBuilder.build();
                mNotificationManager.notify(0, N);
           // }
        }

    }



    /**
     * The type Search notification lists task from elastic search.
     * Searches for specified type and keyword.
     */
    public static class SearchNotificationListsTask extends AsyncTask<String, Void, ArrayList<Notification>> {
        @Override
        protected ArrayList<Notification> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Notification> notifications = new ArrayList<Notification>();

            //Just list top 10000 notifications with type and keyword
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match_phrase\": {\"" + search_parameters[0] + "\": \"" + search_parameters[1] + "\"}}}";

            //Add indexing and type
            Search search = new Search.Builder(search_string)
                    .addIndex("t09test22")
                    .addType("notification")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Notification> foundNotifications = (ArrayList<Notification>) result.getSourceAsObjectList(Notification.class);
                    notifications.addAll(foundNotifications);
                } else {
                    Log.i("Error", "The search query failed to find any notifications that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return notifications;
        }
    }

    /**
     * The type Add notifications task which adds a notification to elastic search.
     */
    public static class AddNotificationsTask extends AsyncTask<Notification, Void, Void> {

        @Override
        protected Void doInBackground(Notification... notifications) {
            verifySettings();

            for (Notification notification : notifications) {
                //Add Indexing and type.
                Index index = new Index.Builder(notification).index("t09test22").type("notification").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //Adds a id if successfully added to elastic search
                        notification.setId(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the notification.");
                        System.out.println("Elastic search was not able to add the notification.");
                    }
                } catch (Exception e) {
                    Log.i("Uhoh", "We failed to add the notification to elastic search!");
                    System.out.println("We failed to add the notification to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    /**
     * The type Delete notifications task from elastic search.
     */
    public static class DeleteNotificationsTask extends AsyncTask<Notification, Void, Void> {

        @Override
        protected Void doInBackground(Notification... notifications) {
            verifySettings();

            for (Notification notification: notifications) {
                // Adds index, type and id of notification to be deleted.
                Delete index = new Delete.Builder(notification.getId()).index("t09test22").type("notification").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to delete the notification.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete the notification from elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Verifies settings of the elastic search server
     */
    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }


}
