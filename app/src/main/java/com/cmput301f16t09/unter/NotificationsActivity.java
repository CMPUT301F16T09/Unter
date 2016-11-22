package com.cmput301f16t09.unter;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NotificationsActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ListView notificationsLV = (ListView) findViewById(R.id.listViewNotifications);

        NotificationController.findNotifications(NotificationsActivity.this);

        //create notifications

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrentUser.getNotificationList());

        notificationsLV.setAdapter(adapter);
    }

}

