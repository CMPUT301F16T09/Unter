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

                        try {
                            if (CurrentUser.getCurrentUser().getMyOffers().size() == 1) {
                                PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                                searchPostListsTask.execute("documentId", CurrentUser.getCurrentUser().getMyOffers().get(0));
                                ArrayList<Post> temp = searchPostListsTask.get();
                                if (!temp.isEmpty()) {
                                    if (temp.get(0).getStatus().equals("Awaiting Completion")) {
                                        Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Please complete current driver offer before selecting a rider", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }
                            // Cycle through lists of potential drivers and remove the post from
                            // their offer list because a rider has been chosen.
                            for (int i = 0; i < CurrentUser.getCurrentPost().getDriverOffers().size(); i++){
                                if (!driverUsername.equals(CurrentUser.getCurrentPost().getDriverOffers().get(i))) {
                                    UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                                    searchUserListsTask.execute("username", CurrentUser.getCurrentPost().getDriverOffers().get(i));
                                    User driver = searchUserListsTask.get().get(0);
                                    driver.getMyOffers().remove(CurrentUser.getCurrentPost().getId());
                                    UserListOnlineController.UpdateUsersTask updateUserListstask = new UserListOnlineController.UpdateUsersTask();
                                    updateUserListstask.execute(driver);
                                    updateUserListstask.get();
                                }
                            }

                            // Go to all other requests made by user and delete them.
                            for (int i = 0; i < CurrentUser.getCurrentUser().getMyRequests().size(); i++){
                                if (!CurrentUser.getCurrentUser().getMyRequests().get(i).equals(CurrentUser.getCurrentPost().getId())) {
                                    PostListOnlineController.DeletePostsTaskId deletePostsTaskid = new PostListOnlineController.DeletePostsTaskId();
                                    deletePostsTaskid.execute(CurrentUser.getCurrentUser().getMyRequests().get(i));
                                    deletePostsTaskid.get();
                                }
                            }

                            // Set the selected post set it's status to Awaiting Completion
                            CurrentUser.getCurrentPost().setStatus("Awaiting Completion");
                            CurrentUser.getCurrentPost().pickDriver(driverUsername);

                            // Update that selected post in elastic search
                            PostListOnlineController.UpdatePostsTask upt = new PostListOnlineController.UpdatePostsTask();
                            upt.execute(CurrentUser.getCurrentPost());
                            upt.get();

                            // Get the chosen driver from elastic search
                            UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                            searchUserListsTask.execute("username", driverUsername);
                            User driver = searchUserListsTask.get().get(0);

                            // Delete all of the offers of the driver on each post that
                            // the driver has offered to.
                            for (int i = 0; i < driver.getMyOffers().size(); i++){
                                if (!driver.getMyOffers().get(i).equals(CurrentUser.getCurrentPost().getId())) {
                                    PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                                    searchPostListsTask.execute("documentId", driver.getMyOffers().get(i));
                                    Post temp = searchPostListsTask.get().get(0);
                                    temp.getDriverOffers().remove(driverUsername);
                                    PostListOnlineController.UpdatePostsTask updatePostsTask = new PostListOnlineController.UpdatePostsTask();
                                    updatePostsTask.execute(temp);
                                    updatePostsTask.get();
                                }
                            }

                            // Set's the chosen's driver MyOffers list to only that one post
                            driver.choosenAsDriver(CurrentUser.getCurrentPost().getId());
                            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
                            uut.execute(driver);
                            uut.get();

                            // Delete all of the offers of the user on each post that
                            // the user has offered to.
                            for (int i = 0; i < CurrentUser.getCurrentUser().getMyOffers().size(); i++){
                                PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
                                searchPostListsTask.execute("documentId", CurrentUser.getCurrentUser().getMyOffers().get(i));
                                Post temp = searchPostListsTask.get().get(0);
                                temp.getDriverOffers().remove(CurrentUser.getCurrentUser().getUsername());
                                PostListOnlineController.UpdatePostsTask updatePostsTask = new PostListOnlineController.UpdatePostsTask();
                                updatePostsTask.execute(temp);
                                updatePostsTask.get();
                            }

                            // Finalize choosing a request and clear out MyOffers
                            CurrentUser.getCurrentUser().choosenADriver(CurrentUser.getCurrentPost().getId());
                            CurrentUser.getCurrentUser().getMyOffers().clear();
                            UserListOnlineController.UpdateUsersTask updateUsersTask = new UserListOnlineController.UpdateUsersTask();
                            updateUsersTask.execute(CurrentUser.getCurrentUser());
                            updateUsersTask.get();
                            Toast.makeText(RidersRequestDetailsPreUIActivity.this, "OK", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();

                            Intent intent = new Intent(RidersRequestDetailsPreUIActivity.this,RidersRequestDetailsPostUIActivity.class);
                            startActivity(intent);

                            finish();
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
        }
        catch (Exception e) {
            Log.i("Error", "Unable to Update Post/User Information");
        }
    }

}
