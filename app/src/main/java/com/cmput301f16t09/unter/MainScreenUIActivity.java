package com.cmput301f16t09.unter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

/**
 * A view that allows the user to choose between rider activies or driver activities.
 */
public class MainScreenUIActivity extends AppCompatActivity {

//    Button requestButton = (Button) findViewById(R.id.RequestARideButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_ui);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.edit_profile) {
            Intent requestARideIntent = new Intent(MainScreenUIActivity.this, EditProfileUIActivity.class);
            startActivity(requestARideIntent);
            return true;
        }

        else if(item.getItemId() == R.id.refresh){


            NotificationController.findNotifications(MainScreenUIActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //The following methods bring the user to the appropriate activity upon selecting that button


    public void requestARide(View v){
        Intent intent = new Intent(MainScreenUIActivity.this, RequestARideUIActivity.class);
        startActivity(intent);
    }

    public void myRideRequests(View v){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, MyRideRequestsUIActivity.class);
        startActivity(requestARideIntent);
    }


    public void provideARide(View v){
//        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, ProvideARideUIActivity.class);
//        startActivity(requestARideIntent);

        try {
            SearchEngine.SearchKeyword sk = new SearchEngine.SearchKeyword();
            sk.execute("19.1033902");
            PostList pl = new PostList();
            pl.setPostList(sk.get());
            pl.getPosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void myRideOffers(View v){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, MyRideOffersUIActivity.class);
        startActivity(requestARideIntent);

//        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, RidersRequestDetailsPreUIActivity.class);
//        startActivity(requestARideIntent);
    }
}
