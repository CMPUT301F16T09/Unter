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
                        CurrentUser.getCurrentPost().setStatus("Awaiting Completion");
                        CurrentUser.getCurrentPost().pickDriver(driverUsername);

                        try {
                            PostListOnlineController.UpdatePostsTask upt = new PostListOnlineController.UpdatePostsTask();
                            upt.execute(CurrentUser.getCurrentPost());
                            upt.get();

                            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
                            uut.execute(CurrentUser.getCurrentUser());
                            uut.get();

                            Toast.makeText(RidersRequestDetailsPreUIActivity.this, "OK", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();

                            PostListMainController.updateMainOfflinePosts(RidersRequestDetailsPreUIActivity.this);

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
            PostListMainController.deletePosts(CurrentUser.getCurrentPost(), RidersRequestDetailsPreUIActivity.this);

            CurrentUser.getCurrentUser().getMyOffers().deletePost(CurrentUser.getCurrentPost());

            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
            uut.execute(CurrentUser.getCurrentUser());
            uut.get();
            Toast.makeText(RidersRequestDetailsPreUIActivity.this, "Successfully deleted the offer!", Toast.LENGTH_SHORT).show();

            finish();
        }
        catch (Exception e) {
            Log.i("Error", "Deletion Error");
        }
    }

}
