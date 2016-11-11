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

public class ViewProfileUIActivity extends AppCompatActivity {
    TextView title ;
    TextView phNumber ;
    TextView name ;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        title = (TextView) findViewById(R.id.textUsernameTitle);
        phNumber = (TextView) findViewById(R.id.displayNumberTextView);
        name = (TextView) findViewById(R.id.displayNameTextView);
        email = (TextView) findViewById(R.id.displayEmailTextView);

        Bundle extras = getIntent().getExtras();
        String riderUserName = extras.getString("Rider");

        title.setText(riderUserName);

        try {
            UserListOnlineController.SearchUserListsTask getRiderInfoTask = new UserListOnlineController.SearchUserListsTask();
            getRiderInfoTask.execute("username",riderUserName);
            ArrayList<User> results = getRiderInfoTask.get();
            User rider = results.get(0);

            name.setText(rider.getName());
            phNumber.setText(rider.getPhoneNumber());
            email.setText(rider.getEmail());
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
