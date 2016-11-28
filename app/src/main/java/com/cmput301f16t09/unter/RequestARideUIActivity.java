package com.cmput301f16t09.unter;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The Request a ride UI activity that allows riders to create a post for a ride. The Activity
 * allows the user to specify start and end location, get an estimate for a fare based on the
 * locations, or input their own fare. The locations can be displayed on the map when the Get Estimate
 * button is pressed.
 *
 */
public class RequestARideUIActivity extends AppCompatActivity implements MapEventsReceiver {

    // Set default values
    private double lowerLeftLat = 53.0;
    private double lowerLeftLong = -114.0;
    private double upperRightLat = 54.0;
    private double upperRightLong = -112.9266;

    private AutoCompleteTextView editStart, editEnd;
    private EditText editFare;
    private double distance = 0.0;

    /**
     * The My activity.
     */
    Activity myActivity = this;

    /**
     * The Start Marker
     */
    Marker startMarker;

    /**
     * The End Marker
     */
    Marker endMarker;

    /**
     * The start location.
     */
    String startLocation;

    /**
     * The End location.
     */
    String endLocation;
    /**
     * The Start addresses.
     */
    ArrayList<String> startAddresses;

    /**
     * The End addresses.
     */
    ArrayList<String> endAddresses;

    /**
     * The Start adapter.
     */
    ArrayAdapter<String> startAdapter;

    /**
     * The End adapter.
     */
    ArrayAdapter<String> endAdapter;

    /**
     * The Road manager.
     */
    RoadManager roadManager;

    /**
     * The Road manager 2.
     */
    RoadManager roadManager2;

    /**
     * The Map.
     */
    MapView map;
    /**
     * The Coder.
     */
    Geocoder coder;
    /**
     * The Map controller.
     */
    IMapController mapController;

    /**
     * The Start point.
     */
    GeoPoint startPoint;

    /**
     * The End point.
     */
    GeoPoint endPoint;

    /**
     * The Find.
     */
    Button find;

    /** Called when the activity is created. */
    /**
     * When the activity is created, the user can interact with the TextView boxes available, get an
     * estimated fare for the ride, and see the route to take on the map.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Requesting A Ride");
        setContentView(R.layout.activity_request_aride_ui);

        /* Set up the map information */
        coder = new Geocoder(this, Locale.getDefault());
        map = (MapView) findViewById(R.id.RequestARideMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(2);
        map.setMaxZoomLevel(18);
        mapController = map.getController();

        /* set the map to view the centre of Edmonton */
        GeoPoint edmPoint = new GeoPoint(53.5444, -113.4909);
        mapController.setZoom(15);
        mapController.setCenter(edmPoint);

        roadManager = new OSRMRoadManager(this);
        roadManager2 = new OSRMRoadManager(this);

        //To use MapEventsReceiver methods, we add a MapEventsOverlay:
        MapEventsOverlay overlay = new MapEventsOverlay(this);
        map.getOverlays().add(overlay);

        find = (Button) findViewById(R.id.findStart);
        registerForContextMenu(find);
    }

    /**
     * Complete start address.
     *
     * @param v the v
     */
    public void completeStartAddress(View v){
        editStart = (AutoCompleteTextView) findViewById(R.id.RequestRideStartLocation);
        editEnd = (AutoCompleteTextView) findViewById(R.id.RequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.RequestRideCost);
        endLocation = editEnd.getText().toString();

        if (WifiReceiver.isNetworkAvailable(RequestARideUIActivity.this)) {
            startAddresses = findAddresses(editStart.getText().toString());
            startAdapter = new ArrayAdapter<>(this, R.layout.start_addresses, startAddresses);

            editStart.setAdapter(startAdapter);
            editStart.showDropDown();
        } else {
            Toast.makeText(this, "Tap on map while offline", Toast.LENGTH_SHORT).show();
        }

        editStart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startLocation = startAddresses.get(position);
                startPoint = geocode(startLocation);
                startMarker = updateMarker(startMarker, startPoint, "Departure:\n" + startLocation);

                if (!endLocation.isEmpty()) {
                    endPoint = geocode(editEnd.getText().toString());
                    getFareAsync();
                    getRoadAsync();

                } else {
                    mapController.setCenter(startPoint);
                    map.invalidate();
                }
            }
        });
    }

    /**
     * Complete end address.
     *
     * @param v the v
     */
    public void completeEndAddress(View v){
        editStart = (AutoCompleteTextView) findViewById(R.id.RequestRideStartLocation);
        editEnd = (AutoCompleteTextView) findViewById(R.id.RequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.RequestRideCost);
        startLocation = editStart.getText().toString();


        if (WifiReceiver.isNetworkAvailable(RequestARideUIActivity.this)) {
            endAddresses = findAddresses(editEnd.getText().toString());
            endAdapter = new ArrayAdapter<>(this, R.layout.end_addresses, endAddresses);

            editEnd.setAdapter(endAdapter);
            editEnd.setThreshold(9);
            editEnd.showDropDown();
        } else {
            Toast.makeText(this, "Tap on map while offline", Toast.LENGTH_SHORT).show();
        }

        editEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                endLocation = endAddresses.get(position);
                endPoint = geocode(endLocation);
                endMarker = updateMarker(endMarker, endPoint, "Destination:\n" + endLocation);

                if (!startLocation.isEmpty()) {
                    startPoint = geocode(startLocation);
                    getFareAsync();
                    getRoadAsync();
                }
                else {
                    mapController.setCenter(endPoint);
                    map.invalidate();
                }
            }
        });
    }

    /**
     * The Awaiting completion.
     */
    Boolean awaitingCompletion = false;

    /**
     * Confirm ride request.
     *
     * @param v the v
     */
    public void confirmRideRequest(View v) {

        try {
            // Check if the user has any posts with awaiting completion
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
            Toast.makeText(RequestARideUIActivity.this, "Please complete current request before making another!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            editFare = (EditText) findViewById(R.id.RequestRideCost);
            String fare = editFare.getText().toString();

            if (startLocation == null && endLocation == null) {
                Toast.makeText(this, "Please specify your start and end locations", Toast.LENGTH_SHORT).show();
            }
            else if (startLocation == null || startLocation.isEmpty()) {
                Toast.makeText(this, "Please specify your start location", Toast.LENGTH_SHORT).show();
            }
            else if (endLocation == null || endLocation.isEmpty()) {
                Toast.makeText(this, "Please specify your end location", Toast.LENGTH_SHORT).show();
            }
            else if (fare.isEmpty()){
                Toast.makeText(this, "Please specify your offered fare", Toast.LENGTH_SHORT).show();
            }
            else {

                Double convertedFare = 0.0;
                boolean isNumeric = true;

                Log.d("START", startLocation);
                Log.d("END", endLocation);

                // Check if the value is valid
                try {
                    convertedFare = Double.parseDouble(fare);
                } catch (Exception e) {
                    isNumeric = false;
                }

                // If the value is valid
                if (isNumeric) {
                    try {
                        Post newPost = new Post(startPoint, endPoint, startLocation, endLocation, convertedFare, convertedFare/(distance/1000), CurrentUser.getCurrentUser().getUsername());
                        // Add post and update user
                        PostListMainController.addPost(newPost, RequestARideUIActivity.this);
                        CurrentUser.getCurrentUser().getMyRequests().add(newPost.getId());
                        UserListOnlineController.UpdateUsersTask updateUserListstask = new UserListOnlineController.UpdateUsersTask();
                        updateUserListstask.execute(CurrentUser.getCurrentUser());
                        updateUserListstask.get();
                    }
                    catch (Exception e) {
                        Log.i("Error", "Elastic Search Error");
                    }
                    try {
                        Thread.sleep(1000);
                    }
                    catch (Exception e) {
                        Log.i("Error", "Thread Sleep Error");
                    }
                    Toast.makeText(this, "Request Made", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(this, "Request invalid", Toast.LENGTH_SHORT).show();
                }
            }
        }
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

        if (WifiReceiver.isNetworkAvailable(RequestARideUIActivity.this)){
            new UpdateRoadTask().execute(wayPoints);
        } else{
            Toast.makeText(this, "Cannot draw road Offline", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Used to update and draw the road on the map.
     * @see GeoPoint
     * @see Polyline
     */
    Polyline roadPolyline;

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

            if (mapOverlays.contains(roadPolyline)) mapOverlays.remove(roadPolyline);

            /*Polyline*/ roadPolyline = RoadManager.buildRoadOverlay(road);
            String routeDesc = road.getLengthDurationText(myActivity.getBaseContext(), -1);

            roadPolyline.setTitle(getString(R.string.app_name) + " - " + routeDesc);
            roadPolyline.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
            roadPolyline.setRelatedObject(0);

            /*
            Add the starting and ending markers.
             */
            mapOverlays.add(startMarker);
            mapOverlays.add(endMarker);
            mapOverlays.add(0, roadPolyline);

            //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
            //to avoid covering the other overlays.
            map.invalidate();
        }
    }

    /**
     * Used to get an estimate fare for the route. (Incomplete)
     *
     * @see FareCalculatingTask
     * @see GeoPoint
     */
    public void getFareAsync(){
        ArrayList<GeoPoint> roadPoints = new ArrayList<>(2);
        roadPoints.add(startPoint);
        roadPoints.add(endPoint);

        if(WifiReceiver.isNetworkAvailable(RequestARideUIActivity.this)) {
            new FareCalculatingTask().execute(roadPoints);
        }
        else {
            Toast.makeText(this, "Cannot estimate fare offline", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  Method used to calculate the fare based on the given points.
     *  @see GeoPoint
     */

    private class FareCalculatingTask extends AsyncTask<Object, Void, Road> {

        protected Road doInBackground(Object... points) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> startToEnd = (ArrayList<GeoPoint>) points[0];
            return roadManager2.getRoad(startToEnd);
        }

        @Override
        protected void onPostExecute(Road road) {

            if (road == null){return;}

            // Set inital distance value
            distance = 0.0;

            Polyline roadPolyline = RoadManager.buildRoadOverlay(road);
            List<GeoPoint> roadSegment = roadPolyline.getPoints();

            // Find the distance of the segments
            for (int j = 0; j < roadSegment.size() - 1; j+= 1){
                GeoPoint geoStart = roadSegment.get(j);
                GeoPoint geoEnd = roadSegment.get(j + 1);

                Location start = new Location("start");
                start.setLatitude(geoStart.getLatitude());
                start.setLongitude(geoStart.getLongitude());

                Location end = new Location("end");
                end.setLatitude(geoEnd.getLatitude());
                end.setLongitude(geoEnd.getLongitude());

                distance += start.distanceTo(end);
            }

            // Fare Calculating formula
            double fare = distance / 1000 + 4.4;
            editFare.setText(String.format("%.2f", fare));
        }
    }

    // Find Address method from inputted string
    private ArrayList<String> findAddresses(String address) {
        //Nov 7th, 2016 - http://stackoverflow.com/questions/13576470/converting-an-address-into-geopoint
        //Geocoder takes an string and finds an address that most closely resembles the string
        //then latitude and longitude is extracted from the address
        ArrayList<String> stringAddresses = new ArrayList<>(9);
        try {
            List<Address> addresses = coder.getFromLocationName(address, 4,
                    lowerLeftLat, lowerLeftLong, upperRightLat, upperRightLong);

            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.size(); i++) {
                    stringAddresses.add((addresses.get(i)).getAddressLine(0) + "\n"
                            + addresses.get(i).getAddressLine(1));
                }
            } else{
                Toast.makeText(myActivity, "No Results!" , Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringAddresses;
    }

    // Get Coordinates from string location
    private GeoPoint geocode(String location){

        // Set inital values
        double latitude, longitude;
        latitude = 0.0;
        longitude = 0.0;

        try {
            List<Address> addresses = coder.getFromLocationName(location, 1,
                    lowerLeftLat, lowerLeftLong, upperRightLat, upperRightLong);
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new GeoPoint(latitude, longitude);
    }

    /**
     * Reverse geocode string.
     *
     * @param point the point
     * @return the string
     */
    public String reverseGeocode(GeoPoint point){
        String address = "None";

        try {
            List<Address> addresses = coder.getFromLocation(point.getLatitude(),
                                                            point.getLongitude(), 1);

            address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    //https://github.com/MKergall/osmbonuspack/blob/master/OSMNavigator/src/main/java/com/osmnavigator/MapActivity.java
    //date: November 25th, 2016, author: MKergall
    // singleTapConfirmHelper, longPressHelper, onCreateContextMenu and onContextItemSelected
    // inspired from lines 1385 - 1434

    // Referenced from
    // https://github.com/MKergall/osmbonuspack/blob/master/OSMNavigator/src/main/java/com/osmnavigator/MapActivity.java
    // date: November 25th, 2016, author: MKergall
    // lines 1385 - 1434

    /**
     * The Clicked.
     */
    GeoPoint clicked;

    // Overwriting required method
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //DO NOTHING
        return true;
    }

    // Grab and create context for map
    @Override
    public boolean longPressHelper(GeoPoint p) {
        clicked = p;
        Button find = (Button) findViewById(R.id.findStart);
        registerForContextMenu(find);
        openContextMenu(find);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
    }

    // Create map marker from selected context
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.menu_departure:
                editFare = (EditText) findViewById(R.id.RequestRideCost);
                startPoint = clicked;

                if (WifiReceiver.isNetworkAvailable(RequestARideUIActivity.this)) {
                    startLocation = reverseGeocode(startPoint);
                } else {
                    //startLocation = startPoint.toDoubleString();
                    startLocation = String.format("%.2f", startPoint.getLatitude()) + ", "
                                  + String.format("%.2f", startPoint.getLongitude());
                }

                startMarker = updateMarker(startMarker, startPoint, "Departure:\n" + startLocation);
                map.invalidate();

                if (endPoint != null) {
                    getFareAsync();
                    getRoadAsync();
                }

                return true;

            case R.id.menu_destination:
                editFare = (EditText) findViewById(R.id.RequestRideCost);
                endPoint = clicked;

                if(WifiReceiver.isNetworkAvailable(RequestARideUIActivity.this)) {
                    endLocation = reverseGeocode(endPoint);
                } else {
                    endLocation = String.format("%.2f", endPoint.getLatitude()) + ", "
                                + String.format("%.2f", endPoint.getLongitude());
                }

                endMarker = updateMarker(endMarker, endPoint, "Destination:\n" + endLocation);
                map.invalidate();

                if (startPoint != null) {
                    getFareAsync();
                    getRoadAsync();
                }

                return true;
    default:

            return super.onContextItemSelected(item);
        }
    }

    //https://github.com/MKergall/osmbonuspack/blob/master/OSMNavigator/src/main/java/com/osmnavigator/MapActivity.java
    //date: November 27th, 2016, author: MKergall
    //updateMarker inspired from lines 758 - 753 (the method called updateItineraryMarker)
    public Marker updateMarker(Marker marker, GeoPoint p, String title){

        if (marker == null){
            marker = new Marker(map);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            //marker.setOnMarkerDragListener(dragListener);
            map.getOverlays().add(marker);
        }

        marker.setPosition(p);
        marker.setTitle(title);
        return marker;
    }

//    class OnDragListener implements Marker.OnMarkerDragListener {
//
//        @Override public void onMarkerDrag(Marker marker) {}
//        @Override public void onMarkerDragStart(Marker marker) {}
//
//        @Override public void onMarkerDragEnd(Marker marker) {
//
//            if (marker == startMarker){
//
//                startPoint = marker.getPosition();
//                startLocation = reverseGeocode(startPoint);
//            }
//
//            else if (marker == endMarker) {
//
//                endPoint = marker.getPosition();
//                endLocation = reverseGeocode(endPoint);
//            }
//
//            //update route:
//            getRoadAsync();
//        }
//    }
//
//    final OnDragListener dragListener = new OnDragListener();
}