package com.cmput301f16t09.unter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salim Simon Akl on 2016-11-09.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String FIREBASE_URL = " unter-8d7e0.firebaseio.com";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FROM:" + remoteMessage.getFrom());

        //Check if the message contains data
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        //Check if the message contains notification

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Dispay the notification
     *
     * @param body
     */
    private void sendNotification(String body) {

        Intent intent = new Intent(this, MainGUIActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Unter")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
    }

    public static void sendNotificationToUser(User user, final String message) {
        Firebase ref = new Firebase(FIREBASE_URL);
        final Firebase notifications = ref.child("notificationRequests");

        Map notification = new HashMap<>();
        notification.put("username", user.getUsername());
        notification.put("message", message);

        notifications.push().setValue(notification);
    }
}