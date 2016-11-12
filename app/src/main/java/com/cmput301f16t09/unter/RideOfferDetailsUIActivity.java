package com.cmput301f16t09.unter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RideOfferDetailsUIActivity extends AppCompatActivity {

    TextView rider ;
    final String postOwner = CurrentUser.getCurrentPost().getUsername();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offer_details_ui);

        rider = (TextView) findViewById(R.id.RideOfferRiderName);
        rider.setText(postOwner);
    }

    public void viewProfile(View v){
        Intent intent = new Intent(this,ViewProfileUIActivity.class);
        String postOwner = CurrentUser.getCurrentPost().getUsername();
        intent.putExtra("User",postOwner);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
