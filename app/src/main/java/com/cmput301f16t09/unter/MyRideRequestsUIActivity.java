package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The type My ride requests ui activity.
 */
public class MyRideRequestsUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ArrayAdapter<Post> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Viewing My Ride Requests");
        setContentView(R.layout.activity_my_ride_requests_ui);

        ListView currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);

         //Get All posts for the specific user
        for(Post p : PostListMainController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

            // Set the view
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                String startLocation = postList.getPost(position).getStartAddress();
                String endLocation = postList.getPost(position).getEndAddress();

                tv.setText("Start: " + startLocation +"\nEnd: " + endLocation);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(20);
                return view;
            }
        };

        currentPostList.setAdapter(adapter);

        currentPostList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int pos ,long id){

                CurrentUser.setCurrentPost(postList.getPost(pos));
                if (CurrentUser.getCurrentPost().getDriver() == null) {
                    Intent RiderRequestPreIntent = new Intent(MyRideRequestsUIActivity.this,
                            RidersRequestDetailsPreUIActivity.class);
                    startActivity(RiderRequestPreIntent);
                }
                else {
                    Intent RiderRequestPostIntent = new Intent(MyRideRequestsUIActivity.this,
                            RidersRequestDetailsPostUIActivity.class);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the list of new posts
        postList.getPosts().clear();
        for(Post p : PostListMainController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Create deletion dialog.
     *
     * @param post the post
     */
    public void createDeletionDialog(Post post) {

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
                    PostListOnlineController.DeletePostsTask deletePostsTask = new PostListOnlineController.DeletePostsTask();
                    deletePostsTask.execute(CurrentUser.getCurrentPost());
                    deletePostsTask.get();

                    UserListOnlineController.UpdateUsersTask updateUsersTask = new UserListOnlineController.UpdateUsersTask();
                    updateUsersTask.execute(CurrentUser.getCurrentUser());
                    updateUsersTask.get();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Save this change of data into FILENAME
                PostListMainController.updateMainOfflinePosts(MyRideRequestsUIActivity.this);

                // Update the list of new posts
                postList.getPosts().clear();
                for(Post p : PostListMainController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
                    if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                        postList.addPost(p);
                    }
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(MyRideRequestsUIActivity.this, "Post Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        deleteDialog.show();
    }
}