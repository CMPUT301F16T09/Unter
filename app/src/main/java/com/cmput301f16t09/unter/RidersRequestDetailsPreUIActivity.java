package com.cmput301f16t09.unter;

import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;

public class RidersRequestDetailsPreUIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_pre_ui);
        ListView potentialDriversListView = (ListView) findViewById(R.id.listViewRideRequestDetailsRiderOffers);

//        Get drivers instead of post

        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPreCurrentStatus);
        String currentStatus = CurrentUser.getCurrentPost().getStatus();
        tvCurrentStatus.setText(currentStatus);

        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPreStartLocationName);
        String startLocation = CurrentUser.getCurrentPost().getStartLocation().toString();
        tvStartLocation.setText(startLocation);

        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPreEndLocationName);
        String endLocation = CurrentUser.getCurrentPost().getEndLocation().toString();
        tvEndLocation.setText(endLocation);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPreOfferedFare);
        String offeredFare = CurrentUser.getCurrentPost().getFare();
        tvOfferedFare.setText(offeredFare);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrentUser.getCurrentPost().getDriverOffers()) {

            // Create the view for the habits. Habits name is red if it has not been completed before
            // and green if it has been completed.
            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setText(CurrentUser.getCurrentPost().getDriverOffers().get(position));
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };


        potentialDriversListView.setAdapter(adapter);
        //potentialDrivers = CurrentUser.getCurrentPost().getDriverOffers();

        potentialDriversListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RidersRequestDetailsPreUIActivity.this);
                final String driverUsername = CurrentUser.getCurrentPost().getDriverOffers().get(position); //unsure about this

                builder.setMessage("Actions for Driver:         "+ driverUsername);
                builder.setPositiveButton(R.string.choose_driver, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        CurrentUser.getCurrentPost().setStatus("Awaiting Completion");
                        CurrentUser.getCurrentPost().pickDriver(driverUsername);

                        try {
                            PostListOnlineController.UpdatePostsTask upt = new PostListOnlineController.UpdatePostsTask();
                            upt.execute(CurrentUser.getCurrentPost());
                            upt.get();

                            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
                            uut.execute(CurrentUser.getCurrentUser());
                            uut.get();

                            Toast.makeText(RidersRequestDetailsPreUIActivity.this, "OK", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();

                            Intent intent = new Intent(RidersRequestDetailsPreUIActivity.this,RidersRequestDetailsPostUIActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        catch (Exception e) {
                            Log.i("Error", "Unable to Update Post/User Information");
                        }
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

    public void cancelRequest(View v) {
        try {
            PostListOnlineController.DeletePostsTask upt = new PostListOnlineController.DeletePostsTask();
            upt.execute(CurrentUser.getCurrentPost());
            upt.get();

            CurrentUser.getCurrentUser().getMyOffers().deletePost(CurrentUser.getCurrentPost());

            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
            uut.execute(CurrentUser.getCurrentUser());
            uut.get();
            Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Successfully deleted the offer!", Toast.LENGTH_SHORT).show();

            finish();
        }
        catch (Exception e) {
            Log.i("Error", "Deletion Error");
        }
    }

}
