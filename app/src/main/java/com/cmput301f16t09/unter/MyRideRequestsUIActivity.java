package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

        Geocoder coder = new Geocoder(this, Locale.CANADA);

        ListView currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);

//         Get All posts for the specific user
        for(Post p : PostListMainController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

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

        // Add a listener and define the update function to refresh the habits list when there
        // is a change in the dataset, then save the data to FILENAME
//        PostListMainController.getPostList(MyRideRequestsUIActivity.this).addListener(new Listener() {
//            @Override
//            public void update()
//            {
//                postList.getPosts().clear();
//
//                PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
//                searchPostListsTask.execute(CurrentUser.getCurrentUser().getMyRequests().toString());
//                // App crashes when using postlist.getposts().clear(), or if there is no list clearing statement
//                try {
//                    for(Post p : searchPostListsTask.get()) {
//                        if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
//                            postList.addPost(p);
//                        }
//                    }
//                }
//                catch (Exception e){
//                    Log.i("Error", "Offline");
//                }
//
//
//                adapter.notifyDataSetChanged();
//                PostListMainController.updateMainOfflinePosts(ProvideARideUIActivity.this);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        postList.getPosts().clear();
        // Probably increase this -- if needed.
        for(Post p : PostListMainController.getPostList(MyRideRequestsUIActivity.this).getPosts()) {
            if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
                postList.addPost(p);
            }
        }

        // For faster searching later
//        PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
//        searchPostListsTask.execute(CurrentUser.getCurrentUser().getMyRequests().toString());
//        try {
//            for(Post p : searchPostListsTask.get()) {
//                if (p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
//                    postList.addPost(p);
//                }
//            }
//        }
//        catch (Exception e){
//            Log.i("Error", "Offline");
//        }
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }


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

//                    CurrentUser.getCurrentUser().getMyOffers().deletePost(CurrentUser.getCurrentPost());

                    UserListOnlineController.UpdateUsersTask updateUsersTask = new UserListOnlineController.UpdateUsersTask();
                    updateUsersTask.execute(CurrentUser.getCurrentUser());
                    updateUsersTask.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Save this change of data into FILENAME
                PostListMainController.updateMainOfflinePosts(MyRideRequestsUIActivity.this);
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