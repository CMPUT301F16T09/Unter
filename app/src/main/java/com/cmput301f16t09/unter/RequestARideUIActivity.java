package com.cmput301f16t09.unter;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
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
 * @author Daniel
 */
public class RequestARideUIActivity extends AppCompatActivity {

    private AutoCompleteTextView editStart;
    private AutoCompleteTextView editEnd;
    private EditText editFare;

    /**
     * The My activity.
     */
    Activity myActivity = this;

    /**
     * The Road Managers.
     */
    RoadManager roadManager, roadManager2;

    MapView map;
    /**
     * The M roads.
     */
    Road[] mRoads;
    /**
     * The Coder.
     */
    Geocoder coder;
    /**
     * The Map controller.
     */
    IMapController mapController;

    /**
     * The Latitude.
     */
    double latitude;
    /**
     * The Longitude.
     */
    double longitude;
    /**
     * The Start point.
     */
    GeoPoint startPoint;
    /**
     * The End point.
     */
    GeoPoint endPoint;

    /** Called when the activity is created. */
    /**
     * When the activity is created, the user can interact with the TextView boxes available, get an
     * estimated fare for the ride, and see the route to take on the map.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_aride_ui);

        /* Set up the map information */
        coder = new Geocoder(this, Locale.getDefault());
        map = (MapView) findViewById(R.id.RequestARideMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();

        /* Center the map on Edmonton */
        GeoPoint edmPoint = new GeoPoint(53.5444, -113.4909);
        mapController.setZoom(15);
        mapController.setCenter(edmPoint);
        
    }

    /* Create the menu button on the activity to allow the user to edit their profile */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Get an estimated fare for the route from start location to end location based on distance.
     * Pressing the Get Estimate button will update the map. The start and end locations are required
     * before the estimate can be obtained.
     * @param v the view
     */
    public void getEstimate(View v) {

        editStart = (AutoCompleteTextView) findViewById(R.id.RequestRideStartLocation);
        editEnd = (AutoCompleteTextView) findViewById(R.id.RequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.RequestRideCost);

        String startLocation = editStart.getText().toString();
        String endLocation = editEnd.getText().toString();

        if (startLocation.equals("") && endLocation.equals("")) {
            Toast.makeText(this, "Please fill in start and end locations", Toast.LENGTH_SHORT).show();
        } else if (startLocation.equals("")) {
            Toast.makeText(this, "Please fill in start location", Toast.LENGTH_SHORT).show();
        } else if (endLocation.equals("")) {
            Toast.makeText(this, "Please fill in end location", Toast.LENGTH_SHORT).show();
        } else {

            startPoint = findCoords(startLocation);
            endPoint = findCoords(endLocation);

            mapController.setCenter(startPoint);
            // to get a key http://developer.mapquest.com/
            //roadManager = new MapQuestRoadManager("xPGrfmORuC6QJMSkF6SXGKYbBgTefNdm");
            roadManager = new OSRMRoadManager(this);
            roadManager2 = new OSRMRoadManager(this);   //fixes fare calculating stuff for some weird reason

            getFareAsync();
            getRoadAsync();
        }
    }

    /**
     * Confirms and creates the ride request post with the inputted information. The start and end
     * locations must be specified.
     *
     * @param v the v
     */
    public void confirmRideRequest(View v) {

        editStart = (AutoCompleteTextView) findViewById(R.id.RequestRideStartLocation);
        editEnd = (AutoCompleteTextView) findViewById(R.id.RequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.RequestRideCost);

        String startLocation = editStart.getText().toString();
        String endLocation = editEnd.getText().toString();
        String fare = editFare.getText().toString();


        if (startLocation.equals("") && endLocation.equals("")) {
            Toast.makeText(this, "Please fill in start and end locations", Toast.LENGTH_SHORT).show();
        }
        else if (startLocation.equals("")) {
            Toast.makeText(this, "Please fill in start location", Toast.LENGTH_SHORT).show();
        }
        else if (endLocation.equals("")) {
            Toast.makeText(this, "Please fill in end location", Toast.LENGTH_SHORT).show();
        }
        else {
            startPoint = findCoords(startLocation);
            endPoint = findCoords(endLocation);

            /**
             * Add the post to elastic search and save the post to the offline data.
             * @see PostListOfflineController
             * @see PostListOnlineController
             * @see Post
             */
            PostListOnlineController.AddPostsTask addPostOnline = new PostListOnlineController.AddPostsTask();
            Post newPost = new Post(startPoint, endPoint, startLocation, endLocation, fare, CurrentUser.getCurrentUser().getUsername());
            PostListOfflineController.addOfflinePost(newPost, RequestARideUIActivity.this);
            addPostOnline.execute(newPost);
            Toast.makeText(this, "Request Made", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Taken from CMPUT 301 Fall 16 Lab 8 - Geolocation by Stephen Romansky
     * Calls upon the UpdateRoadTask to draw a given route in the Activities MapView
     */
    public void getRoadAsync() {
        mRoads = null;
        ArrayList<GeoPoint> wayPoints = new ArrayList<>(2);
        wayPoints.add(startPoint);
        wayPoints.add(endPoint);
        new UpdateRoadTask().execute(wayPoints);
    }

    /**
     * Used to update and draw the road on the map.
     * @see GeoPoint
     * @see Polyline
     */
    private class UpdateRoadTask extends AsyncTask<Object, Void, Road[]> {
        protected Road[] doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> wayPoints = (ArrayList<GeoPoint>) params[0];
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
            List<Overlay> mapOverlays = map.getOverlays();
            for (int i = 0; i < roads.length; i++) {
                Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[i]);
                String routeDesc = roads[i].getLengthDurationText(myActivity.getBaseContext(), -1);
                roadPolyline.setTitle(getString(R.string.app_name) + " - " + routeDesc);
                roadPolyline.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
                roadPolyline.setRelatedObject(i);
                mapOverlays.add(0, roadPolyline);

                map.invalidate();
                //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
                //to avoid covering the other overlays.
            }
        }
    }

    /**
     * Used to get an estimate fare for the route. (Incomplete)
     * @see FareCalculatingTask
     * @see GeoPoint
     */
    public void getFareAsync(){
        ArrayList<GeoPoint> roadPoints = new ArrayList<>(2);
        roadPoints.add(startPoint);
        roadPoints.add(endPoint);
        new FareCalculatingTask().execute(roadPoints);
    }

    /**
     *  Method used to calculate the fare based on the given points.
     *  @see GeoPoint
     */
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

            /**
             * Draws small segments of the Polyline and calculates the cost of the line based on the
             * length of the line
             */
            for (int i = 0; i < roads.length; i++) {

                Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[i]);
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
            }

            double fare = distance / 1000 + 4.4;
            editFare.setText(String.format("$%.2f", fare));
        }
    }

    /**
     * Find and display the name of the lat/long GeoPoint based on the Geocoder.
     * @param address
     * @return GeoPoint
     */
    private GeoPoint findCoords(String address) {
        //Nov 7th, 2016 - http://stackoverflow.com/questions/13576470/converting-an-address-into-geopoint
        //Geocoder takes an string and finds an address that most closely resembles the string
        //then latitude and longitude is extracted from the address
        //Author: Misha Bhardwaj
        try {
            List<Address> startAddress = coder.getFromLocationName(address, 1);
            latitude = startAddress.get(0).getLatitude();
            longitude = startAddress.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(this, "Invalid Location", Toast.LENGTH_SHORT).show();
            Log.i("Error", "Can't find Location");
        }
        return new GeoPoint(latitude, longitude);
    }
}