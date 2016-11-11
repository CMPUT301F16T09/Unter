package com.cmput301f16t09.unter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

public class MainScreenUIActivity extends AppCompatActivity {

//    Button requestButton = (Button) findViewById(R.id.RequestARideButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_ui);
        Toast.makeText(MainScreenUIActivity.this, CurrentUser.getCurrentUser().getId(), Toast.LENGTH_SHORT).show();
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
        return super.onOptionsItemSelected(item);
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
//        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, MyRideOffersUIActivity.class);
//        startActivity(requestARideIntent);

        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, RidersRequestDetailsPreUIActivity.class);
        startActivity(requestARideIntent);
    }
}
