package com.cmput301f16t09.unter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * The type My ride offers ui activity allows the user to view the offers made to multiple posts.
 */
public class MyRideOffersUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();

    /** Called when the activity is created. */
    /**
     * Creates the screen where drivers can view the offers they have made. The screen contains a
     * ListView that shows all of the offers. Clicking on the offer will change activities and
     * provide more details for the offer.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Viewing My Ride Offers");
        setContentView(R.layout.activity_my_ride_offers_ui);

        ListView currentPostList = (ListView) findViewById(R.id.listViewMyRideOffers);

        /**
         * Get all of the posts and filter for the username of the CurrentUser to only display the
         * posts that correlate to the user.
         *
         * @see CurrentUser
         * @see PostList
         * @see PostListOnlineController
         * @see PostListOfflineController
         */

        for (Post p : PostListMainController.getPostList(MyRideOffersUIActivity.this).getPosts()) {
            if (p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        /**
         * Adapter for posts that will be linked to the ListView for displaying posts
         */
        final ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                String startLocation = postList.getPost(position).getStartAddress();
                String endLocation = postList.getPost(position).getEndAddress();
                String forTestUsername = postList.getPost(position).getUsername();

                tv.setText("Username: " + forTestUsername + "\nStart: " + startLocation + "\nEnd: " + endLocation);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(20);
                return view;
            }
        };

        // Set the adapter to the listview
        currentPostList.setAdapter(adapter);

        /**
         * If a post is clicked, store the post in CurrentUser and change activities to
         * RideOfferDetailsUIActivity.
         * @see CurrentUser
         * @see RideOfferDetailsUIActivity
         */
        currentPostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                CurrentUser.setCurrentPost(postList.getPost(pos));
                Intent RideOfferDetailsIntent = new Intent(MyRideOffersUIActivity.this,
                        RideOfferDetailsUIActivity.class);
                startActivity(RideOfferDetailsIntent);
            }
        });

    }
}
