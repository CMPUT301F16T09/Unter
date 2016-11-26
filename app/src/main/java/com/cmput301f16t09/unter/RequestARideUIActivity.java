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
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequestARideUIActivity extends AppCompatActivity implements MapEventsReceiver {

    private double lowerLeftLat = 53.0;
    private double lowerLeftLong = -114.0;
    private double upperRightLat = 54.0;
    private double upperRightLong = -112.9266;

    private AutoCompleteTextView editStart, editEnd;
    private EditText editFare;
    Activity myActivity = this;

    String startLocation, endLocation;
    ArrayList<String> startAddresses, endAddresses;
    ArrayAdapter<String> startAdapter, endAdapter;

    ArrayList<OverlayItem> overlayItemArray;
    RoadManager roadManager, roadManager2;
    MapView map;
    Road[] mRoads;
    Geocoder coder;
    IMapController mapController;

    GeoPoint startPoint, endPoint;

    Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_aride_ui);
        coder = new Geocoder(this, Locale.getDefault());

        map = (MapView) findViewById(R.id.RequestARideMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(2);
        map.setMaxZoomLevel(18);
        mapController = map.getController();

        GeoPoint edmPoint = new GeoPoint(53.5444, -113.4909);
        mapController.setZoom(15);
        mapController.setCenter(edmPoint);

        roadManager = new OSRMRoadManager(this);
        roadManager2 = new OSRMRoadManager(this);

        overlayItemArray = new ArrayList<>();

        //To use MapEventsReceiver methods, we add a MapEventsOverlay:
        MapEventsOverlay overlay = new MapEventsOverlay(this);
        map.getOverlays().add(overlay);

        find = (Button) findViewById(R.id.findStart);
        registerForContextMenu(find);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void completeStartAddress(View v){
        editStart = (AutoCompleteTextView) findViewById(R.id.RequestRideStartLocation);
        editEnd = (AutoCompleteTextView) findViewById(R.id.RequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.RequestRideCost);
        endLocation = editEnd.getText().toString();

        startAddresses = findAddresses(editStart.getText().toString());
        startAdapter = new ArrayAdapter<>(this, R.layout.start_addresses, startAddresses);

        editStart.setAdapter(startAdapter);
        editStart.showDropDown();

        editStart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!endLocation.isEmpty()){
                    startLocation = startAddresses.get(position);
                    startPoint = getCoords(startLocation);
                    endPoint = getCoords(editEnd.getText().toString());

                    getFareAsync();
                    getRoadAsync();
                }
            }
        });
    }

    public void completeEndAddress(View v){
        editStart = (AutoCompleteTextView) findViewById(R.id.RequestRideStartLocation);
        editEnd = (AutoCompleteTextView) findViewById(R.id.RequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.RequestRideCost);
        startLocation = editStart.getText().toString();

        endAddresses = findAddresses(editEnd.getText().toString());
        endAdapter = new ArrayAdapter<>(this, R.layout.end_addresses, endAddresses);

        editEnd.setAdapter(endAdapter);
        editEnd.setThreshold(9);
        editEnd.showDropDown();


        editEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!startLocation.isEmpty()) {
                    startPoint = getCoords(startLocation);
                    endLocation = endAddresses.get(position);
                    endPoint = getCoords(endLocation);

                    getFareAsync();
                    getRoadAsync();
                }
            }
        });
    }

    public void confirmRideRequest(View v) {

//        startLocation = editStart.getText().toString();
//        endLocation = editEnd.getText().toString();

//
//        if (startLocation.equals("") && endLocation.equals("")) {
//            Toast.makeText(this, "Please fill in start and end locations", Toast.LENGTH_SHORT).show();
//        }
//        else if (startLocation.equals("")) {
//            Toast.makeText(this, "Please fill in start location", Toast.LENGTH_SHORT).show();
//        }
//        else if (endLocation.equals("")) {
//            Toast.makeText(this, "Please fill in end location", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            startPoint = getCoords(startLocation);
//            endPoint = getCoords(endLocation);

        editFare = (EditText) findViewById(R.id.RequestRideCost);
        String fare = editFare.getText().toString();

        Log.d("START LOCATION", startLocation);
        Log.d("END END LOCATION", endLocation);

        PostListOnlineController.AddPostsTask addPostOnline = new PostListOnlineController.AddPostsTask();
        Post newPost = new Post(startPoint, endPoint, startLocation, endLocation, fare, CurrentUser.getCurrentUser().getUsername());
        PostListOfflineController.addOfflinePost(newPost, RequestARideUIActivity.this);
        addPostOnline.execute(newPost);
        Toast.makeText(this, "Request Made", Toast.LENGTH_SHORT).show();

        finish();
    }

    /**
     * Taken from CMPUT 301 Fall 16 Lab 8 - Geolocation by Stephen Romansky
     * Calls upon the UpdateRoadTask to draw a given route in the Activities MapView
     */
    public void getRoadAsync() {
        mRoads = null;

        overlayItemArray.add(new OverlayItem("Starting Point", "This is the starting point", startPoint));
        overlayItemArray.add(new OverlayItem("Destination", "This is the destination point", endPoint));

        ArrayList<GeoPoint> wayPoints = new ArrayList<>(2);
        wayPoints.add(startPoint);
        wayPoints.add(endPoint);

        mapController.setCenter(startPoint);
        new UpdateRoadTask().execute(wayPoints);
    }
    private class UpdateRoadTask extends AsyncTask<Object, Void, Road[]> {
        protected Road[] doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> wayPoints = (ArrayList<GeoPoint>) params[0];

            Marker startMarker = new Marker(map);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            startMarker.setIcon(getDrawable(R.mipmap.ic_launcher));
//            startMarker.setTitle("Start point");



            Marker endMarker = new Marker(map);
            endMarker.setPosition(endPoint);
            endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            endMarker.setIcon(getDrawable(R.mipmap.ic_launcher));
//            endMarker.setTitle("End point");

            return roadManager.getRoads(wayPoints);
        }
        @Override
        protected void onPostExecute(Road[] roads) {
            mRoads = roads;
            if (roads == null)
                return;
            if (roads[0].mStatus == Road.STATUS_TECHNICAL_ISSUE)
                Toast.makeText(map.getContext(), "Technical issue when getting the route", Toast.LENGTH_SHORT).show();
            else if (roads[0].mStatus > Road.STATUS_TECHNICAL_ISSUE) //functional issues
                Toast.makeText(map.getContext(), "No possible route here", Toast.LENGTH_SHORT).show();

            Polyline[] mRoadOverlays = new Polyline[roads.length];
            List<Overlay> mapOverlays = map.getOverlays();

            //for (int i = 0; i < roads.length; i++) {
                Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[0]);
                String routeDesc = roads[0].getLengthDurationText(myActivity.getBaseContext(), -1);
                roadPolyline.setTitle(getString(R.string.app_name) + " - " + routeDesc);
                roadPolyline.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
                roadPolyline.setRelatedObject(0);

                mapOverlays.clear();
                mapOverlays.add(0, roadPolyline);
                //mapOverlays.add(roadPolyline);
                map.invalidate();
                //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
                //to avoid covering the other overlays.

        }
    }

    public void getFareAsync(){
        ArrayList<GeoPoint> roadPoints = new ArrayList<>(2);
        roadPoints.add(startPoint);
        roadPoints.add(endPoint);
        new FareCalculatingTask().execute(roadPoints);
    }
    private class FareCalculatingTask extends AsyncTask<Object, Void, Road[]> {

        protected Road[] doInBackground(Object... points) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> startToEnd = (ArrayList<GeoPoint>) points[0];
            return roadManager2.getRoads(startToEnd);
        }

        @Override
        protected void onPostExecute(Road[] roads) {

            if (roads == null){return;}
            double distance = 0.0;

            //for (int i = 0; i < roads.length; i++) {

                Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[0]);
                List<GeoPoint> roadSegment = roadPolyline.getPoints();

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


            double fare = distance / 1000 + 4.4;
            editFare.setText(String.format("$%.2f", fare));
        }
    }

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
    private GeoPoint getCoords(String location){

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

    //https://github.com/MKergall/osmbonuspack/blob/master/OSMNavigator/src/main/java/com/osmnavigator/MapActivity.java
    //date: November 25th, 2016, author: MKergall
    // lines 1385 - 1434

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //Toast.makeText(this, "Tapped", Toast.LENGTH_SHORT).show();
        //DO NOTHING
        return true;
    }

    GeoPoint clicked;

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();

        clicked = p;
        Button find = (Button) findViewById(R.id.findStart);
        registerForContextMenu(find);
        openContextMenu(find);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.menu_departure:
                editFare = (EditText) findViewById(R.id.RequestRideCost);
                startPoint = clicked;
                startLocation = reverseGeocode(startPoint);
//                Log.d("START POINT", startPoint.toDoubleString());
//                Log.d("CLICKED", clicked.toDoubleString());

                if (endPoint != null) {
                    getFareAsync();
                    getRoadAsync();
                }

                return true;

            case R.id.menu_destination:
                editFare = (EditText) findViewById(R.id.RequestRideCost);
                endPoint = clicked;
                endLocation = reverseGeocode(endPoint);
//                Log.d("START POINT", endPoint.toDoubleString());
//                Log.d("CLICKED", clicked.toDoubleString());

                if (startPoint != null) {
                    getFareAsync();
                    getRoadAsync();
                }

                return true;
    default:

            return super.onContextItemSelected(item);
        }
    }

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
}