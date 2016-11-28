package com.cmput301f16t09.unter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The type Notifications activity.
 */
public class NotificationsActivity extends AppCompatActivity {

    private ArrayAdapter<Notification> adapter;
    private ArrayList<Notification> tempList = new ArrayList<Notification>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        final ListView notificationsLV = (ListView) findViewById(R.id.listViewNotifications);

        // Find the notifications for the user
        NotificationOnlineController.findNotifications();
        tempList = NotificationOnlineController.getList();

        // Set the notifications in the ListView Adapter if there are any for the user
        if (!tempList.isEmpty()) {
            adapter = new ArrayAdapter<Notification>(this, android.R.layout.simple_list_item_1, tempList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    String notification_message = NotificationOnlineController.getList().get(position).getNotification();
                    tv.setText(notification_message);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);

                    return view;
                }
            };
            notificationsLV.setAdapter(adapter);

        }

        //The refresh button queries elastic search separate from the periodic queries
        Button refresh = (Button) findViewById(R.id.RefreshButton);

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(NotificationsActivity.this, "Searching online for notifications..", Toast.LENGTH_SHORT).show();
                NotificationOnlineController.findNotifications();
            }
        });

        //clearAllNotifications calls the deleteNotificationTask individually
        Button clearAllNotifcations = (Button) findViewById(R.id.ClearAllButton);
        clearAllNotifcations.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tempList.isEmpty()){
                Toast.makeText(NotificationsActivity.this, "No notifications to delete!", Toast.LENGTH_SHORT).show();
                }
                for (Notification n : tempList){
                    try {
                        NotificationOnlineController.DeleteNotificationsTask deleteNotificationsTask = new NotificationOnlineController.DeleteNotificationsTask();
                        deleteNotificationsTask.execute(n);
                        deleteNotificationsTask.get();
                        tempList.remove(n);
                        adapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        Log.i("Error", "Error trying to delete notification");
                    }
                }
            }
        });

        // If notificationsLV isn't null, set listeners for them
        if (notificationsLV != null) {
            notificationsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public  boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Toast.makeText(NotificationsActivity.this, "Notification deleted.", Toast.LENGTH_SHORT).show();
                    try {
                        NotificationOnlineController.DeleteNotificationsTask deleteNotificationsTask = new NotificationOnlineController.DeleteNotificationsTask();
                        deleteNotificationsTask.execute(tempList.get(position));
                        deleteNotificationsTask.get();
                        tempList.remove(tempList.get(position));
                        adapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        Log.i("Error", "Error trying to delete notification");
                    }
                    return true;
                }
            });


            //clicking on the notification will bring them to either myRideRequests or MyRideOffers for more detail
            notificationsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public  void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if (tempList.get(position).getPostType().equals("offer")){
                        Intent intent = new Intent(NotificationsActivity.this,MyRideOffersUIActivity.class);
                        startActivity(intent);
                    }
                    else if (tempList.get(position).getPostType().equals("request")){
                        Intent intent = new Intent(NotificationsActivity.this,MyRideRequestsUIActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Log.i("Error","Notification post type was not set correctly");
                    }
                }
            });
        }
    }

}


