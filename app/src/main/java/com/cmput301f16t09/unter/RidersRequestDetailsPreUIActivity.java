package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;

/**
 * The type Riders request details pre ui activity. This activity displays a list of drivers that
 * have offered a ride for the users ride request post. The user can view the user's profile who
 * offered the post, cancel the post, or confirm an offer made by a user for the post. There is also
 * basic information (no map) for the post.
 * @author Daniel
 */
public class RidersRequestDetailsPreUIActivity extends AppCompatActivity {

    /* Called when the activity is called and creates the activity. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_pre_ui);
        ListView potentialDriversListView = (ListView) findViewById(R.id.listViewRideRequestDetailsRiderOffers);

        /**
         * Get and display all of the information from the post that is stored in CurrentUser
         * @see CurrentUser
         */
        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPreCurrentStatus);
        String currentStatus = CurrentUser.getCurrentPost().getStatus();
        tvCurrentStatus.setText(currentStatus);

        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPreStartLocationName);
        String startLocation = CurrentUser.getCurrentPost().getStartAddress();
        tvStartLocation.setText(startLocation);

        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPreEndLocationName);
        String endLocation = CurrentUser.getCurrentPost().getEndAddress();
        tvEndLocation.setText(endLocation);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPreOfferedFare);
        String offeredFare = CurrentUser.getCurrentPost().getFare();
        tvOfferedFare.setText(offeredFare);

        /**
         * Create an adapter for the ListView that will display all of the posts.
         */
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrentUser.getCurrentPost().getDriverOffers()) {

            // Create the view for the habits. Habits name is red if it has not been completed before
            // and green if it has been completed.
            // Code to change text from: http://android--code.blogspot.ca/2015/08/android-listview-text-color.html
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setText(CurrentUser.getCurrentPost().getDriverOffers().get(position));
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        // Attach the adapter to the ListView
        potentialDriversListView.setAdapter(adapter);

        /**
         * If there is a long click on a post, the create a dialog fragment for the option to view
         * that users profile, or to accept the offer made by the user.
         */
        potentialDriversListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RidersRequestDetailsPreUIActivity.this);
                final String driverUsername = CurrentUser.getCurrentPost().getDriverOffers().get(position); //unsure about this

                /**
                 * Setting up the dialog box for accepting the driver. The status is updated and
                 * pushed onto elasticsearch. The screen directly changes to RidersRequestDetailsPostUIActivity
                 * @see PostListOnlineController
                 * @see CurrentUser
                 * @see RidersRequestDetailsPostUIActivity
                 */
                builder.setMessage("Actions for Driver:         "+ driverUsername);
                builder.setPositiveButton(R.string.choose_driver, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Boolean awaitingCompletion = false;
                        try {
                            CurrentUser.updateCurrentUser();
                            if (CurrentUser.getCurrentUser().getMyOffers().size() == 1) {
                                PostListOnlineController.SearchPostListsTask searchPostListsTask1 = new PostListOnlineController.SearchPostListsTask();
                                searchPostListsTask1.execute("documentId", CurrentUser.getCurrentUser().getMyOffers().get(0));
                                ArrayList<Post> temp = searchPostListsTask1.get();
                                if (!temp.isEmpty()) {
                                    if (temp.get(0).getStatus().equals("Awaiting Completion")) {
                                        awaitingCompletion = true;
                                    }
                                }
                            }
                            if (awaitingCompletion == true) {
                                Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Please complete current driver offer before selecting a rider", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                // Cycle through lists of potential drivers and remove the post from
                                // their offer list because a rider has been chosen.
                                for (int i = 0; i < CurrentUser.getCurrentPost().getDriverOffers().size(); i++){
                                    if (!driverUsername.equals(CurrentUser.getCurrentPost().getDriverOffers().get(i))) {
                                        UserListOnlineController.SearchUserListsTask searchUserListsTask1 = new UserListOnlineController.SearchUserListsTask();
                                        searchUserListsTask1.execute("username", CurrentUser.getCurrentPost().getDriverOffers().get(i));
                                        User driver = searchUserListsTask1.get().get(0);
                                        driver.getMyOffers().remove(CurrentUser.getCurrentPost().getId());
                                        UserListOnlineController.UpdateUsersTask updateUserListstask1 = new UserListOnlineController.UpdateUsersTask();
                                        updateUserListstask1.execute(driver);
                                        updateUserListstask1.get();
                                    }
                                }

                                // Go to all other requests made by user and delete them.
                                for (int i = 0; i < CurrentUser.getCurrentUser().getMyRequests().size(); i++){
                                    if (!CurrentUser.getCurrentUser().getMyRequests().get(i).equals(CurrentUser.getCurrentPost().getId())) {
                                        PostListOnlineController.SearchPostListsTask searchPostListsTask2 = new PostListOnlineController.SearchPostListsTask();
                                        searchPostListsTask2.execute("documentId", CurrentUser.getCurrentUser().getMyRequests().get(i));
                                        ArrayList<Post> templist = searchPostListsTask2.get();
                                        if (!templist.isEmpty()) {
                                            for (int j = 0; j < templist.get(0).getDriverOffers().size(); j++){
                                                UserListOnlineController.SearchUserListsTask searchUserListsTask2 = new UserListOnlineController.SearchUserListsTask();
                                                searchUserListsTask2.execute("username", CurrentUser.getCurrentPost().getDriverOffers().get(j));
                                                User driver2 = searchUserListsTask2.get().get(0);
                                                driver2.getMyOffers().remove(templist.get(0).getId());
                                                UserListOnlineController.UpdateUsersTask updateUserListstask2 = new UserListOnlineController.UpdateUsersTask();
                                                updateUserListstask2.execute(driver2);
                                                updateUserListstask2.get();
                                            }
                                        }
                                        PostListOnlineController.DeletePostsTaskId deletePostsTaskid1 = new PostListOnlineController.DeletePostsTaskId();
                                        deletePostsTaskid1.execute(CurrentUser.getCurrentUser().getMyRequests().get(i));
                                        deletePostsTaskid1.get();
                                    }
                                }

                                // Set the selected post set it's status to Awaiting Completion
                                CurrentUser.getCurrentPost().setStatus("Awaiting Completion");
                                CurrentUser.getCurrentPost().pickDriver(driverUsername);

                                // Update that selected post in elastic search
                                PostListOnlineController.UpdatePostsTask updatePostsTask2 = new PostListOnlineController.UpdatePostsTask();
                                updatePostsTask2.execute(CurrentUser.getCurrentPost());
                                updatePostsTask2.get();

                                // Get the chosen driver from elastic search
                                UserListOnlineController.SearchUserListsTask searchUserListsTask3 = new UserListOnlineController.SearchUserListsTask();
                                searchUserListsTask3.execute("username", driverUsername);
                                User driver3 = searchUserListsTask3.get().get(0);

                                // Delete all of the offers of the driver on each post that
                                // the driver has offered to.
                                for (int i = 0; i < driver3.getMyOffers().size(); i++){
                                    if (!driver3.getMyOffers().get(i).equals(CurrentUser.getCurrentPost().getId())) {
                                        PostListOnlineController.SearchPostListsTask searchPostListsTask3 = new PostListOnlineController.SearchPostListsTask();
                                        searchPostListsTask3.execute("documentId", driver3.getMyOffers().get(i));
                                        ArrayList<Post> templist2 = searchPostListsTask3.get();
                                        if(!templist2.isEmpty()) {
                                            templist2.get(0).getDriverOffers().remove(driverUsername);
                                            PostListOnlineController.UpdatePostsTask updatePostsTask3 = new PostListOnlineController.UpdatePostsTask();
                                            updatePostsTask3.execute(templist2.get(0));
                                            updatePostsTask3.get();
                                        }
                                    }
                                }

                                // Set's the chosen's driver MyOffers list to only that one post
                                driver3.choosenAsDriver(CurrentUser.getCurrentPost().getId());
                                UserListOnlineController.UpdateUsersTask updateUsersTask3 = new UserListOnlineController.UpdateUsersTask();
                                updateUsersTask3.execute(driver3);
                                updateUsersTask3.get();

                                // Delete all of the offers of the user on each post that
                                // the user has offered to.
                                for (int i = 0; i < CurrentUser.getCurrentUser().getMyOffers().size(); i++){
                                    PostListOnlineController.SearchPostListsTask searchPostListsTask4 = new PostListOnlineController.SearchPostListsTask();
                                    searchPostListsTask4.execute("documentId", CurrentUser.getCurrentUser().getMyOffers().get(i));
                                    ArrayList<Post> templist3 = searchPostListsTask4.get();
                                    if (!templist3.isEmpty()) {
                                        templist3.get(0).getDriverOffers().remove(CurrentUser.getCurrentUser().getUsername());
                                        PostListOnlineController.UpdatePostsTask updatePostsTask4 = new PostListOnlineController.UpdatePostsTask();
                                        updatePostsTask4.execute(templist3.get(0));
                                        updatePostsTask4.get();
                                    }

                                }

                                // Finalize choosing a request and clear out MyOffers
                                CurrentUser.getCurrentUser().choosenADriver(CurrentUser.getCurrentPost().getId());
                                CurrentUser.getCurrentUser().getMyOffers().clear();
                                UserListOnlineController.UpdateUsersTask updateUsersTask4 = new UserListOnlineController.UpdateUsersTask();
                                updateUsersTask4.execute(CurrentUser.getCurrentUser());
                                updateUsersTask4.get();
                                Toast.makeText(RidersRequestDetailsPreUIActivity.this, "OK", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();

                                Intent intent = new Intent(RidersRequestDetailsPreUIActivity.this,RidersRequestDetailsPostUIActivity.class);
                                startActivity(intent);
                                try {
                                    Thread.sleep(500);
                                }
                                catch (Exception e) {
                                }
                                finish();
                            }
                        }
                        catch (Exception e) {
                            Log.i("Error", "Unable to Update Post/User Information");
                        }
                    }
                });

                /**
                 * Create a cancel button to close the dialog
                 */
                builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Cancel", Toast.LENGTH_SHORT).show();

                    }
                });

                /**
                 * Create a button to view the user's profile. The activity changes to
                 * ViewProfileUIActivity, where the user's information will be displayed.
                 * @see ViewProfileUIActivity
                 */
                builder.setNeutralButton(R.string.view_profile, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(RidersRequestDetailsPreUIActivity.this, ViewProfileUIActivity.class);
                        intent.putExtra("User", driverUsername);
                        startActivity(intent);

                        Toast.makeText(RidersRequestDetailsPreUIActivity.this, "viewing profile", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();
                return true;
            }
        });
    }

    /**
     * When the Cancel Request button is pressed, then delete the post from elastic search and from
     * the current postlist. Update the user's information about current posts
     * @see PostListOnlineController
     * @see CurrentUser
     * @param v the v
     */
    public void cancelRequest(View v) {
        try {
            CurrentUser.updateCurrentUser();
            // Remove all of the offers attached to the driver's myoffers list
            // Update each user
            for (String username : CurrentUser.getCurrentPost().getDriverOffers()) {
                UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                searchUserListsTask.execute("username", username);
                User driver = searchUserListsTask.get().get(0);
                driver.getMyOffers().remove(CurrentUser.getCurrentPost().getId());
                UserListOnlineController.UpdateUsersTask updateUserListstask = new UserListOnlineController.UpdateUsersTask();
                updateUserListstask.execute(driver);
                updateUserListstask.get();
            }
//            PostListMainController.deletePosts(CurrentUser.getCurrentPost(), RidersRequestDetailsPreUIActivity.this);

            // Deletes the post from the elastic search database
            PostListOnlineController.DeletePostsTaskId deletePostsTaskid = new PostListOnlineController.DeletePostsTaskId();
            deletePostsTaskid.execute(CurrentUser.getCurrentPost().getId());
            deletePostsTaskid.get();

            CurrentUser.getCurrentUser().getMyRequests().remove(CurrentUser.getCurrentPost().getId());
            UserListOnlineController.UpdateUsersTask updateUserListstask = new UserListOnlineController.UpdateUsersTask();
            updateUserListstask.execute(CurrentUser.getCurrentUser());
            updateUserListstask.get();

            Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Successfully deleted the post!", Toast.LENGTH_SHORT).show();
            finish();

//            CurrentUser.decreasePostCount();
//            if (CurrentUser.postCount=0){
//                CurrentUser.setRole("User");
//            }
//            else{
//                CurrentUser.setRole("Rider");
//            }

        }
        catch (Exception e) {
            Log.i("Error", "Unable to Update Post/User Information");
        }
    }

}
