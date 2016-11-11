package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyRideRequestsUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_requests_ui);

        currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);
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

        currentPostList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                final Post postToRemove = postList.getPosts().get(pos);
                createDeletionDialog(postToRemove);
                return true;
            }
        });

        // Add a listener and define the update function to refresh the habits list when there
        // is a change in the dataset, then save the data to FILENAME
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

//    @Override
//    protected void onStart() {
//
//        super.onStart();
//        adapter = new ArrayAdapter<Post>(this, R.layout.list_item, postList.getPosts());
//        currentPostList.setAdapter(adapter);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void createDeletionDialog(Post post) {
        // Get the habit
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
                // Remove the habit from HabitList
                try {
                    PostListOfflineController.getPostList(MyRideRequestsUIActivity.this).deletePost(postToRemove);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Save this change of data into FILENAME
                PostListOfflineController.saveOfflinePosts(MyRideRequestsUIActivity.this);

                // Toast that the habit was deleted
                Toast.makeText(MyRideRequestsUIActivity.this, "Post Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        deleteDialog.show();
    }
}