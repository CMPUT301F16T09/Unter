package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RidersRequestDetailsPreUIActivity extends AppCompatActivity {
    ListView potentialDriversListView;
    ArrayList<String> potentialDrivers;
    ArrayAdapter<String> driversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_pre_ui);

        potentialDriversListView = (ListView) findViewById(R.id.listViewRideRequestDetailsRiderOffers);
        //potentialDrivers = CurrentUser.getCurrentPost().getDriverOffers();
        potentialDrivers = new ArrayList<String>(); //test
        potentialDrivers.add("joker"); //test
        potentialDrivers.add("kappaross"); //test

        driversAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,potentialDrivers);
        potentialDriversListView.setAdapter(driversAdapter);

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


    }//end of onCreate
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
}//end of activity
