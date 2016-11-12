package com.cmput301f16t09.unter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyRideOffersUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_offers_ui);

        currentPostList = (ListView) findViewById(R.id.listViewMyRideOffers);
        // Get All posts for the specific user
        for(Post p : PostListOfflineController.getPostList(MyRideOffersUIActivity.this).getPosts()) {
            if (p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        final ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

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

        currentPostList.setAdapter(adapter);

        currentPostList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int pos ,long id){

                CurrentUser.setCurrentPost(postList.getPost(pos));
                Intent RideOfferDetailsIntent = new Intent(MyRideOffersUIActivity.this,
                        RideOfferDetailsUIActivity.class);
                startActivity(RideOfferDetailsIntent);
            }
        });

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

        currentPostList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int currPost = i;
                CurrentUser.setCurrentPost(postList.getPost(currPost));

                Intent intent = new Intent(MyRideOffersUIActivity.this,RideOfferDetailsUIActivity.class);
                startActivity(intent);

                return false;
            }
        });



    }//oncreate


}//class
