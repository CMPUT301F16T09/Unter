package com.cmput301f16t09.unter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A view used to view user profiles.
 */
public class ViewProfileUIActivity extends AppCompatActivity {
    /**
     * The Title.
     */
    TextView title ;
    /**
     * The Ph number.
     */
    TextView phNumber ;
    /**
     * The Name.
     */
    TextView name ;
    /**
     * The Email.
     */
    TextView email;

    /**
     * The Vehicle.
     */
    TextView vehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Viewing User Profile");
        setContentView(R.layout.activity_view_profile);

        // Finds the TextViews
        title = (TextView) findViewById(R.id.textUsernameTitle);
        phNumber = (TextView) findViewById(R.id.displayNumberTextView);
        name = (TextView) findViewById(R.id.displayNameTextView);
        email = (TextView) findViewById(R.id.displayEmailTextView);
        vehicle = (TextView) findViewById(R.id.displayVehicleTextView);

        // Retrieve the user and if information s restricted
        Bundle extras = getIntent().getExtras();
        String fetchThisUser = extras.getString("User");
        Boolean isRestricted = extras.getBoolean("isRestricted");
        title.setText(fetchThisUser);

        try {
            // Finds the user from elastic search
            UserListOnlineController.SearchUserListsTask getUserInfoTask = new UserListOnlineController.SearchUserListsTask();
            getUserInfoTask.execute("username",fetchThisUser);
            ArrayList<User> results = getUserInfoTask.get();
            User fetched = results.get(0);

            // Sets the profile information to be viewed by the user
            name.setText(fetched.getName());
            phNumber.setText(fetched.getPhoneNumber());
            email.setText(fetched.getEmail());
            if (isRestricted) {
                vehicle.setText("Restricted");
            }
            else {
                vehicle.setText(fetched.getVehicle());
            }
        }
        catch(Exception e){
        }
    }
}
