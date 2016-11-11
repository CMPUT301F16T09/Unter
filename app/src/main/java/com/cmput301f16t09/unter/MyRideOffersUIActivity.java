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

public class MyRideOffersUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_offers_ui);

        currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);
        // Get All posts for the specific user
        for(Post p : PostListOfflineController.getPostList(MyRideOffersUIActivity.this).getPosts()) {
            if (p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

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
                String forTestUsername = postList.getPost(position).getUsername();
                tv.setText("Username: " + forTestUsername + "\nStart: " + startLocation +"\nEnd: " + endLocation);
                // tv.setText(postList.getPost(position).getUsername());
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        PostListOfflineController.getPostList(MyRideOffersUIActivity.this).addListener(new Listener() {
            @Override
            public void update()
            {
                postList.getPosts().clear();

                for(Post p : PostListOfflineController.getPostList(MyRideOffersUIActivity.this).getPosts()) {
                    if (p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) {
                        postList.addPost(p);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
