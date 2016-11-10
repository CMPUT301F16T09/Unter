package com.cmput301f16t09.unter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRideRequestsUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_requests_ui);

        currentPostList = (ListView) findViewById(R.id.listViewMyRideRequests);

        PostListOfflineController ploc = new PostListOfflineController();
        postList = ploc.loadOfflinePosts(this);


        final ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts())
        {

            // Create the view for the habits. Habits name is red if it has not been completed before
            // and green if it has been completed.
            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setText(postList.getPost(position).getUsername());
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        // Set the adapter to the HabitList habitList
        currentPostList.setAdapter(adapter);

        // Add a listener and define the update function to refresh the habits list when there
        // is a change in the dataset, then save the data to FILENAME
//        HabitListController.getHabitList().addListener(new HabitListener() {
//            @Override
//            public void update()
//            {
//                hlist.clear();
//                ArrayList<Habit> habits = list_of_habits.getHabits();
//                hlist.addAll(habits);
//                habitTrackerAdapter.notifyDataSetChanged();
//                saveHabits();
//            }
//        });
    }

//    @Override
//    protected void onStart() {
//
//        super.onStart();
//        adapter = new ArrayAdapter<Post>(this, R.layout.list_item, postList.getPosts());
//        currentPostList.setAdapter(adapter);
//    }

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