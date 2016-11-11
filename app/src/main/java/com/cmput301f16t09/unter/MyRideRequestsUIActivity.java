package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyRideRequestsUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private Geocoder coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_requests_ui);

        ListView currentPostList;
        coder = new Geocoder(this, Locale.CANADA);

        currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);

        PostListOfflineController.getPostList(MyRideRequestsUIActivity.this);

        // Get All posts for the specific user
        for(Post p : PostListOfflineController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        final ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String forTestUsername = postList.getPost(position).getUsername();

//                double startLat = postList.getPost(position).getStartLocation().getLatitude();
//                double startLon = postList.getPost(position).getStartLocation().getLongitude();
//                double endLat = postList.getPost(position).getEndLocation().getLatitude();
//                double endLon = postList.getPost(position).getEndLocation().getLongitude();
//
//                try {
//                    List<Address> startAddress = coder.getFromLocation(startLat, startLon, 1);
//                    List<Address> endAddress = coder.getFromLocation(endLat, endLon, 1);
//                    tv.setText("Username: " + forTestUsername + "\nStart: " + startAddress.get(0).getAddressLine(0) +"\nEnd: " + endAddress.get(0).getAddressLine(0));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                String startLocation = postList.getPost(position).getStartLocation().toString();
                String endLocation = postList.getPost(position).getEndLocation().toString();

                tv.setText("Username: " + forTestUsername + "\nStart: " + startLocation +"\nEnd: " + endLocation);


                // Remove forTestUsername after
//                tv.setText(postList.getPost(position).getUsername());
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(24);
                return view;
            }
        };

        currentPostList.setAdapter(adapter);

        currentPostList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int pos ,long id){
                Intent RiderRequestPreIntent = new Intent(MyRideRequestsUIActivity.this,
                                                        RidersRequestDetailsPreUIActivity.class);
                Intent RiderRequestPostIntent = new Intent(MyRideRequestsUIActivity.this,
                        RidersRequestDetailsPostUIActivity.class);

                CurrentUser.setPost(postList.getPost(pos));
                if (CurrentUser.getPost().getDriver() == null) {
                    startActivity(RiderRequestPreIntent);
                }
                else {
                    startActivity(RiderRequestPostIntent);
                }

            }
        });

        currentPostList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                final Post postToRemove = postList.getPosts().get(pos);
                createDeletionDialog(postToRemove);
                return true;
            }
        });

        PostListOfflineController.getPostList(MyRideRequestsUIActivity.this).addListener(new Listener() {
            @Override
            public void update()
            {
                postList.getPosts().clear();

                for(Post p : PostListOfflineController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
                    if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                        postList.addPost(p);
                    }
                }

                adapter.notifyDataSetChanged();
                PostListOfflineController.saveOfflinePosts(MyRideRequestsUIActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void createDeletionDialog(Post post) {

        final Post postToRemove = post;

        // Build the dialog
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MyRideRequestsUIActivity.this);
        deleteDialog.setMessage("Delete This Post?");
        deleteDialog.setCancelable(true);
        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            // Don't do anything if Cancel is clicked, which closes the dialog
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // If delete is pressed
        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    PostListOfflineController.getPostList(MyRideRequestsUIActivity.this).deletePost(postToRemove);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Save this change of data into FILENAME
                PostListOfflineController.saveOfflinePosts(MyRideRequestsUIActivity.this);

                Toast.makeText(MyRideRequestsUIActivity.this, "Post Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        deleteDialog.show();
    }
}