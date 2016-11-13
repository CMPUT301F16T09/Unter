package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailsUIActivity extends AppCompatActivity {

    // If the polyline disappears with zooming
    // http://stackoverflow.com/questions/35799907/route-polyline-dissapears-after-zoom-in-osmbonuspack
    RoadManager roadManager;
    MapView map;
    Activity myActivity = this;
    Road[] mRoads;
    GeoPoint startPoint;
    GeoPoint endPoint;
    IMapController mapController;

    TextView poster;
    TextView start_Location;
    TextView end_Location;
    TextView fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details_ui);

        poster = (TextView) findViewById(R.id.RequestDetailsPostedByRiderName);
        poster.setText(CurrentUser.getCurrentPost().getUsername());

        start_Location = (TextView) findViewById(R.id.RequestDetailsStartLocationName);
        start_Location.setText(CurrentUser.getCurrentPost().getStartAddress());

        end_Location = (TextView) findViewById(R.id.RequestDetailsEndLocationName);
        end_Location.setText(CurrentUser.getCurrentPost().getEndAddress());

        fare = (TextView) findViewById(R.id.RequestDetailsCost);
        fare.setText(CurrentUser.getCurrentPost().getFare());

        map = (MapView) findViewById(R.id.RequestDetailsMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();

        GeoPoint edmPoint = new GeoPoint(53.5444, -113.4909);
        mapController.setZoom(15);
        mapController.setCenter(edmPoint);

        startPoint = CurrentUser.getCurrentPost().getStartLocation();
        endPoint = CurrentUser.getCurrentPost().getEndLocation();

        // to get a key http://developer.mapquest.com/
        //roadManager = new MapQuestRoadManager("xPGrfmORuC6QJMSkF6SXGKYbBgTefNdm");
        roadManager = new OSRMRoadManager(myActivity);

        ArrayList<OverlayItem> overlayItemArray;
        overlayItemArray = new ArrayList<>();
        overlayItemArray.add(new OverlayItem("Starting Point", "This is the starting point", startPoint));
        overlayItemArray.add(new OverlayItem("Destination", "This is the detination point", endPoint));
        getRoadAsync();

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailsUIActivity.this,ViewProfileUIActivity.class);
                intent.putExtra("User",CurrentUser.getCurrentPost().getUsername());
                startActivity(intent);
            }
        });
    }

    public void getRoadAsync() {
        mRoads = null;

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>(2);
        waypoints.add(startPoint);
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

    public void confirm_ride_request(View v) {
        Boolean found = false;
        PostList temp;
        for(Post p : PostListOfflineController.getPostList(RequestDetailsUIActivity.this).getPosts()) {
            // Prob don't need the first check if this if statement.
            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) && (p.getStatus().equals("Pending Offer") || p.getStatus().equals("Pending Approval")) && CurrentUser.getCurrentPost().getId().equals(p.getId())) {
                found = true;
                p.addDriverOffer(CurrentUser.getCurrentUser().getUsername());
                p.setStatus("Pending Approval");
                CurrentUser.setCurrentPost(p);
                break;
            }
        }
        if (found) {
            try {
                PostListOnlineController.UpdatePostsTask updatePostsTask = new PostListOnlineController.UpdatePostsTask();
                updatePostsTask.execute(CurrentUser.getCurrentPost());
                updatePostsTask.get();
//                PostListOfflineController.addOfflinePost(CurrentUser.getCurrentPost(), RequestDetailsUIActivity.this);
                CurrentUser.getCurrentUser().getMyOffers().addPost(CurrentUser.getCurrentPost());
                UserListOnlineController.UpdateUsersTask updateUserTask = new UserListOnlineController.UpdateUsersTask();
                updateUserTask.execute(CurrentUser.getCurrentUser());
                updateUserTask.get();
                Toast.makeText(RequestDetailsUIActivity.this, "Successfully sent the offer!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(RequestDetailsUIActivity.this, "Sorry, Could not update the database", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(RequestDetailsUIActivity.this, "Sorry, Post is unavailable to offer ride to.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
