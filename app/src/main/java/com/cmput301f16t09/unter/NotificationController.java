package com.cmput301f16t09.unter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Kelly on 2016-11-21.
 */


//http://stackoverflow.com/questions/7040742/android-notification-manager-having-a-notification-without-an-intent
public class NotificationController {

    //query elastic search for updates
    public static void findNotifications(Context c){
        //any status changes are placed into current user notification list

        CurrentUser.getNotificationList().clear();
        CurrentUser.getNotificationList().add("Dummy notification");

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(c.getApplicationContext(),0,intent,0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c.getApplicationContext())
                        .setSmallIcon(R.drawable.bonuspack_bubble)
                        //  .setTicker("Ticker!!")
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }
}
