package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ride offer details ui activity.
 * The User sees the details of his ride offer in this activity
 * details like the name of the user who made the request, the fare offered and the status of
 * the request
 */
public class RideOfferDetailsUIActivity extends AppCompatActivity {

    /**
     * The Road manager.
     */
    RoadManager roadManager;
    /**
     * The Map.
     */
    MapView map;
    /**
     * The Map controller.
     */
    IMapController mapController;

    /**
     * The My activity.
     */
    Activity myActivity = this;
    /**
     * The Start point.
     */
    GeoPoint startPoint;
    /**
     * The End point.
     */
    GeoPoint endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Viewing Ride Offer Details");
        setContentView(R.layout.activity_ride_offer_details_ui);

        // Creating Display for user to see details of offer
        TextView tvRiderName = (TextView) findViewById(R.id.RideOfferRiderName);
        String riderName = CurrentUser.getCurrentPost().getUsername();
        SpannableString content = new SpannableString(riderName);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvRiderName.setText(content);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideOfferCost);
        String offeredFare = CurrentUser.getCurrentPost().getFare().toString();
        tvOfferedFare.setText("$" + offeredFare);

        TextView tvStatus = (TextView) findViewById(R.id.RideOfferStatusDesc);
        String statusDesc = CurrentUser.getCurrentPost().getStatus();
        tvStatus.setText(statusDesc);

        map = (MapView) findViewById(R.id.ride_offer_details_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(2);
        map.setMaxZoomLevel(18);
        mapController = map.getController();

        roadManager = new OSRMRoadManager(myActivity);

        startPoint = CurrentUser.getCurrentPost().getStartLocation();
        endPoint = CurrentUser.getCurrentPost().getEndLocation();

        mapController.setCenter(startPoint);
        mapController.setZoom(15);
        getRoadAsync();
    }

    /**
     * Views the profile of a certain user (this user's name is long clicked on by another user)
     *
     * @param v the v
     */
    public void viewProfile(View v){
        Intent intent = new Intent(this,ViewProfileUIActivity.class);
        String postOwner = CurrentUser.getCurrentPost().getUsername();
        intent.putExtra("User", postOwner);
        intent.putExtra("isRestricted", true);
        startActivity(intent);
    }

    /**
     * Taken from CMPUT 301 Fall 16 Lab 8 - Geolocation by Stephen Romansky
     * Calls upon the UpdateRoadTask to draw a given route in the Activities MapView
     */
    public void getRoadAsync() {

        ArrayList<GeoPoint> wayPoints = new ArrayList<>(2);
        wayPoints.add(startPoint);
        wayPoints.add(endPoint);

        mapController.setCenter(startPoint);

        if (WifiReceiver.isNetworkAvailable(myActivity)){
            new UpdateRoadTask().execute(wayPoints);
        } else{
            Toast.makeText(this, "Cannot draw road Offline", Toast.LENGTH_SHORT).show();
        }
    }
    private class UpdateRoadTask extends AsyncTask<Object, Void, Road> {

        protected Road doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> wayPoints = (ArrayList<GeoPoint>) params[0];
            return roadManager.getRoad(wayPoints);
        }
        @Override
        protected void onPostExecute(Road road) {

            if (road == null)
                return;
            if (road.mStatus == Road.STATUS_TECHNICAL_ISSUE)
                Toast.makeText(map.getContext(), "Technical issue when getting the route", Toast.LENGTH_SHORT).show();
            else if (road.mStatus > Road.STATUS_TECHNICAL_ISSUE) //functional issues
                Toast.makeText(map.getContext(), "No possible route here", Toast.LENGTH_SHORT).show();

            // Create the path for the user on the map
            List<Overlay> mapOverlays = map.getOverlays();

            Polyline roadPolyline = RoadManager.buildRoadOverlay(road);
            String routeDesc = road.getLengthDurationText(myActivity.getBaseContext(), -1);
            roadPolyline.setTitle(getString(R.string.app_name) + " - " + routeDesc);
            roadPolyline.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
            roadPolyline.setRelatedObject(0);

            Marker startMarker = new Marker(map);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setPosition(startPoint);

            Marker endMarker = new Marker(map);
            endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            endMarker.setPosition(endPoint);

            startMarker.setTitle("Departure:\n" + CurrentUser.getCurrentPost().getStartAddress());
            endMarker.setTitle("Destination:\n" + CurrentUser.getCurrentPost().getEndAddress());

            //We insert the road overlays at the "bottom", just above the MapEventsOverlay,
            //to avoid covering the other overlays.
            mapOverlays.add(startMarker);
            mapOverlays.add(endMarker);
            mapOverlays.add(0, roadPolyline);

            map.invalidate();
        }
    }
}
