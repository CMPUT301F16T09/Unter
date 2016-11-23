package com.cmput301f16t09.unter;

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

public class NotificationsActivity extends AppCompatActivity {

    private ArrayAdapter<Notification> adapter;
    private ArrayList<Notification> templist = new ArrayList<Notification>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ListView notificationsLV = (ListView) findViewById(R.id.listViewNotifications);

        NotificationOnlineController.findNotifications();
        templist = NotificationOnlineController.getList();


            if (!templist.isEmpty()) {
//                notificationList.setNotificationList(templist);
                adapter = new ArrayAdapter<Notification>(this, android.R.layout.simple_list_item_1, templist) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        String notification_message = templist.get(position).getNotification();
                        tv.setText(notification_message);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);

                        return view;
                    }
                };
                notificationsLV.setAdapter(adapter);


                Button refresh = (Button) findViewById(R.id.RefreshButton);

                refresh.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click

                        NotificationOnlineController.findNotifications();
                    }
                });
            }
//                notificationsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        Toast.makeText(NotificationsActivity.this, "Notification deleted.", Toast.LENGTH_SHORT).show();
//                        try {
//                            NotificationOnlineController.DeleteNotificationsTask deleteNotificationsTask = new NotificationOnlineController.DeleteNotificationsTask();
//                            deleteNotificationsTask.execute(templist.get(position));
//                            deleteNotificationsTask.get();
//                            templist.remove(templist.get(position));
//                            adapter.notifyDataSetChanged();
//                        } catch(Exception e) {
//                            Log.i("Error", "Error trying to delete notification");
//                        }
//                    }
//                });
           // }

    }

}

