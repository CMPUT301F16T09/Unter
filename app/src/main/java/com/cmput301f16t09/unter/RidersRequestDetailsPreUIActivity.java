package com.cmput301f16t09.unter;

import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RidersRequestDetailsPreUIActivity extends AppCompatActivity {
    ArrayList<String> potentialDrivers;

    private PostList postList = new PostList();
    private ListView potentialDriversListView;
    ArrayAdapter<String> driversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_pre_ui);
        potentialDriversListView = (ListView) findViewById(R.id.listViewMyRideRequests);

        // Get All posts for the specific user
        for(Post p : PostListOfflineController.getPostList(RidersRequestDetailsPreUIActivity.this).getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

//        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPreCurrentStatus);
//        String currentStatus = CurrentUser.getCurrentPost().getStatus().toString();
//        tvCurrentStatus.setText(currentStatus);
//
//        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPreStartLocationName);
//        String startLocation = CurrentUser.getCurrentPost().getStartLocation().toString();
//        tvStartLocation.setText(startLocation);
//
//        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPreEndLocationName);
//        String endLocation = CurrentUser.getCurrentPost().getEndLocation().toString();
//        tvEndLocation.setText(endLocation);
//
//        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPreOfferedFare);
//        String offeredFare = CurrentUser.getCurrentPost().getFare().toString();
//        tvOfferedFare.setText(offeredFare);

//        final ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {
//
//            // Create the view for the habits. Habits name is red if it has not been completed before
//            // and green if it has been completed.
//            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(android.R.id.text1);
//
//                String startLocation = postList.getPost(position).getStartLocation().toString();
//                String endLocation = postList.getPost(position).getEndLocation().toString();
//                // Remove forTestUsername after
////                String forTestUsername = postList.getPost(position).getDriverOffers();
////                tv.setText("Username: " + forTestUsername);
////                tv.setText(postList.getPost(position).getUsername());
//                tv.setTextColor(Color.WHITE);
//                return view;
//            }
//        };

        driversAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,potentialDrivers);

        potentialDriversListView.setAdapter(driversAdapter);
        //potentialDrivers = CurrentUser.getCurrentPost().getDriverOffers();
        potentialDrivers = new ArrayList<String>(); //test
        potentialDrivers.add("joker"); //test
        potentialDrivers.add("kappaross"); //test


        potentialDriversListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int currDriver_pos = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(RidersRequestDetailsPreUIActivity.this);
                //String driverUsername = CurrentUser.getCurrentPost().getDriverOffers().get(currDriver_pos).toString(); //unsure about this
                final String driverUsername = potentialDrivers.get(currDriver_pos);

                builder.setMessage("Actions for Driver:         "+ driverUsername);
                builder.setPositiveButton(R.string.choose_driver, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Toast.makeText(RidersRequestDetailsPreUIActivity.this, "OK", Toast.LENGTH_SHORT).show();

                        //update postlist here
                    }
                });

                builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Cancel", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNeutralButton(R.string.view_profile, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // String driverUsername = CurrentUser.getCurrentPost().getDriverOffers().get(currDriver_pos).toString(); //unsure about this
                        // String driverUsername = potentialDrivers.get(currDriver_pos);
                        Intent intent = new Intent(RidersRequestDetailsPreUIActivity.this, ViewProfileUIActivity.class);
                        intent.putExtra("User", driverUsername);
                        startActivity(intent);

                        Toast.makeText(RidersRequestDetailsPreUIActivity.this, "viewing profile", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();
                return true;
            }
        });


    }

}
