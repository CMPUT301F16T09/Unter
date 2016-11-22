package com.cmput301f16t09.unter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private ArrayAdapter<Notification> adapter;
    private NotificationList notificationList;
    private ArrayList<Notification> templist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ListView notificationsLV = (ListView) findViewById(R.id.listViewNotifications);

//        NotificationOnlineController.findNotifications(NotificationsActivity.this);
        try {
            NotificationOnlineController.SearchNotificationListsTask searchNotificationListsTask = new NotificationOnlineController.SearchNotificationListsTask();
            searchNotificationListsTask.execute("username", CurrentUser.getCurrentUser().getUsername());
            ArrayList<Notification> templist = searchNotificationListsTask.get();
            //create notifications
        } catch (Exception e) {
            Log.i("Error", "Error trying to obtain notification");
            templist = new ArrayList<Notification>();
        }

        if (templist == null) {

        } else {
            if (!templist.isEmpty()) {
                notificationList.setNotificationList(templist);
                adapter = new ArrayAdapter<Notification>(this, android.R.layout.simple_list_item_1, notificationList.getNotificationList()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        String notification_message = notificationList.getNotificationList().get(position).getNotification();
                        tv.setText(notification_message);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(24);

                        return view;
                    }
                };
                notificationsLV.setAdapter(adapter);

                notificationsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Toast.makeText(NotificationsActivity.this, "Notification deleted.", Toast.LENGTH_SHORT).show();
                        try {
                            NotificationOnlineController.DeleteNotificationsTask deleteNotificationsTask = new NotificationOnlineController.DeleteNotificationsTask();
                            deleteNotificationsTask.execute(notificationList.getNotificationList().get(position));
                            deleteNotificationsTask.get();
                            notificationList.getNotificationList().remove(notificationList.getNotificationList().get(position));
                            adapter.notifyDataSetChanged();
                        } catch(Exception e) {
                            Log.i("Error", "Error trying to delete notification");
                        }
                    }
                });
            }
        }
    }

}

