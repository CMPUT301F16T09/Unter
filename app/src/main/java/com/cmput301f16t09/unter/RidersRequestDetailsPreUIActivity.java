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
        potentialDrivers = new ArrayList<String>();
        potentialDrivers.add("joker");
        potentialDrivers.add("kappaross");

        driversAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,potentialDrivers);
        potentialDriversListView.setAdapter(driversAdapter);

        potentialDriversListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int currDriver_pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RidersRequestDetailsPreUIActivity.this);
                //String driverUsername = CurrentUser.getCurrentPost().getDriverOffers().get(currDriver_pos).toString(); //unsure about this
                String driverUsername = potentialDrivers.get(currDriver_pos);


                builder.setMessage("Confirm Driver Selection: "+ driverUsername);
                builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
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
                return true;
            }
        });

        //short clicks go to that user's profile
        potentialDriversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int currDriver_pos, long l) {
               // String driverUsername = CurrentUser.getCurrentPost().getDriverOffers().get(currDriver_pos).toString(); //unsure about this
                String driverUsername = potentialDrivers.get(currDriver_pos);
                Intent intent = new Intent(RidersRequestDetailsPreUIActivity.this, ViewProfileUIActivity.class);
                intent.putExtra("Rider", driverUsername);
                startActivity(intent);
            }

        });

        //long click brigs up dialog to confirm driver selection


    }//end of onCreate
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
}//end of activity
