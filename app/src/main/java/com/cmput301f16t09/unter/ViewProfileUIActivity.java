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
        String fetchThisUser = extras.getString("User");

        title.setText(fetchThisUser);

        try {
            UserListOnlineController.SearchUserListsTask getUserInfoTask = new UserListOnlineController.SearchUserListsTask();
            getUserInfoTask.execute("username",fetchThisUser);
            ArrayList<User> results = getUserInfoTask.get();
            User fetched = results.get(0);

            name.setText(fetched.getName());
            phNumber.setText(fetched.getPhoneNumber());
            email.setText(fetched.getEmail());
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
