package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a 'View' that displays the information for a specific request.
 * It is accessible by drivers once they select a request under 'Provide A Ride'
 * Driver's can offer rides from this screen.
 * Notifications are created here
 */
public class RequestDetailsUIActivity extends AppCompatActivity {

    /**
     * The Road manager.
     */
    // Referenced 2016-11-14
    // If the polyline disappears with zooming
    // http://stackoverflow.com/questions/35799907/route-polyline-dissapears-after-zoom-in-osmbonuspack
    // Author: croaton
    RoadManager roadManager;
    /**
     * The Map.
     */
    MapView map;

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
    /**
     * The Map controller.
     */
    IMapController mapController;

    /**
     * The owner of the request (rider).
     */
    TextView poster;
    /**
     * The Start location.
     */
    TextView start_Location;
    /**
     * The End location.
     */
    TextView end_Location;
    /**
     * The Fare.
     */
    TextView fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Viewing Ride Request Details");
        setContentView(R.layout.activity_request_details_ui);

        // Creating Display for user to see details of request
        poster = (TextView) findViewById(R.id.RequestDetailsPostedByRiderName);
        SpannableString content = new SpannableString(CurrentUser.getCurrentPost().getUsername());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        poster.setText(content);

        start_Location = (TextView) findViewById(R.id.RequestDetailsStartLocationName);
        start_Location.setText(CurrentUser.getCurrentPost().getStartAddress());

        end_Location = (TextView) findViewById(R.id.RequestDetailsEndLocationName);
        end_Location.setText(CurrentUser.getCurrentPost().getEndAddress());

        fare = (TextView) findViewById(R.id.RequestDetailsCost);
        fare.setText("$" + CurrentUser.getCurrentPost().getFare().toString());

        map = (MapView) findViewById(R.id.RequestDetailsMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(2);
        map.setMaxZoomLevel(18);
        mapController = map.getController();

        mapController = map.getController();
        mapController.setZoom(15);

        startPoint = CurrentUser.getCurrentPost().getStartLocation();
        endPoint = CurrentUser.getCurrentPost().getEndLocation();

        mapController.setCenter(startPoint);

        roadManager = new OSRMRoadManager(myActivity);

        ArrayList<OverlayItem> overlayItemArray;
        overlayItemArray = new ArrayList<>();
        overlayItemArray.add(new OverlayItem("Starting Point", "This is the starting point", startPoint));
        overlayItemArray.add(new OverlayItem("Destination", "This is the destination point", endPoint));
        getRoadAsync();

        //Brings the user to the rider's profile if their name is clicked.
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailsUIActivity.this,ViewProfileUIActivity.class);
                intent.putExtra("User",CurrentUser.getCurrentPost().getUsername());
                intent.putExtra("isRestricted", true);
                startActivity(intent);
            }
        });
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
    // Async task to update the road
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

            List<Overlay> mapOverlays = map.getOverlays();

            // Create path to display
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * If the user decides to offer a ride for this request, The PostListOnlineController and UserListController
     * will do the proper updates for the user and post objects.
     *
     * Changes the status of the post from 'Pending Offer' to 'Pending Approval' in the current User, in Persistent data and elastic search.
     * An additional check is done to prevent user from offering a ride multiple times.
     *
     * @param v the current view
     * @see PostListOnlineController
     * @see PostListOfflineController
     * @see UserListOnlineController
     */
    public void confirm_ride_request(View v) {

        Boolean found = false;
        Boolean awaitingCompletion = false;
        try {
            // Check if the user has any requests that are awaiting completion
            CurrentUser.updateCurrentUser();
            if (CurrentUser.getCurrentUser().getMyRequests().size() == 1) {
                PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                searchPostListsTask.execute("documentId", CurrentUser.getCurrentUser().getMyRequests().get(0));
                ArrayList<Post> temp = searchPostListsTask.get();
                if (!temp.isEmpty()) {
                    if (temp.get(0).getStatus().equals("Awaiting Completion")) {
                        awaitingCompletion = true;
                    }
                }
            }
            if (CurrentUser.getCurrentUser().getMyOffers().size() == 1) {
                PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                searchPostListsTask.execute("documentId", CurrentUser.getCurrentUser().getMyOffers().get(0));
                ArrayList<Post> temp = searchPostListsTask.get();
                if (!temp.isEmpty()) {
                    if (temp.get(0).getStatus().equals("Awaiting Completion")) {
                        awaitingCompletion = true;
                    }
                }
            }
        } catch (Exception e) {
            Log.i("Error", "Error with elastic search");
        }
        if (awaitingCompletion == true) {
            Toast.makeText(RequestDetailsUIActivity.this, "Please complete current request before offering a ride!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (CurrentUser.getCurrentUser().getVehicle().equals("")) {
            Toast.makeText(RequestDetailsUIActivity.this, "Please provide information about vehicle before offer a ride!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // Check if the post is still exists and accepting offers
            for(Post p : PostListMainController.getPostList(RequestDetailsUIActivity.this).getPosts()) {
                if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) && (p.getStatus().equals("Pending Approval")) && CurrentUser.getCurrentPost().getId().equals(p.getId())) {
                    found = true;
                    p.addDriverOffer(CurrentUser.getCurrentUser().getUsername());
                    CurrentUser.setCurrentPost(p);
                    break;
                }
            }
            if (found) {
                try {
                    // Update post
                    PostListMainController.updatePosts(CurrentUser.getCurrentPost(), RequestDetailsUIActivity.this);
                    CurrentUser.getCurrentUser().getMyOffers().add(CurrentUser.getCurrentPost().getId());

                    // Update user
                    UserListOnlineController.UpdateUsersTask updateUserTask = new UserListOnlineController.UpdateUsersTask();
                    updateUserTask.execute(CurrentUser.getCurrentUser());
                    updateUserTask.get();

                    // Create notification to be sent
                    String msg = CurrentUser.getCurrentUser().getUsername() + " wants to be your driver for route " + CurrentUser.getCurrentPost().getStartAddress() + " -> " + CurrentUser.getCurrentPost().getEndAddress() +  ", in a " + CurrentUser.getCurrentUser().getVehicle() + " .";
                    Notification notification = new Notification(CurrentUser.getCurrentPost().getUsername(), msg);
                    notification.setPostType("request");

                    // Execute Async task to add to elastic search
                    NotificationOnlineController.AddNotificationsTask addNotificationsTask = new NotificationOnlineController.AddNotificationsTask();
                    addNotificationsTask.execute(notification);
                    addNotificationsTask.get();
                    Toast.makeText(RequestDetailsUIActivity.this, "Successfully sent the offer!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(RequestDetailsUIActivity.this, "Sorry, Could not update the database", Toast.LENGTH_SHORT).show();
                }
            }
            // Sleep thread to allow Async tasks to finish
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
            }
            finish();
        }
    }
}
