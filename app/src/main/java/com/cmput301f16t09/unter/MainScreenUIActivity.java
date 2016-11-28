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

/**
 * A view that allows the user to choose between rider activies or driver activities.
 */
public class MainScreenUIActivity extends AppCompatActivity {

    /**
     * The Connected.
     */
    WifiReceiver connected;
    /**
     * The Handler thread.
     */
    HandlerThread handlerThread = new HandlerThread("WiFiConnectionThread");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Choose an option");
        setContentView(R.layout.activity_main_screen_ui);

        // 2016-11-19
        // http://stackoverflow.com/questions/5674518/does-broadcastreceiver-onreceive-always-run-in-the-ui-thread
        // Author: Caner
        // Set up a handler thread once someone is logged in because they must be online to log in
        // its purpose is to receive signals on a network change
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);

        // Log if the WifiConnectionThread is alive
        if (handlerThread.isAlive()) {Log.i("WiFiConnectionThread","Working");}

        //Make WifiReceiver
        connected = new WifiReceiver();

        //Create new intents
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(connected, intentFilter, null, handler);

        // Notifications will be checked and the periodic notification check will be activated here on the handler thread
        NotificationOnlineController.createNotifications(MainScreenUIActivity.this);
        WifiReceiver.polling(MainScreenUIActivity.this);
    }

    // Remove thread if activity is destroyed
    @Override
    public void onDestroy() {
        handlerThread.quit();
        unregisterReceiver(connected);
        super.onDestroy();
    }
    /**
     * Editing profile and logging out can only be done on the main screen
     */
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

    /**
     * The following methods bring the user to the appropriate activity upon selecting that button
     *
     * @param v the v
     */
    public void viewNotifications(View v){
        Intent intent = new Intent(MainScreenUIActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }

    /**
     * Request a ride.
     *
     * @param v the v
     */
    public void requestARide(View v){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, RequestARideUIActivity.class);
        startActivity(requestARideIntent);
    }

    /**
     * My ride requests.
     *
     * @param v the v
     */
    public void myRideRequests(View v){
        Intent myRideRequestsIntent = new Intent(MainScreenUIActivity.this, MyRideRequestsUIActivity.class);
        startActivity(myRideRequestsIntent);
    }


    /**
     * Provide a ride.
     *
     * @param v the v
     */
    public void provideARide(View v){
        Intent provideARideIntent = new Intent(MainScreenUIActivity.this, ProvideARideUIActivity.class);
        startActivity(provideARideIntent);
    }


    /**
     * My ride offers.
     *
     * @param v the v
     */
    public void myRideOffers(View v){
        Intent myRideOffersIntent = new Intent(MainScreenUIActivity.this, MyRideOffersUIActivity.class);
        startActivity(myRideOffersIntent);
    }
}
