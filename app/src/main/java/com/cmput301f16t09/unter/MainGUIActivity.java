package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The type Main gui activity.
 */
public class MainGUIActivity extends AppCompatActivity {

    private Activity activity = this;
    private EditText mainScreenUsername;
    private EditText mainScreenPassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_aride_ui);
    }
    public void test_add() {
        setResult(RESULT_OK);
        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
        UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
        addUserTask.execute(new_user);
    }
    public void verifyLoginCredentials(View v) {
        EditText usernameInput = (EditText) findViewById(R.id.mainScreenUsername);
        EditText passwordInput = (EditText) findViewById(R.id.mainScreenPassword);
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
//        CurrentUser currentUser = new CurrentUser();
        // Verify with UserListOnlineController before changing intent
        if (UserListOnlineController.verifyLogin(username, password)) {
            // Set the current user before intent change
//            currentUser.setCurrentUser();
            Intent intent = new Intent(MainGUIActivity.this, MainScreenUIActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();

        }
    }

    public void createNewUser(View v) {
//        Intent intent = new Intent(MainGUIActivity.this, CreateNewUserUIActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(MainGUIActivity.this, CreateNewUserUIActivity.class);
        startActivity(intent);

    }
}
