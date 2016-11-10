package com.cmput301f16t09.unter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RidersRequestDetailsPreUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_pre_ui);
        currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);

        PostListOfflineController ploc = new PostListOfflineController();
        ploc.loadOfflinePosts(RidersRequestDetailsPreUIActivity.this);
        // Get All posts for the specific user
        for(Post p : ploc.getPostList().getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPreCurrentStatus);
        String currentStatus = CurrentUser.getPost().getStatus().toString();
        tvCurrentStatus.setText(currentStatus);

        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPreStartLocationName);
        String startLocation = CurrentUser.getPost().getStartLocation().toString();
        tvStartLocation.setText(startLocation);

        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPreEndLocationName);
        String endLocation = CurrentUser.getPost().getEndLocation().toString();
        tvEndLocation.setText(endLocation);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPreOfferedFare);
        String offeredFare = CurrentUser.getPost().getFare().toString();
        tvOfferedFare.setText(offeredFare);

        final ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

            // Create the view for the habits. Habits name is red if it has not been completed before
            // and green if it has been completed.
            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                String startLocation = postList.getPost(position).getStartLocation().toString();
                String endLocation = postList.getPost(position).getEndLocation().toString();
                // Remove forTestUsername after
//                String forTestUsername = postList.getPost(position).getDriverOffers();
//                tv.setText("Username: " + forTestUsername);
//                tv.setText(postList.getPost(position).getUsername());
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

    }

}
