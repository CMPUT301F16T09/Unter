package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Provide A Ride UI Activity is where the user sees all the Requests made (minus his/her own)
 * and can choose to make a Ride offer for a Request
 */
public class ProvideARideUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;
    private ArrayAdapter<Post> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_aride_ui);
;
        currentPostList = (ListView) findViewById(R.id.listViewProvideARide);

        for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                    (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                    (p.getStatus().equals("Pending Offer") || p.getStatus().equals("Pending Approval"))) {
                postList.addPost(p);
            }
        }

        adapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, postList.getPosts()) {

            // Create the view for the habits. Habits name is red if it has not been completed before
            // and green if it has been completed.
            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                //String forTestUsername = postList.getPost(position).getUsername();

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

                String startLocation = postList.getPost(position).getStartAddress();
                String endLocation = postList.getPost(position).getEndAddress();
                // Remove forTestUsername after
                tv.setText("Start: " + startLocation +"\nEnd: " + endLocation);

//                tv.setText(postList.getPost(position).getUsername());
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(24);

                return view;
            }
        };

        currentPostList.setAdapter(adapter);

        currentPostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CurrentUser.setCurrentPost(postList.getPost(position));
                Boolean found = false;
                postList.getPosts().clear();

                // Why is this for loop here? -- Added in (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                // To ensure that the posts with the user offer in it is not in the list
                for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
                    if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                            (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                            (p.getStatus().equals("Pending Offer") || p.getStatus().equals("Pending Approval"))) {
                        postList.addPost(p);
                    }
                }
                for (Post p : postList.getPosts()) {
                    if (CurrentUser.getCurrentPost().getId().equals(p.getId())) {
                        CurrentUser.setCurrentPost(p);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    Intent intent = new Intent(ProvideARideUIActivity.this, RequestDetailsUIActivity.class);
                    adapter.notifyDataSetChanged();
                    startActivity(intent);
                }
                else {
                    CurrentUser.setCurrentPost(null);
                    Toast.makeText(ProvideARideUIActivity.this, "Selected Post Request Unavailable. Updated List.", Toast.LENGTH_SHORT).show();
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

//        PostListMainController.getPostList(ProvideARideUIActivity.this).addListener(new Listener() {
//            @Override
//            public void update()
//            {
//                postList.getPosts().clear();
//
//                for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
//                    if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
//                            (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
//                            (p.getStatus().equals("Pending Offer") || p.getStatus().equals("Pending Approval"))) {
//                        postList.addPost(p);
//                    }
//                }
//
//                adapter.notifyDataSetChanged();
//                PostListMainController.updateOfflinePosts(ProvideARideUIActivity.this);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        postList.getPosts().clear();
        for(Post p : PostListMainController.getPostList(ProvideARideUIActivity.this).getPosts()) {
            if (!(p.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) &&
                    (!p.getDriverOffers().contains(CurrentUser.getCurrentUser().getUsername())) &&
                    (p.getStatus().equals("Pending Offer") || p.getStatus().equals("Pending Approval"))) {
                postList.addPost(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Long clicking on a post will give the user the option to delete the post
     *
     * @param post the post to be deleted
     */
    public void createDeletionDialog(Post post) {

        final Post postToRemove = post;

        // Build the dialog
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ProvideARideUIActivity.this);
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
                    PostListMainController.getPostList(ProvideARideUIActivity.this).deletePost(postToRemove);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(ProvideARideUIActivity.this, "Post Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        deleteDialog.show();
    }
}
