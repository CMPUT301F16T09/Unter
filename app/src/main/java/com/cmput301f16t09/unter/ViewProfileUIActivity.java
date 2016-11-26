package com.cmput301f16t09.unter;

/**
 * Created by Kelly on 2016-11-10.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The type View profile ui activity.
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

    TextView vehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        title = (TextView) findViewById(R.id.textUsernameTitle);
        phNumber = (TextView) findViewById(R.id.displayNumberTextView);
        name = (TextView) findViewById(R.id.displayNameTextView);
        email = (TextView) findViewById(R.id.displayEmailTextView);
        vehicle = (TextView) findViewById(R.id.displayVehicleTextView);

        Bundle extras = getIntent().getExtras();
        String fetchThisUser = extras.getString("User");
        Boolean isRestricted = extras.getBoolean("isRestricted");

        title.setText(fetchThisUser);

        try {
            UserListOnlineController.SearchUserListsTask getUserInfoTask = new UserListOnlineController.SearchUserListsTask();
            getUserInfoTask.execute("username",fetchThisUser);
            ArrayList<User> results = getUserInfoTask.get();
            User fetched = results.get(0);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
