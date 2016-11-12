package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RideOfferDetailsUIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offer_details_ui);

        TextView tvRiderName = (TextView) findViewById(R.id.RideOfferRiderName);
        String riderName = CurrentUser.getCurrentPost().getUsername();
        tvRiderName.setText(riderName);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideOfferCost);
        String offeredFare = CurrentUser.getCurrentPost().getFare();
        tvOfferedFare.setText(offeredFare);

        TextView tvStatus = (TextView) findViewById(R.id.RideOfferStatusDesc);
        String statusDesc = CurrentUser.getCurrentPost().getStatus();
        tvStatus.setText(statusDesc);
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
