package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * This 'View' displays the updated information after a rider selects a driver
 * and can go on to complete the request, bringing up a dialog window to enable payment
 * options.
 *
 * There is a map here to show the route to be taken.
 */
public class RidersRequestDetailsPostUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    /**
     * The Map.
     */
    private MapView map;
    /**
     * The Start point.
     */
    private GeoPoint startPoint;
    /**
     * The End point.
     */
    private GeoPoint endPoint;

    /**
     * The Road manager.
     */
    private RoadManager roadManager;

    private Activity myActivity = this;
    /**
     * The roads to be used for the route
     */
    private Road[] mRoads;
    /**
     * The Map controller.
     */
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_post_ui);

        //Display request details for the user
        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPostCurrentStatus);
        String currentStatus = CurrentUser.getCurrentPost().getStatus().toString();
        tvCurrentStatus.setText(currentStatus);

        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPostStartLocationName);
        String startLocation = CurrentUser.getCurrentPost().getStartAddress();
        tvStartLocation.setText(startLocation);

        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPostEndLocationName);
        String endLocation = CurrentUser.getCurrentPost().getEndAddress();
        tvEndLocation.setText(endLocation);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPostOfferedFare);
        String offeredFare = CurrentUser.getCurrentPost().getFare().toString();
        tvOfferedFare.setText("$" + offeredFare);

        TextView tvDriverName = (TextView) findViewById(R.id.RideRequestDetailsPostDriverName);
        final String driverName = CurrentUser.getCurrentPost().getDriver().toString();
        SpannableString content = new SpannableString(driverName);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvDriverName.setText(content);

        map = (MapView) findViewById(R.id.RidersRequestDetailsPostMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        roadManager = new OSRMRoadManager(myActivity);

        startPoint = CurrentUser.getCurrentPost().getStartLocation();
        endPoint = CurrentUser.getCurrentPost().getEndLocation();

        mapController.setCenter(startPoint);
        mapController.setZoom(15);
        getRoadAsync();

        //Allows for the user to view the driver's user profile where they can phone or email them
        tvDriverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RidersRequestDetailsPostUIActivity.this,ViewProfileUIActivity.class);
                intent.putExtra("User",driverName);
                intent.putExtra("isRestricted", false);
                startActivity(intent);
            }
        });


        //Bring up dialog window for payment and also delete the post on elastic search once it has been completed
        Button complete_request = (Button) findViewById(R.id.RideRequestDetailsCompleteRequestButton);
        complete_request.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder paymentDialog = new AlertDialog.Builder(RidersRequestDetailsPostUIActivity.this);
                paymentDialog.setTitle("Payment Options");
                paymentDialog.setMessage("$"+ "  " + CurrentUser.getCurrentPost().getFare().toString()); //make this to two decimal places
                paymentDialog.setCancelable(false);

                paymentDialog.setPositiveButton("Complete Transaction", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CurrentUser.getCurrentPost().setStatus("Completed");

                        // Updating tasks to successfully remove from the post from all connected components
                        try {
                            UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                            searchUserListsTask.execute("username", driverName);
                            User driver = searchUserListsTask.get().get(0);

                            driver.getMyOffers().clear();

                            UserListOnlineController.UpdateUsersTask updateUserListstask = new UserListOnlineController.UpdateUsersTask();
                            updateUserListstask.execute(driver);
                            updateUserListstask.get();

                            PostListOnlineController.DeletePostsTask upt = new PostListOnlineController.DeletePostsTask();
                            upt.execute(CurrentUser.getCurrentPost());
                            upt.get();

                            CurrentUser.getCurrentUser().getMyRequests().clear();

                            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
                            uut.execute(CurrentUser.getCurrentUser());
                            uut.get();
                            Toast.makeText(RidersRequestDetailsPostUIActivity.this, "Completed request", Toast.LENGTH_SHORT).show();

                            PostListMainController.updateMainOfflinePosts(RidersRequestDetailsPostUIActivity.this);

                            //Bring to main menu ; clear android activity stack
                            Intent intent = new Intent(RidersRequestDetailsPostUIActivity.this,MainScreenUIActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        catch (Exception e) {
                            Log.i("Error", "Deletion upon completion Error");
                        }
                    }
                });
                paymentDialog.show();
            }
        });
    }

    /**
     * Taken from CMPUT 301 Fall 16 Lab 8 - Geolocation by Stephen Romansky
     * Calls upon the UpdateRoadTask to draw a given route in the Activities MapView
     * This task draws the route on the map
     */
    public void getRoadAsync() {
        mRoads = null;

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>(2);
        waypoints.add(startPoint);
        waypoints.add(endPoint);

        new UpdateRoadTask().execute(waypoints);
    }

    // Update Road tasks
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

            //Create the path on the map
            Polyline[] mRoadOverlays = new Polyline[roads.length];
            List<Overlay> mapOverlays = map.getOverlays();

            Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[0]);

            mRoadOverlays[0] = roadPolyline;
            String routeDesc = roads[0].getLengthDurationText(myActivity.getBaseContext(), -1);

            roadPolyline.setTitle(getString(R.string.app_name) + " - " + routeDesc);
            roadPolyline.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
            roadPolyline.setRelatedObject(0);

            //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
            //to avoid covering the other overlays.
            mapOverlays.add(0, roadPolyline);
            map.invalidate();
        }
    }
}
