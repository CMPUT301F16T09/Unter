package com.cmput301f16t09.unter;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide A Ride UI Activity is where the user sees all the Requests made (minus his/her own)
 * and can choose to make a Ride offer for a Request
 *
 */
public class ProvideARideUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ArrayAdapter<Post> adapter;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private TextView tvSearch;
    private ArrayList<Post> searchList = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Provide A Ride");
        setContentView(R.layout.activity_provide_aride_ui);

        //Creating Nav bars, Accessed November 26
        //http://blog.teamtreehouse.com/add-navigation-drawer-android
        //Set up Nav bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nvView);
        mActivityTitle = getTitle().toString();

        tvSearch = (TextView) findViewById(R.id.ProvideARideSearch);

        //Add the Drawer items and set it up
        addDrawerItems();
        setupDrawer();

        ListView currentPostList = (ListView) findViewById(R.id.listViewProvideARide);

        // Grab the items for the listview showing it to the user
        for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                    (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                    p.getStatus().equals("Pending Approval")) {
                postList.addPost(p);
            }
        }

        adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

            // Create the view for the posts
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Grab Starting and ending addresses
                String startLocation = postList.getPost(position).getStartAddress();
                String endLocation = postList.getPost(position).getEndAddress();

                // Set the format of text displayed to the user
                tv.setText("Start: " + startLocation +"\nEnd: " + endLocation);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(24);

                return view;
            }
        };

        currentPostList.setAdapter(adapter);

        currentPostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CurrentUser.setCurrentPost(postList.getPost(position));
                Boolean found = false;
                postList.getPosts().clear();

                // Cycle through and grab updated list from elastic search
                for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
                    if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                            (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                            p.getStatus().equals("Pending Approval")) {
                        postList.addPost(p);
                    }
                }

                //Ensure that the post exists
                for (Post p : postList.getPosts()) {
                    if (CurrentUser.getCurrentPost().getId().equals(p.getId())) {
                        CurrentUser.setCurrentPost(p);
                        found = true;
                        break;
                    }
                }
                //If so, show the user
                if (found) {
                    Intent intent = new Intent(ProvideARideUIActivity.this, RequestDetailsUIActivity.class);
                    startActivity(intent);
                }
                //If not, refresh the list
                else {
                    CurrentUser.setCurrentPost(null);
                    Toast.makeText(ProvideARideUIActivity.this, "Selected Post Request Unavailable. Updated List.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Only update the list if the user is not on a search
    // However, will update ListView if user offer rides
    @Override
    public void onResume() {
        super.onResume();
        if (searchList.isEmpty()) {
            resetPosts();
        }
        else {
            postList.getPosts().clear();
            for (Post p : searchList) {
                if (!CurrentUser.getCurrentUser().getMyOffers().contains(p.getId())) {
                    postList.addPost(p);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Reset posts. Clear the postList and retrieve a new updated copy
     */
    public void resetPosts() {
        postList.getPosts().clear();
        for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                    (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                    p.getStatus().equals("Pending Approval")) {
                postList.addPost(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // Back Button if Nav Bar is open
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }

    // Nav Bar configurations
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    // Nav Bar configurations
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Nav Bar configurations for drawer
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Search Options");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Provide A Ride");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    // Adding Draw items for Nav Bar
    private void addDrawerItems() {
        String[] searchOptions = {"Keyword", "Geolocation", "Fare", "Fare/km", "Address", "Show all open requests"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchOptions) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);
                tv.setTextSize(20);

                return view;
            };
        };
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Reference video from
                // https://www.youtube.com/watch?v=_ANXdk_V71s
                // Nov 26, 2016
                if (position == 0) { //Keyword search
                    final Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    final EditText editKeyword = (EditText) dialog.findViewById(R.id.searchKeyword);
                    final Button searchKeywordCancel = (Button) dialog.findViewById(R.id.searchKeywordCancel);

                    dialog.setTitle("Search by Keyword");
                    dialog.setContentView(R.layout.dialog_search_keyword);
                    dialog.show();

                    Button searchKeywordConfirm = (Button) dialog.findViewById(R.id.searchKeywordConfirm);

                    searchKeywordConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String keyword = editKeyword.getText().toString();
                            if (keyword.length()==0) {
                                // Toast for invalid search
                                Toast.makeText(ProvideARideUIActivity.this, "Invalid search", Toast.LENGTH_SHORT).show();

                            } else {
                                tvSearch.setText("Searching by Keyword:\nKeyword is: " + keyword);
                                // Search method for keyword
                                Toast.makeText(ProvideARideUIActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
                                try {
                                    PostListOnlineController.SearchKeywordTask searchKeywordTask = new PostListOnlineController.SearchKeywordTask();
                                    searchKeywordTask.execute(keyword);
                                    ArrayList<Post> tempPostlist = searchKeywordTask.get();

                                    // Clear update list and append new search
                                    postList.getPosts().clear();
                                    postList.getPosts().addAll(tempPostlist);

                                    // Set the searchList for if the user is searching
                                    searchList.clear();
                                    searchList.addAll(postList.getPosts());

                                    adapter.notifyDataSetChanged();
                                }
                                catch (Exception e) {
                                    Log.i("Error", "Error searching for keyword");
                                }
                                onBackPressed();
                                dialog.cancel();
                            }
                        }
                    });

                    searchKeywordCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProvideARideUIActivity.this, "Cancelling search.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                } else if (position == 1) { //Geolocation search
                    final Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    final EditText editGeoLat = (EditText) dialog.findViewById(R.id.searchGeolocationLat);
                    final EditText editGeoLong = (EditText) dialog.findViewById(R.id.searchGeolocationLong);
                    final EditText editGeoRadius = (EditText) dialog.findViewById(R.id.searchGeolocationRadius);

                    dialog.setTitle("Search by Geolocation");
                    dialog.setContentView(R.layout.dialog_search_geolocation);
                    dialog.show();

                    Button searchGeolocationConfirm = (Button) dialog.findViewById(R.id.searchGeolocationConfirm);
                    Button searchGeolocationCancel = (Button) dialog.findViewById(R.id.searchGeolocationCancel);

                    searchGeolocationConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Set up initial values
                            Double geoLat = 0.0;
                            Double geoLong = 0.0;
                            Double geoRadius = 0.0;
                            boolean isNumeric = true;

                            // Try to convert, if not set isNumeric to false
                            try {
                                geoLat = Double.parseDouble(editGeoLat.getText().toString());
                                geoLong = Double.parseDouble(editGeoLong.getText().toString());
                                geoRadius = Double.parseDouble(editGeoRadius.getText().toString());

                            } catch (Exception e){
                                isNumeric = false;
                            }

                            // If editGeoLat and editGeoLong are valid
                            if (isNumeric) {
                                Toast.makeText(ProvideARideUIActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
                                tvSearch.setText("Searching by Geolocation: \nWithin " + geoRadius + "m" + " of "
                                        + "\nLAT: "+ geoLat + "\nLONG: " + geoLong);

                                Location a = new Location("a");
                                a.setLatitude(geoLat);
                                a.setLongitude(geoLong);
                                try {
                                    PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                                    searchPostListsTask.execute("status", "Pending Approval");
                                    ArrayList<Post> tempPostList = searchPostListsTask.get();

                                    postList.getPosts().clear();

                                    // Obtain new posts that correspond to search
                                    for(Post p : tempPostList) {
                                        if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                                                (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername()))) {
                                            Location b = new Location("b");
                                            b.setLatitude(p.getStartLocation().getLatitude());
                                            b.setLongitude(p.getStartLocation().getLongitude());
                                            Float distance = a.distanceTo(b);
                                            if (distance < geoRadius) {
                                                postList.addPost(p);
                                            }
                                        }
                                    }

                                    // Set the searchList for if the user is searching
                                    searchList.clear();
                                    searchList.addAll(postList.getPosts());

                                    adapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                }
                                onBackPressed();
                                dialog.cancel();

                            } else {
                                // Invalid Toast
                                Toast.makeText(ProvideARideUIActivity.this, "Invalid search", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    searchGeolocationCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProvideARideUIActivity.this, "Search cancelled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                } else if (position == 2) { // Fare searching
                    final Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    final EditText editMinFare = (EditText) dialog.findViewById(R.id.searchMinFare);
                    final EditText editMaxFare = (EditText) dialog.findViewById(R.id.searchMaxFare);

                    dialog.setTitle("Search by Fare");
                    dialog.setContentView(R.layout.dialog_search_fare);
                    dialog.show();

                    Button searchFareConfirm = (Button) dialog.findViewById(R.id.searchFareConfirm);
                    Button searchFareCancel = (Button) dialog.findViewById(R.id.searchFareCancel);

                    searchFareConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Set inital values
                            Double minFare = 0.0;
                            Double maxFare = 1000000.0;

                            // If user did not enter any values to either fields
                            if (editMaxFare.getText().toString().equals("") && editMinFare.getText().toString().equals("")) {
                                Toast.makeText(ProvideARideUIActivity.this, "Invalid search, no fields entered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                boolean isNumeric = true;
                                // Convert values entered by user
                                if (editMaxFare.getText().toString().equals("")) {
                                    try {
                                        minFare = Double.parseDouble(editMinFare.getText().toString());
                                    } catch (Exception e){
                                        isNumeric = false;
                                    }
                                }
                                else if (editMinFare.getText().toString().equals("")) {
                                    try {
                                        maxFare = Double.parseDouble(editMaxFare.getText().toString());
                                    } catch (Exception e){
                                        isNumeric = false;
                                    }
                                }
                                else {
                                    try {
                                        maxFare = Double.parseDouble(editMaxFare.getText().toString());
                                        minFare = Double.parseDouble(editMinFare.getText().toString());
                                    } catch (Exception e){
                                        isNumeric = false;
                                    }
                                }
                                // If values entered by user is valid then..
                                if (isNumeric) {

                                    // Check if the min value is less than the max value
                                    if (minFare <= maxFare) {
                                        try {
                                            if (editMaxFare.getText().toString().equals("")) {
                                                tvSearch.setText("Searching for Fare: \n" + "Minimum fare: $" + minFare);
                                            }
                                            else if (editMinFare.getText().toString().equals("")) {
                                                tvSearch.setText("Searching for Fare: \n" + "Maximum fare: $" + maxFare);
                                            }
                                            else {
                                                tvSearch.setText("Searching for Fare: \n" + "Minimum fare: $" +
                                                        minFare + "\nMaximum fare: $" + maxFare);
                                            }
                                            // Update the ListView shown to the user for they search for
                                            postList.getPosts().clear();
                                            PostListOnlineController.SearchPostListsRangeTask searchPostListsRangeTask = new PostListOnlineController.SearchPostListsRangeTask();
                                            searchPostListsRangeTask.execute("fare", minFare.toString(), maxFare.toString());
                                            ArrayList<Post> tempPostList = searchPostListsRangeTask.get();

                                            postList.getPosts().addAll(tempPostList);

                                            // Set the searchList for if the user is searching
                                            searchList.clear();
                                            searchList.addAll(postList.getPosts());

                                            adapter.notifyDataSetChanged();
                                        } catch (Exception e) {}
                                        onBackPressed();
                                        dialog.cancel();
                                    }
                                    else {
                                        Toast.makeText(ProvideARideUIActivity.this, "Invalid search", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(ProvideARideUIActivity.this, "Invalid search, fields contain non-numeric characters", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    searchFareCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProvideARideUIActivity.this, "Search cancelled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                } else if (position == 3) { //Fare/km searching
                    final Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    final EditText editMinFareKM = (EditText) dialog.findViewById(R.id.searchMinFareKM);
                    final EditText editMaxFareKM = (EditText) dialog.findViewById(R.id.searchMaxFareKM);

                    dialog.setTitle("Search by Fare/km");
                    dialog.setContentView(R.layout.dialog_search_farekm);
                    dialog.show();

                    Button searchFareKMConfirm = (Button) dialog.findViewById(R.id.searchFareKMConfirm);
                    Button searchFareKMCancel = (Button) dialog.findViewById(R.id.searchFareKMCancel);

                    searchFareKMConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Set the initial values
                            Double minFareKM = 0.0;
                            Double maxFareKM = 1000000.0;

                            // If user did not enter any values to either fields
                            if (editMaxFareKM.getText().toString().equals("") && editMinFareKM.getText().toString().equals("")) {
                                Toast.makeText(ProvideARideUIActivity.this, "Invalid search, no fields entered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                boolean isNumeric = true;
                                // Convert values entered by user
                                if (editMaxFareKM.getText().toString().equals("")) {
                                    try {
                                        minFareKM = Double.parseDouble(editMinFareKM.getText().toString());
                                    } catch (Exception e){
                                        isNumeric = false;
                                    }
                                }
                                else if (editMinFareKM.getText().toString().equals("")) {
                                    try {
                                        maxFareKM = Double.parseDouble(editMaxFareKM.getText().toString());
                                    } catch (Exception e){
                                        isNumeric = false;
                                    }
                                }
                                else {
                                    try {
                                        maxFareKM = Double.parseDouble(editMaxFareKM.getText().toString());
                                        minFareKM = Double.parseDouble(editMinFareKM.getText().toString());
                                    } catch (Exception e){
                                        isNumeric = false;
                                    }
                                }
                                // If values entered by user is valid then..
                                if (isNumeric) {

                                    // Check if the min value is less than the max value
                                    if (minFareKM <= maxFareKM) {
                                        try {
                                            if (editMaxFareKM.getText().toString().equals("")) {
                                                tvSearch.setText("Searching for Fare/km: \n" + "Minimum fare/km: $" + minFareKM);
                                            }
                                            else if (editMinFareKM.getText().toString().equals("")) {
                                                tvSearch.setText("Searching for Fare/km: \n" + "Maximum fare/km: $" + maxFareKM);
                                            }
                                            else {
                                                tvSearch.setText("Searching for Fare/km: \n" + "Minimum fare/km: $" +
                                                        minFareKM + "\nMaximum fare/km: $" + maxFareKM);
                                            }
                                            // Update the ListView shown to the user for they search for
                                            postList.getPosts().clear();
                                            PostListOnlineController.SearchPostListsRangeTask searchPostListsRangeTask = new PostListOnlineController.SearchPostListsRangeTask();
                                            searchPostListsRangeTask.execute("fareKM", minFareKM.toString(), maxFareKM.toString());
                                            ArrayList<Post> tempPostList = searchPostListsRangeTask.get();

                                            postList.getPosts().addAll(tempPostList);

                                            // Set the searchList for if the user is searching
                                            searchList.clear();
                                            searchList.addAll(postList.getPosts());

                                            adapter.notifyDataSetChanged();
                                        } catch (Exception e) {}
                                        onBackPressed();
                                        dialog.cancel();
                                    }
                                    else {
                                        Toast.makeText(ProvideARideUIActivity.this, "Invalid search", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(ProvideARideUIActivity.this, "Invalid search, fields contain non-numeric characters", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    searchFareKMCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProvideARideUIActivity.this, "Search cancelled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                } else if (position == 4){ //Address searching
                    final Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    final EditText editAddress = (EditText) dialog.findViewById(R.id.searchAddress);
                    final EditText editAddressRadius = (EditText) dialog.findViewById(R.id.searchAddressRadius);

                    dialog.setTitle("Search by Address");
                    dialog.setContentView(R.layout.dialog_search_address);
                    dialog.show();

                    Button searchAddressConfirm = (Button) dialog.findViewById(R.id.searchAddressConfirm);
                    Button searchAddressCancel = (Button) dialog.findViewById(R.id.searchAddressCancel);

                    searchAddressConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (true) {
                                Toast.makeText(ProvideARideUIActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
                                tvSearch.setText("Searching for Address:\nSearching within " + editAddressRadius.getText().toString() + "m"
                                                    + " of\n" + editAddress.getText().toString());
                                // Set inital values
                                boolean isNumeric = true;
                                Double radius = 0.0;

                                // Try to convert the radius to double
                                try {
                                    radius = Double.parseDouble(editAddressRadius.getText().toString());
                                } catch(Exception e) {
                                    isNumeric = false;
                                }
                                // If valid number from user
                                if (isNumeric) {
                                    try {
                                        // Set the Geocoder address of inputted address
                                        Geocoder coder = new Geocoder(ProvideARideUIActivity.this);
                                        List<Address> addresses = coder.getFromLocationName(editAddress.getText().toString(), 1);
                                        Location a = new Location("a");

                                        a.setLatitude(addresses.get(0).getLatitude());
                                        a.setLongitude(addresses.get(0).getLongitude());

                                        PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                                        searchPostListsTask.execute("status", "Pending Approval");
                                        ArrayList<Post> tempPostList = searchPostListsTask.get();
                                        postList.getPosts().clear();

                                        // Cycle through the posts and add it in if it
                                        // is within the range of the radius
                                        for(Post p : tempPostList) {
                                            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                                                    (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername()))) {
                                                // Set the Location from the post
                                                Location b = new Location("b");
                                                b.setLatitude(p.getStartLocation().getLatitude());
                                                b.setLongitude(p.getStartLocation().getLongitude());

                                                // Find the float distance
                                                Float distance = a.distanceTo(b);
                                                if (distance < (radius)) {
                                                    postList.addPost(p);
                                                }
                                            }
                                        }

                                        // Set the searchList for if the user is searching
                                        searchList.clear();
                                        searchList.addAll(postList.getPosts());

                                        adapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                    }
                                    onBackPressed();
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(ProvideARideUIActivity.this, "Invalid search radius", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(ProvideARideUIActivity.this, "Invalid search address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    searchAddressCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            Toast.makeText(ProvideARideUIActivity.this, "Search cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{// Default for showing all of the posts
                    Toast.makeText(ProvideARideUIActivity.this, "Showing all ride requests", Toast.LENGTH_SHORT).show();
                    searchList.clear();
                    resetPosts();
                    tvSearch.setText("All available Requests");
                    getSupportActionBar().setTitle("Provide A Ride");
                    onBackPressed();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
