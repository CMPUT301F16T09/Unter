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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offer_details_ui);

        rider = (TextView) findViewById(R.id.textViewRideOfferRiderName);
        rider.setText("jellybean");
//        rider.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RideOfferDetailsUIActivity.this,ViewProfileUIActivity.class);
//                intent.putExtra("Rider","jellybean");
//                setIntent(intent);
//            }
//        });

    }

    public void viewProfile(View v){
        Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ViewProfileUIActivity.class);
        intent.putExtra("Rider","jellybean");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
