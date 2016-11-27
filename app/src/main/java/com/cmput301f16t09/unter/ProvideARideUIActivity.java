package com.cmput301f16t09.unter;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Provide A Ride UI Activity is where the user sees all the Requests made (minus his/her own)
 * and can choose to make a Ride offer for a Request
 * How to do nav bars
 * http://blog.teamtreehouse.com/add-navigation-drawer-android
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Provide A Ride");
        setContentView(R.layout.activity_provide_aride_ui);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nvView);
        mActivityTitle = getTitle().toString();

        tvSearch = (TextView) findViewById(R.id.ProvideARideSearch);

        addDrawerItems();
        setupDrawer();
//        setUpNavigationView();

        ListView currentPostList = (ListView) findViewById(R.id.listViewProvideARide);

        for(Post p : PostListOfflineController.getPostList(ProvideARideUIActivity.this).getPosts()) {
            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                    (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                    p.getStatus().equals("Pending Approval")) {
                postList.addPost(p);
            }
        }

        adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

            // Create the view for the habits. Habits name is red if it has not been completed before
            // and green if it has been completed.
            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                String startLocation = postList.getPost(position).getStartAddress();
                String endLocation = postList.getPost(position).getEndAddress();
                // Remove forTestUsername after
                tv.setText("Start: " + startLocation +"\nEnd: " + endLocation);

//                tv.setText(postList.getPost(position).getUsername());
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

                // Why is this for loop here? -- Added in (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                // To ensure that the posts with the user offer in it is not in the list
                // Can probably change up to be faster?
                for(Post p : PostListOfflineController.getPostList(ProvideARideUIActivity.this).getPosts()) {
                    if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                            (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                            p.getStatus().equals("Pending Approval")) {
                        postList.addPost(p);
                    }
                }
                for (Post p : postList.getPosts()) {
                    if (CurrentUser.getCurrentPost().getId().equals(p.getId())) {
                        CurrentUser.setCurrentPost(p);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    Intent intent = new Intent(ProvideARideUIActivity.this, RequestDetailsUIActivity.class);
                    adapter.notifyDataSetChanged();
                    startActivity(intent);
                }
                else {
                    CurrentUser.setCurrentPost(null);
                    Toast.makeText(ProvideARideUIActivity.this, "Selected Post Request Unavailable. Updated List.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        PostListOfflineController.getPostList(ProvideARideUIActivity.this).addListener(new Listener() {
            @Override
            public void update()
            {
                postList.getPosts().clear();

                for(Post p : PostListOfflineController.getPostList(ProvideARideUIActivity.this).getPosts()) {
                    if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                            (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                             p.getStatus().equals("Pending Approval")) {
                        postList.addPost(p);
                    }
                }

                adapter.notifyDataSetChanged();
                PostListOfflineController.saveOfflinePosts(ProvideARideUIActivity.this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        postList.getPosts().clear();
        for(Post p : PostListOfflineController.getPostList(ProvideARideUIActivity.this).getPosts()) {
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


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

    private void addDrawerItems() {
        String[] osArray = { "Keyword", "Geolocation", "Fare", "Fare/km", "Address" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Toast.makeText(ProvideARideUIActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    dialog.setTitle("Search by Keyword");
                    dialog.setContentView(R.layout.dialog_search_keyword);
                    dialog.show();

                    final EditText editText = (EditText) dialog.findViewById(R.id.searchKeyword);
                    Button searchKeywordConfirm = (Button) dialog.findViewById(R.id.searchKeywordConfirm);
                    Button searchKeywordCancel = (Button) dialog.findViewById(R.id.searchKeywordCancel);

                    searchKeywordConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           String keyword = editText.getText().toString();
                            if (keyword.length()==0) {
                                // add toast
                            }
                            tvSearch.setText("Searching by Keyword: " + keyword);
                                // Implement search method

                            onBackPressed();
                            dialog.cancel();
                        }
                    });

                    searchKeywordCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                } else if (position == 1) {
                    Dialog dialog = new Dialog(ProvideARideUIActivity.this);
                    dialog.setTitle("Search by Keyword");
                    dialog.setContentView(R.layout.dialog_search_keyword);
                    dialog.show();

                    final EditText editText = (EditText) dialog.findViewById(R.id.searchKeyword);
                    Button searchKeywordConfirm = (Button) dialog.findViewById(R.id.searchKeywordConfirm);
                    Button searchKeywordCancel = (Button) dialog.findViewById(R.id.searchKeywordCancel);

                } else if (position == 2) {

                } else if (position == 3) {
                } else {
                } //position ==4

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
