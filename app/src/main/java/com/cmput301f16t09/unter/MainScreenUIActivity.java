package com.cmput301f16t09.unter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

public class MainScreenUIActivity extends AppCompatActivity {

    Button requestButton = (Button) findViewById(R.id.RequestARideButton);

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

    public void reqestRide(){
        Intent requestARideIntent = new Intent(MainScreenUIActivity.this, RequestARideUIActivity.class);
        startActivity(requestARideIntent);
    }

}
