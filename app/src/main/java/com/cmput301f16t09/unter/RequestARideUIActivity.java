package com.cmput301f16t09.unter;
import android.app.Activity;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
public class RequestARideUIActivity extends AppCompatActivity {
    EditText editStart;
    EditText editEnd;
    EditText editFare;

    RoadManager roadManager;
    MapView map;
    Road[] mRoads;
    GeoPoint startPoint;
    GeoPoint endPoint;
    double startLong;
    double startLat;
    double endLat;
    double endLong;
    Activity myActivity = this;
    IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_aride_ui);

        editStart = (EditText) findViewById(R.id.editTextRequestRideStartLocation);
        editEnd = (EditText) findViewById(R.id.editTextRequestRideEndLocation);
        editFare = (EditText) findViewById(R.id.editTextRequestRideEstimatedFare);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(12);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void requestARide(){
        String startLocation = editStart.getText().toString();
        String endLocation = editEnd.getText().toString();
        //Nov 7th, 2016 - http://stackoverflow.com/questions/13576470/converting-an-address-into-geopoint
        //Geocoder takes an string and finds an address that most closely resembles the string
        //then latitude and longitude is extracted from the address
        Geocoder coder = new Geocoder(this, Locale.CANADA);
        try {
            List<Address> startAddress = coder.getFromLocationName(startLocation, 1);
            startLat = startAddress.get(0).getLatitude();
            startLong = startAddress.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            List<Address> endAddress = coder.getFromLocationName(endLocation, 1);
            endLat = endAddress.get(0).getLatitude();
            endLong = endAddress.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startPoint = new GeoPoint(startLong, startLat);
        endPoint = new GeoPoint(endLong, endLat);
        double fare = getFareEstimate(startPoint, endPoint);
        editFare.setText(Double.toString(fare));                //brings out fare estimate to users
        mapController.setCenter(startPoint);
        // to get a key http://developer.mapquest.com/
        roadManager = new MapQuestRoadManager("--");
        ArrayList<OverlayItem> overlayItemArray = new ArrayList<>();
        overlayItemArray.add(new OverlayItem("Starting Point", "This is the starting point", startPoint));
        overlayItemArray.add(new OverlayItem("Destination", "This is the destination point", endPoint));
        getRoadAsync();
        PostListOfflineController pOffC = new PostListOfflineController();
        PostListOnlineController.AddPostsTask addPostOnline = new PostListOnlineController.AddPostsTask();
        Post newPost = new Post(startPoint, endPoint, Double.toString(fare), CurrentUser.getCurrentUser());
        pOffC.addOfflinePost(newPost, this);
        addPostOnline.execute(newPost);
        Toast.makeText(this, "Request Made", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void getRoadAsync() {
        mRoads = null;
        GeoPoint roadStartPoint = startPoint;
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>(2);
        waypoints.add(roadStartPoint);
        waypoints.add(endPoint);
        new UpdateRoadTask().execute(waypoints);
    }
    private class UpdateRoadTask extends AsyncTask<Object, Void, Road[]> {
        protected Road[] doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> waypoints = (ArrayList<GeoPoint>) params[0];
            return roadManager.getRoads(waypoints);
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
            for (int i = 0; i < roads.length; i++) {
                Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[i]);
                mRoadOverlays[i] = roadPolyline;
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
    private double getFareEstimate(GeoPoint startPoint, GeoPoint endPoint) {
        Location from = new Location("start");
        Location to = new Location("end");
        from.setLatitude(startPoint.getLatitude());
        from.setLongitude(startPoint.getLongitude());
        to.setLatitude(endPoint.getLatitude());
        to.setLongitude(endPoint.getLongitude());
        double distance = from.distanceTo(to);
        double fare = distance * 1.5; //will most probably do something else for the estimate
        return fare;
    }
}