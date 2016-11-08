package com.cmput301f16t09.unter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

/**
 * The type Main gui activity.
 */
public class MainGUIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gui);
    }
    public void test_add() {
        setResult(RESULT_OK);
        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
        addUserTask.execute(new_user);
    }
    public void verifyLoginCredentials(View v) {

        // Verify with UserListOnlineController before changing intent
        //Intent intent = new Intent(MainGUIActivity.this, MainScreenUIActivity.class);
        setResult(RESULT_OK);
        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
        addUserTask.execute(new_user);
        //startActivity(intent);
    }

    public void createNewUser(View v) {
//        Intent intent = new Intent(MainGUIActivity.this, CreateNewUserUIActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(MainGUIActivity.this, CreateNewUserUIActivity.class);
        startActivity(intent);
    }
}
