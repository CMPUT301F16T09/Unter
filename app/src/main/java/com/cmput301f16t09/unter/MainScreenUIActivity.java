package com.cmput301f16t09.unter;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

/**
 * A view that allows the user to choose between rider activies or driver activities.
 */
public class MainScreenUIActivity extends AppCompatActivity {

//    Button requestButton = (Button) findViewById(R.id.RequestARideButton);
    WifiReceiver connected;
    HandlerThread handlerThread = new HandlerThread("WiFiConnectionThread");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_ui);

        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        if (handlerThread.isAlive()) {
            Log.i("WiFiConnectionThread","Working");}
        connected = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(connected, intentFilter, null, handler);

        NotificationOnlineController.createNotifications(MainScreenUIActivity.this);
        WifiReceiver.polling(MainScreenUIActivity.this);
    }

        @Override
    public void onDestroy() {
        connected.abortBroadcast();
        connected = null;
        handlerThread.quit();
        unregisterReceiver(connected);
        super.onDestroy();
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

        else if(item.getItemId() == R.id.log_out){

            Intent intent = new Intent(MainScreenUIActivity.this,MainGUIActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //The following methods bring the user to the appropriate activity upon selecting that button

    public void viewNotifications(View v){
        Intent intent = new Intent(MainScreenUIActivity.this,NotificationsActivity.class);
        startActivity(intent);
    }

    public void requestARide(View v){
        Intent intent = new Intent(MainScreenUIActivity.this, RequestARideUIActivity.class);
        startActivity(intent);
    }

    public void myRideRequests(View v){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, MyRideRequestsUIActivity.class);
        startActivity(requestARideIntent);
    }


    public void provideARide(View v){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, ProvideARideUIActivity.class);
        startActivity(requestARideIntent);
    }


    public void myRideOffers(View v){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, MyRideOffersUIActivity.class);
        startActivity(requestARideIntent);
    }
}
