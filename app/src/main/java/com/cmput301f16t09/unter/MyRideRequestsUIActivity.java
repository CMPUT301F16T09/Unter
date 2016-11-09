package com.cmput301f16t09.unter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyRideRequestsUIActivity extends AppCompatActivity {

    private PostList postList= new PostList();
    private ArrayAdapter<Post> adapter;
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_requests_ui);

        currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);

        PostListOfflineController ploc = new PostListOfflineController();
        postList = ploc.loadOfflinePosts(this);
    }

    @Override
    protected void onStart() {

        super.onStart();
        adapter = new ArrayAdapter<Post>(this, R.layout.list_item, postList.getPosts());
        currentPostList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_ride_requests_ui);

//        postList.getPosts().clear();
//        PostListOnlineController.SearchPostListsTask searchUserPosts = new PostListOnlineController.SearchPostListsTask();
//        searchUserPosts.execute("username", CurrentUser.getCurrentUser().getUsername());
//        try {
//            postList.setPostList(searchUserPosts.get());
//        }
//        catch (Exception e) {
//            Log.i("Error", "Loading failed");
//        }
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    protected void onStart() {
//
//        super.onStart();
//        PostListOnlineController.SearchPostListsTask searchUserPosts = new PostListOnlineController.SearchPostListsTask();
//        searchUserPosts.execute("username", CurrentUser.getCurrentUser().getUsername());
//        try {
//            postList.setPostList(searchUserPosts.get());
//        }
//        catch (Exception e) {
//            Log.i("Error", "Loading failed");
//        }
//        adapter = new ArrayAdapter<Post>(this, R.layout.list_item, postList.getPosts());
//        currentPostList.setAdapter(adapter);
//    }